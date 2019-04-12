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

package org.feathercore.protocol.packet.handshake.client;

import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.packet.ConnectionState;
import org.feathercore.protocol.packet.Packet;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class C00Handshake implements Packet {

    public final static int ID = 0x00;

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

    @Override
    public int getID() {
        return ID;
    }

}
