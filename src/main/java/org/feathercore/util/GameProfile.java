package org.feathercore.util;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class GameProfile {

    private final UUID uuid;
    private final String name;
    private boolean legacy;

    public GameProfile(UUID uuid, String name) {
        if (uuid == null && (name == null || name.trim().isEmpty())) {
            throw new IllegalArgumentException("Name and UUID can't both be blank");
        }
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public boolean isLegacy() {
        return this.legacy;
    }

    public boolean isComplete() {
        return this.uuid != null && this.name != null && !this.name.trim().isEmpty();
    }

    @Override
    public int hashCode() {
        int result = this.uuid != null ? this.uuid.hashCode() : 0;
        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameProfile that = (GameProfile) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(name, that.name);
    }

}
