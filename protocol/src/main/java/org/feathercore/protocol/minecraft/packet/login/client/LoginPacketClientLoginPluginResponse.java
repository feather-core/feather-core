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

package org.feathercore.protocol.minecraft.packet.login.client;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.minecraft.packet.MinecraftPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// FIXME: FIXME !!
// This class has optional fields. You need to write valid reader and writer.
// https://wiki.vg/Protocol#Login_Plugin_Response

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class LoginPacketClientLoginPluginResponse implements MinecraftPacket {

    public static final int ID = 0x02;

    /**
     * Should match ID from server
     */
    int messageId;

    /**
     * {@link true} if the client understands the request, {@link false} otherwise (no payload follows)
     */
    boolean successful;

    /**
     * Any data, depending on the channel
     *
     * @apiNote The length of this array must be inferred from the packet length
     */
    @Nullable byte[] data;

    @Override
    public void read(@NotNull final Buffer buffer) {
        messageId = buffer.readVarInt();
        successful = buffer.readBoolean();
        // Array size for data not found
        // FIXME: 18.04.2019 because `The length of this array must be inferred from the packet length.`
        buffer.readBytes(data);
    }

    @Override
    public int getId() {
        return ID;
    }
}
