package org.feathercore.network.packet;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public enum ConnectionState {
    HANDSHAKING(-1),
    PLAY(0),
    STATUS(1),
    LOGIN(2);

    private final static ConnectionState[] VALUES = values();

    public static ConnectionState getByID(int id) {
        for (ConnectionState state : VALUES) {
            if (state.id == id) {
                return state;
            }
        }
        return null;
    }

    private final int id;

    ConnectionState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
