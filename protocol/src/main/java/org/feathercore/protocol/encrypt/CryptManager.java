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

package org.feathercore.protocol.encrypt;

import lombok.experimental.UtilityClass;
import org.feathercore.protocol.minecraft.packet.login.server.LoginPacketServerEncryptionRequest;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class CryptManager {

    private final PublicKey PUBLIC_KEY;
    private final PrivateKey PRIVATE_KEY;

    public LoginPacketServerEncryptionRequest newEncryptionRequest() {
        Random random = ThreadLocalRandom.current();
        String serverHash = Long.toString(random.nextLong(), 16);
        byte[] verify = new byte[4];
        random.nextBytes(verify);
        return new LoginPacketServerEncryptionRequest(serverHash, PUBLIC_KEY, verify);
    }

    public PublicKey decodePublicKey(byte[] encoded) {
        try {
            EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Missing RSA algorithm", ex);
        } catch (InvalidKeySpecException ex) {
            throw new IllegalArgumentException("Invalid encoded key!", ex);
        }
    }

    public SecretKey decryptSharedKey(byte[] secretKey, byte[] verifyToken) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
            byte[] decrypted = cipher.doFinal(verifyToken);

            if (!Arrays.equals(verifyToken, decrypted)) {
                throw new IllegalStateException("Key pairs do not match!");
            }

            cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
            return new SecretKeySpec(cipher.doFinal(secretKey), "AES");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("No RSA algorithm!", ex);
        } catch (InvalidKeyException ex) {
            throw new IllegalArgumentException("Invalid encoded key!", ex);
        } catch (NoSuchPaddingException ex) {
            throw new RuntimeException("Padding mechanism not available!", ex);
        } catch (BadPaddingException ex) {
            throw new IllegalArgumentException("Bad padding!", ex);
        } catch (IllegalBlockSizeException ex) {
            throw new IllegalArgumentException("Input data is not a multiple of 16!", ex);
        }
    }

    public byte[] getPublicEncoded() {
        return PUBLIC_KEY.getEncoded();
    }

    static {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair keyPair = generator.generateKeyPair();
            PRIVATE_KEY = keyPair.getPrivate();
            PUBLIC_KEY = keyPair.getPublic();
        } catch (NoSuchAlgorithmException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}
