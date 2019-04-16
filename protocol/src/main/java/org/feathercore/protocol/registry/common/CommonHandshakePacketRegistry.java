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

import org.feathercore.protocol.minecraft.packet.MinecraftPacket;
import org.feathercore.protocol.minecraft.packet.client.HandshakePacketClientHandshake;
import org.feathercore.protocol.packet.PacketType;
import org.feathercore.protocol.registry.ArrayBasedPacketRegistry;
import org.feathercore.protocol.registry.PacketRegistry;

import java.util.function.BiConsumer;

/**
 * Created by k.shandurenko on 16/04/2019
 */
public class CommonHandshakePacketRegistry {

    @SuppressWarnings("unchecked")
    public static PacketRegistry<MinecraftPacket> createNew() {
        //noinspection Convert2Lambda bug in javac
        return ArrayBasedPacketRegistry.Builder.create()
                .addPacket(
                        PacketType.create(HandshakePacketClientHandshake.class, HandshakePacketClientHandshake::new),
                        new BiConsumer() {
                            @Override
                            public void accept(final Object connection, final Object packet) {
                                //TODO
                            }
                        }
                )
                .build();
    }

}
