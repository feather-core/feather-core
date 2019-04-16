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

package org.feathercore.protocol.minecraft.packet.login.server;

import com.mojang.authlib.GameProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.minecraft.packet.MinecraftPacket;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created by k.shandurenko on 09/04/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class LoginPacketServerLoginSuccess implements MinecraftPacket {

    public static final int ID = 0x02;

    GameProfile profile;

    @Override
    public void write(@NotNull final Buffer buffer) {
        UUID uuid = this.profile.getId();
        buffer.writeString(uuid == null ? "" : uuid.toString());
        buffer.writeString(this.profile.getName());
    }

    @Override
    public void read(@NotNull final Buffer buffer) {
        UUID uuid = UUID.fromString(buffer.readString(36));
        String name = buffer.readString(16);
        this.profile = new GameProfile(uuid, name);
    }

    @Override
    public int getId() {
        return ID;
    }
}
