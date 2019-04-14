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

package org.feathercore.protocol.registry;

import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;

import java.util.Collection;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@ToString
public class SimplePacketRegistry<P extends Packet> extends AbstractPacketRegistry<P> {

    private final PacketType[] types;

    private SimplePacketRegistry(@NonNull final Collection<PacketType<? extends P>> types) {
        this.types = new PacketType[types
                .stream()
                .mapToInt(PacketType::getId)
                .max()
                .orElse(0)
                ];

        for (val type : types) this.types[type.getId()] = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PacketType<? extends P> getTypeById(int id) {
        return id < 0 || id >= this.types.length ? null : this.types[id];
    }

    public static class Builder<P extends Packet> extends AbstractPacketRegistry.Builder<P> {

        protected Builder(@NonNull final Collection<PacketType<? extends P>> packetTypes) {
            super(packetTypes);
        }

        @Override
        public PacketRegistry<P> build() {
            return new SimplePacketRegistry<>(super.packetTypes);
        }
    }

}
