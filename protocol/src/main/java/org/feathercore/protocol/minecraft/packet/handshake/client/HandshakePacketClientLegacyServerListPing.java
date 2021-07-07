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

package org.feathercore.protocol.minecraft.packet.handshake.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.minecraft.packet.MinecraftPacket;
import org.jetbrains.annotations.NotNull;

/**
 * This packet uses a nonstandard format. It is never length-prefixed,
 * and the packet ID is an Unsigned Byte instead of a VarInt.
 *
 * While not technically part of the current protocol, legacy clients may
 * send this packet to initiate Server List Ping, and modern servers
 * should handle it correctly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class HandshakePacketClientLegacyServerListPing implements MinecraftPacket {

    public static final int ID = 0xFE;

    /**
     * Always 1 ({@link 0x01})
     */
    int payload;

    @Override
    public void read(@NotNull final Buffer buffer) {
        payload = buffer.readUnsignedByte();
    }

    @Override
    public int getId() {
        return ID;
    }
}
