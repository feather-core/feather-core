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

package org.feathercore.protocol.packet.login.client;

import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.packet.Packet;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class C01EncryptionResponse implements Packet {

    public final static int ID = 0x01;

    private byte[] secretKeyEncrypted = new byte[0];
    private byte[] verifyTokenEncrypted = new byte[0];

    public C01EncryptionResponse(byte[] secretKeyEncrypted, byte[] verifyTokenEncrypted) {
        this.secretKeyEncrypted = secretKeyEncrypted;
        this.verifyTokenEncrypted = verifyTokenEncrypted;
    }

    public C01EncryptionResponse() {
    }

    public SecretKey getSecretKey(PrivateKey key) {
        throw new UnsupportedOperationException("Should be recreated using Mojang API");
        // TODO: return CryptManager.decryptSharedKey(key, this.secretKeyEncrypted);
    }

    public byte[] getVerifyToken(PrivateKey key) {
        throw new UnsupportedOperationException("Should be recreated using Mojang API");
        // TODO: return key == null ? this.verifyTokenEncrypted : CryptManager.decryptData(key, this.verifyTokenEncrypted);
    }

    @Override
    public void write(@NotNull Buffer buffer) {
        buffer.writeByteArray(this.secretKeyEncrypted);
        buffer.writeByteArray(this.verifyTokenEncrypted);
    }

    @Override
    public void read(@NotNull Buffer buffer) {
        this.secretKeyEncrypted = buffer.readByteArray();
        this.verifyTokenEncrypted = buffer.readByteArray();
    }

    @Override
    public int getID() {
        return ID;
    }
}
