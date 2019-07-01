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

package org.feathercore.protocol.server;

import lombok.Getter;
import lombok.NonNull;
import org.feathercore.protocol.minecraft.packet.MinecraftPacket;
import org.feathercore.protocol.netty.NettyServer;
import org.feathercore.protocol.registry.PacketRegistry;
import org.feathercore.protocol.registry.common.CommonHandshakePacketRegistry;

import java.net.SocketAddress;

/**
 * Created by k.shandurenko on 16/04/2019
 */
public class SimpleMinecraftServer extends NettyServer<MinecraftPacket> {

    @Getter private final PacketRegistry<MinecraftPacket> packetRegistry = CommonHandshakePacketRegistry.createNew();

    public SimpleMinecraftServer(@NonNull final SocketAddress address) {
        super(address);
    }
}
