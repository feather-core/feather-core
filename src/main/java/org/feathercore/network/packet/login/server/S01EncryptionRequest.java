package org.feathercore.network.packet.login.server;

import org.feathercore.network.Buffer;
import org.feathercore.network.Packet;
import org.feathercore.util.CryptManager;

import java.security.PublicKey;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class S01EncryptionRequest extends Packet {

    private String hashedServerID;
    private PublicKey publicKey;
    private byte[] verifyToken;

    public S01EncryptionRequest(String hashedServerID, PublicKey publicKey, byte[] verifyToken) {
        this.hashedServerID = hashedServerID;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    public S01EncryptionRequest() {
    }

    public String getHashedServerID() {
        return hashedServerID;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getVerifyToken() {
        return verifyToken;
    }

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeString(this.hashedServerID);
        buffer.writeByteArray(this.publicKey.getEncoded());
        buffer.writeByteArray(this.verifyToken);
    }

    @Override
    public void read(Buffer buffer) {
        this.hashedServerID = buffer.readString(20);
        this.publicKey = CryptManager.decodePublicKey(buffer.readByteArray());
        this.verifyToken = buffer.readByteArray();
    }

}
