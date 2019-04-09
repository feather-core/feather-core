package org.feathercore.network.packet.status.client;

import org.feathercore.network.Buffer;
import org.feathercore.network.Packet;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class C01Ping extends Packet {

    private long clientTime;

    public C01Ping(long clientTime) {
        this.clientTime = clientTime;
    }

    public C01Ping() {
    }

    public long getClientTime() {
        return clientTime;
    }

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeLong(this.clientTime);
    }

    @Override
    public void read(Buffer buffer) {
        this.clientTime = buffer.readLong();
    }

}
