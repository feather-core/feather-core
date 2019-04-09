package org.feathercore.network.packet.login.client;

import org.feathercore.network.Buffer;
import org.feathercore.network.Packet;
import org.feathercore.util.CryptManager;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class C01EncryptionResponse extends Packet {

    private byte[] secretKeyEncrypted = new byte[0];
    private byte[] verifyTokenEncrypted = new byte[0];

    public C01EncryptionResponse(byte[] secretKeyEncrypted, byte[] verifyTokenEncrypted) {
        this.secretKeyEncrypted = secretKeyEncrypted;
        this.verifyTokenEncrypted = verifyTokenEncrypted;
    }

    public C01EncryptionResponse() {
    }

    public SecretKey getSecretKey(PrivateKey key) {
        return CryptManager.decryptSharedKey(key, this.secretKeyEncrypted);
    }

    public byte[] getVerifyToken(PrivateKey key) {
        return key == null ? this.verifyTokenEncrypted : CryptManager.decryptData(key, this.verifyTokenEncrypted);
    }

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeByteArray(this.secretKeyEncrypted);
        buffer.writeByteArray(this.verifyTokenEncrypted);
    }

    @Override
    public void read(Buffer buffer) {
        this.secretKeyEncrypted = buffer.readByteArray();
        this.verifyTokenEncrypted = buffer.readByteArray();
    }
}
