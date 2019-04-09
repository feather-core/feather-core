package org.feathercore.network.packet.login.client;

import org.feathercore.network.Buffer;
import org.feathercore.network.Packet;
import org.feathercore.util.GameProfile;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class C00LoginStart extends Packet {

    private GameProfile profile;

    public C00LoginStart(GameProfile profile) {
        this.profile = profile;
    }

    public C00LoginStart() {
    }

    public GameProfile getProfile() {
        return profile;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public void write(Buffer buffer) {
        buffer.writeString(this.profile.getName());
    }

    @Override
    public void read(Buffer buffer) {
        this.profile = new GameProfile(null, buffer.readString(16));
    }

}
