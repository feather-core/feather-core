/*
 * Copyright 2019 Feather Core
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.feathercore.protocol.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.netty.codec.CipherDecoder;
import org.feathercore.protocol.netty.codec.CipherEncoder;
import org.feathercore.protocol.netty.codec.Compressor;
import org.feathercore.protocol.netty.codec.Decompressor;
import org.feathercore.protocol.netty.util.NettyAttributes;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.registry.PacketRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.function.Consumer;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@RequiredArgsConstructor
public class NettyConnection implements Connection {

    @Getter
    private final ChannelHandlerContext context;

    private boolean encrypted;

    @Override
    public void write(@NotNull byte[] bytes) {
        this.context.writeAndFlush(bytes, context.voidPromise());
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) this.context.channel().remoteAddress();
    }

    @Override
    public boolean isActive() {
        return this.context.channel().isActive();
    }

    @Override
    public boolean isEncrypted() {
        return this.encrypted;
    }

    public <T> void setAttributeValue(@NonNull AttributeKey<T> key, @Nullable T newValue) {
        if (!isActive()) {
            throw new IllegalStateException("This connection is not connected anymore: can't change attribute's value");
        }
        context.channel().attr(key).set(newValue);
    }

    public <T> T getAttributeValue(@NonNull AttributeKey<T> key) {
        if (!isActive()) {
            throw new IllegalStateException("This connection is not connected anymore: can't get attribute's value");
        }
        return context.channel().attr(key).get();
    }

    public void removeAttribute(@NonNull AttributeKey<?> key) {
        if (!isActive()) {
            throw new IllegalStateException("This connection is not connected anymore: can't remove attribute");
        }
        Channel channel = context.channel();
        if (!channel.hasAttr(key)) {
            throw new IllegalStateException("Channel does not contain requested attribute key!");
        }
        channel.attr(key).set(null);
    }

    public void changePacketRegistry(@NonNull PacketRegistry<? extends Packet> packetRegistry) {
        HandlerBoss boss = NettyAttributes.getAttribute(context, NettyAttributes.HANDLER_BOSS_ATTRIBUTE_KEY);
        if (boss == null) {
            throw new IllegalStateException("This connection is not connected anymore: packet registry can't be changed");
        }
        PacketRegistry<?> old = boss.getPacketRegistry();
        if (old != null) {
            old.registryDetached(this);
        }
        boss.setPacketRegistry(packetRegistry);
        packetRegistry.registryAttached(this);
    }

    public void enableEncryption(@NonNull SecretKey key) {
        this.encrypted = true;
        this.context.channel().pipeline()
                .addBefore("splitter", "decrypt", new CipherDecoder(createNetCipherInstance(2, key)))
                .addBefore("prepender", "encrypt", new CipherEncoder(createNetCipherInstance(1, key)));
    }

    public void setCompressionThreshold(int threshold) {
        Channel channel = this.context.channel();
        if (threshold >= 0) {
            ChannelHandler handler = channel.pipeline().get("compressor");
            if (handler instanceof Compressor) {
                ((Compressor) handler).setThreshold(threshold);
            } else {
                channel.pipeline().addAfter("prepender", "compressor", new Compressor(threshold));
            }
            handler = channel.pipeline().get("decompressor");
            if (handler instanceof Decompressor) {
                ((Decompressor) handler).setThreshold(threshold);
            } else {
                channel.pipeline().addBefore("decoder", "decompressor", new Decompressor(threshold));
            }
        } else {
            if (channel.pipeline().get("compressor") instanceof Compressor) {
                channel.pipeline().remove("compressor");
            }
            if (channel.pipeline().get("decompressor") instanceof Decompressor) {
                channel.pipeline().remove("decompressor");
            }
        }
    }

    private Cipher createNetCipherInstance(int opMode, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(opMode, key, new IvParameterSpec(key.getEncoded()));
            return cipher;
        } catch (GeneralSecurityException generalsecurityexception) {
            throw new RuntimeException(generalsecurityexception);
        }
    }

}
