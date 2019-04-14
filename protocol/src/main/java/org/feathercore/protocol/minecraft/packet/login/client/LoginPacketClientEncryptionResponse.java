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
import org.feathercore.protocol.packet.Packet;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

/**
 * Created by k.shandurenko on 09/04/2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class LoginPacketClientEncryptionResponse implements Packet {

    public static final int ID = 0x01;

    byte[] secretKeyEncrypted = new byte[0];
    byte[] verifyTokenEncrypted = new byte[0];

    public SecretKey getSecretKey(@NotNull final PrivateKey key) {
        throw new UnsupportedOperationException("Should be recreated using Mojang API");
        // TODO: return CryptManager.decryptSharedKey(key, this.secretKeyEncrypted);
    }

    public byte[] getVerifyToken(@NotNull final PrivateKey key) {
        throw new UnsupportedOperationException("Should be recreated using Mojang API");
        // TODO: return key == null ? this.verifyTokenEncrypted : CryptManager.decryptData(key, this.verifyTokenEncrypted);
    }

    @Override
    public void write(@NotNull final Buffer buffer) {
        buffer.writeByteArray(this.secretKeyEncrypted);
        buffer.writeByteArray(this.verifyTokenEncrypted);
    }

    @Override
    public void read(@NotNull final Buffer buffer) {
        this.secretKeyEncrypted = buffer.readByteArray();
        this.verifyTokenEncrypted = buffer.readByteArray();
    }

    @Override
    public int getId() {
        return ID;
    }
}
