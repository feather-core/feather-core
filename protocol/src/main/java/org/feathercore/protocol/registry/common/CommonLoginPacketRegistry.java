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

package org.feathercore.protocol.registry.common;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.Recycler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.encrypt.EncryptionRequestContext;
import org.feathercore.protocol.minecraft.packet.MinecraftPacket;
import org.feathercore.protocol.minecraft.packet.login.client.LoginPacketClientEncryptionResponse;
import org.feathercore.protocol.minecraft.packet.login.client.LoginPacketClientLoginStart;
import org.feathercore.protocol.minecraft.packet.login.server.LoginPacketServerDisconnect;
import org.feathercore.protocol.minecraft.packet.login.server.LoginPacketServerEncryptionRequest;
import org.feathercore.protocol.netty.NettyConnection;
import org.feathercore.protocol.netty.util.NettyAttributes;
import org.feathercore.protocol.packet.PacketType;
import org.feathercore.protocol.registry.ArrayBasedPacketRegistry;
import org.feathercore.protocol.registry.PacketRegistry;
import org.feathercore.protocol.encrypt.CryptManager;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommonLoginPacketRegistry {

    private static final Recycler<RecyclableMessageDigest> RECYCLER = new Recycler<RecyclableMessageDigest>() {
        @Override
        protected RecyclableMessageDigest newObject(final Handle<RecyclableMessageDigest> handle) {
            MessageDigest sha;
            try {
                sha = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException("SHA-1 algorithm not available", ex);
            }
            return new RecyclableMessageDigest(handle, sha);
        }
    };

    @SuppressWarnings("unchecked")
    public static PacketRegistry<MinecraftPacket> createNew() {
        return ArrayBasedPacketRegistry.Builder.create()
                                               .exceptionHandler((BiConsumer<Connection, Throwable>) (connection, t) ->
                                                       connection.writeFuture(new LoginPacketServerDisconnect(/* TODO message */))
                                                                 .addListener(ChannelFutureListener.CLOSE)
                                               )
                                               .attachListener((Consumer<Connection>) connection -> ((NettyConnection)connection)
                                                       .setAttributeValue(
                                                               NettyAttributes.LOGIN_STATE_ATTRIBUTE_KEY,
                                                               LoginState.USERNAME
                                                       ))
                                               .addPacket(
                                                       PacketType.create(LoginPacketClientLoginStart.class, LoginPacketClientLoginStart::new),
                                                       (BiConsumer<Connection, LoginPacketClientLoginStart>) (connection, packet) -> {
                                                           NettyConnection nettyConnection = (NettyConnection) connection;
                                                           checkState(nettyConnection, LoginState.USERNAME);
                                                           GameProfile profile = packet.getProfile();
                                                           nettyConnection.setAttributeValue(NettyAttributes.LOGIN_PROFILE_ATTRIBUTE_KEY, profile);
                                                           // TODO: online mode
                                                           if (false) {
                                                               nettyConnection.setAttributeValue(NettyAttributes.LOGIN_STATE_ATTRIBUTE_KEY, LoginState.ENCRYPT);
                                                               LoginPacketServerEncryptionRequest request = CryptManager.newEncryptionRequest();
                                                               EncryptionRequestContext context = EncryptionRequestContext.fromPacket(request);
                                                               nettyConnection.setAttributeValue(NettyAttributes.ENCRYPTION_REQUEST_CONTEXT_KEY, context);
                                                               nettyConnection.write(request);
                                                           }
                                                           // TODO
                                                       }
                                               )
                                               .addPacket(
                                                       PacketType.create(LoginPacketClientEncryptionResponse.class, LoginPacketClientEncryptionResponse::new),
                                                       (BiConsumer<Connection, LoginPacketClientEncryptionResponse>) (connection, packet) -> {
                                                           NettyConnection nettyConnection = (NettyConnection) connection;
                                                           checkState(nettyConnection, LoginState.ENCRYPT);
                                                           nettyConnection.setAttributeValue(NettyAttributes.LOGIN_STATE_ATTRIBUTE_KEY, LoginState.FINISH);
                                                           // TODO
                                                           byte[] verifyToken = packet.getVerifyTokenEncrypted();
                                                           if (verifyToken.length == 0) {
                                                               throw new IllegalStateException("Empty encrypted verify token! Can't continue");
                                                           }
                                                           SecretKey sharedKey = CryptManager.decryptSharedKey(packet.getSecretKeyEncrypted(), verifyToken);
                                                           nettyConnection.enableEncryption(sharedKey);
                                                           EncryptionRequestContext context = nettyConnection.removeAttribute(NettyAttributes.ENCRYPTION_REQUEST_CONTEXT_KEY);
                                                           RecyclableMessageDigest sha = null;
                                                           String serverId;
                                                           try {
                                                               sha = RECYCLER.get();
                                                               MessageDigest md = sha.digest;
                                                               md.reset();
                                                               for (byte[] bit : new byte[][]{
                                                                       context.getServerHash().getBytes(StandardCharsets.ISO_8859_1),
                                                                       sharedKey.getEncoded(), CryptManager.getPublicEncoded()
                                                               }) {
                                                                   md.update(bit);
                                                               }
                                                               // Maybe we should replace it with something else
                                                               serverId = new BigInteger(md.digest()).toString(16);
                                                           } finally {
                                                               if (sha != null) {
                                                                   sha.handle.recycle(sha);
                                                               }
                                                           }
                                                           GameProfile profile = nettyConnection.getAttributeValue(NettyAttributes.LOGIN_PROFILE_ATTRIBUTE_KEY);
                                                           // TODO Thread & authliib management
                                                           ExecutorService service = null;
                                                           MinecraftSessionService sessionService = null;
                                                           service.execute(() -> {
                                                               if (!nettyConnection.isActive()) {
                                                                   return;
                                                               }
                                                               GameProfile result;
                                                               try {
                                                                   result = sessionService.hasJoinedServer(new GameProfile((UUID)null, profile.getName()), serverId);
                                                               } catch (AuthenticationUnavailableException ex) {
                                                                   nettyConnection.getContext().fireExceptionCaught(ex);
                                                                   return;
                                                               }
                                                               nettyConnection.setAttributeValue(NettyAttributes.LOGIN_PROFILE_ATTRIBUTE_KEY, result);
                                                               // TODO: initialize PLAY state
                                                           });
                                                       }
                                               )
                                               .build();
    }

    private static void checkState(NettyConnection connection, LoginState expected) {
        LoginState state = connection.getAttributeValue(NettyAttributes.LOGIN_STATE_ATTRIBUTE_KEY);
        if (expected != state) {
            throw new IllegalStateException("Expected login state: " + expected + ", but got: " + state);
        }
    }

    public enum LoginState {

        USERNAME,
        ENCRYPT,
        FINISH
    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    private static class RecyclableMessageDigest {

        final Recycler.Handle<RecyclableMessageDigest> handle;
        final MessageDigest digest;
    }
}
