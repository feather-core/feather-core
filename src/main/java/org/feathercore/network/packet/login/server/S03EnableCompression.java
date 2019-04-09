package org.feathercore.network.packet.login.server;

import org.feathercore.network.Buffer;
import org.feathercore.network.Packet;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class S03EnableCompression extends Packet {

    private int compressionThreshold;

    public S03EnableCompression(int compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
    }

    public S03EnableCompression() {
    }

    public int getCompressionThreshold() {
        return compressionThreshold;
    }

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(this.compressionThreshold);
    }

    @Override
    public void read(Buffer buffer) {
        this.compressionThreshold = buffer.readVarInt();
    }

}
