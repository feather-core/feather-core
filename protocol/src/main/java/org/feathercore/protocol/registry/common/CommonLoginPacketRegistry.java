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

import org.feathercore.protocol.Connection;
import org.feathercore.protocol.minecraft.packet.MinecraftPacket;
import org.feathercore.protocol.minecraft.packet.login.client.LoginPacketClientLoginStart;
import org.feathercore.protocol.netty.NettyConnection;
import org.feathercore.protocol.netty.util.NettyAttributes;
import org.feathercore.protocol.packet.PacketType;
import org.feathercore.protocol.registry.ArrayBasedPacketRegistry;
import org.feathercore.protocol.registry.PacketRegistry;

import java.util.function.BiConsumer;

public class CommonLoginPacketRegistry {

    @SuppressWarnings("unchecked")
    public static PacketRegistry<MinecraftPacket> createNew() {
        return ArrayBasedPacketRegistry.Builder.create()
                                               .attachListener(connection -> ((NettyConnection)connection)
                                                       .setAttributeValue(
                                                               NettyAttributes.LOGIN_STATE_ATTRIBUTE_KEY,
                                                               LoginState.USERNAME
                                                       ))
                                               .addPacket(
                                                       PacketType.create(LoginPacketClientLoginStart.class, LoginPacketClientLoginStart::new),
                                                       (BiConsumer<Connection, LoginPacketClientLoginStart>) (connection, packet) -> {
                                                           // TODO
                                                       }
                                               )
                                               .build();
    }

    public enum LoginState {

        USERNAME, ENCRYPT
    }
}
