package org.feathercore.network.packet.handshake.client;

import org.feathercore.network.Buffer;
import org.feathercore.network.Packet;
import org.feathercore.network.packet.ConnectionState;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class C00Handshake extends Packet {

    private int protocolVersion;
    private String ip;
    private int port;
    private ConnectionState requestedState;

    public C00Handshake(int protocolVersion, String ip, int port, ConnectionState requestedState) {
        this.protocolVersion = protocolVersion;
        this.ip = ip;
        this.port = port;
        this.requestedState = requestedState;
    }

    public C00Handshake() {
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public ConnectionState getRequestedState() {
        return requestedState;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeVarInt(this.protocolVersion);
        buffer.writeString(this.ip);
        buffer.writeShort((short) this.port);
        buffer.writeVarInt(this.requestedState.getId());
    }

    @Override
    public void read(Buffer buffer) {
        this.protocolVersion = buffer.readVarInt();
        this.ip = buffer.readString();
        this.port = buffer.readShort();
        this.requestedState = ConnectionState.getByID(buffer.readVarInt());
    }

}
