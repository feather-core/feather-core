package org.feathercore.network.packet.login.server;

import org.feathercore.network.Buffer;
import org.feathercore.network.Packet;
import org.feathercore.util.GameProfile;

import java.util.UUID;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class S02LoginSuccess extends Packet {

    private GameProfile profile;

    public S02LoginSuccess(GameProfile profile) {
        this.profile = profile;
    }

    public S02LoginSuccess() {
    }

    public GameProfile getProfile() {
        return profile;
    }

    @Override
    public int getId() {
        return 0x02;
    }

    @Override
    public void write(Buffer buffer) {
        UUID uuid = this.profile.getUuid();
        buffer.writeString(uuid == null ? "" : uuid.toString());
        buffer.writeString(this.profile.getName());
    }

    @Override
    public void read(Buffer buffer) {
        UUID uuid = UUID.fromString(buffer.readString(36));
        String name = buffer.readString(16);
        this.profile = new GameProfile(uuid, name);
    }
}
