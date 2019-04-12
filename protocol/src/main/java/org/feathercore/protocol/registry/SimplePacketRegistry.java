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
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;

import java.util.Collection;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public class SimplePacketRegistry extends AbstractPacketRegistry<Packet> {

    private final PacketType[] types;

    private SimplePacketRegistry(Collection<PacketType<?>> types) {
        this.types = new PacketType[types.stream().mapToInt(PacketType::getId).max().orElse(0)];
        types.forEach(type -> this.types[type.getId()] = type);
    }

    @Override
    public PacketType<? extends Packet> getTypeByID(int id) {
        return id < 0 || id >= this.types.length ? null : this.types[id];
    }

    public static class SimplePacketRegistryBuilder extends Builder<Packet> {

        public SimplePacketRegistryBuilder(@NonNull Collection<PacketType<? extends Packet>> packetTypes, Collection<PacketType<? extends Packet>> packetTypesView) {
            super(packetTypes, packetTypesView);
        }

        protected SimplePacketRegistryBuilder(@NonNull Collection<PacketType<? extends Packet>> packetTypes) {
            super(packetTypes);
        }

        @Override
        public PacketRegistry<Packet> build() {
            return new SimplePacketRegistry(super.packetTypes);
        }
    }

}
