package org.feathercore.network.packet.status.client;

import org.feathercore.network.Buffer;
import org.feathercore.network.Packet;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class C00ServerQuery extends Packet {
    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void write(Buffer buffer) {

    }

    @Override
    public void read(Buffer buffer) {

    }
}
