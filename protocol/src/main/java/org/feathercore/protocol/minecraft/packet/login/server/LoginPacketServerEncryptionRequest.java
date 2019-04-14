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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.packet.Packet;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;

/**
 * Created by k.shandurenko on 09/04/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class LoginPacketServerEncryptionRequest implements Packet {

    public static final int ID = 0x01;

    String hashedServerID;
    PublicKey publicKey;
    byte[] verifyToken;

    @Override
    public void write(@NotNull final Buffer buffer) {
        buffer.writeString(this.hashedServerID);
        buffer.writeByteArray(this.publicKey.getEncoded());
        buffer.writeByteArray(this.verifyToken);
    }

    @Override
    public void read(@NotNull final Buffer buffer) {
        if (true) throw new UnsupportedOperationException("Should be recreated using Mojang API");
        this.hashedServerID = buffer.readString(20);
        // TODO: this.publicKey = CryptManager.decodePublicKey(buffer.readByteArray());
        this.verifyToken = buffer.readByteArray();
    }

    @Override
    public int getId() {
        return ID;
    }

}
