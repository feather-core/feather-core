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
import io.netty.channel.ChannelFutureListener;
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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommonLoginPacketRegistry {

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
                                                           checkState((NettyConnection) connection, LoginState.ENCRYPT);
                                                           // TODO
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

        USERNAME, ENCRYPT
    }
}
