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
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractPacketRegistry<P extends Packet> implements PacketRegistry<P> {

    @ToString
    @RequiredArgsConstructor
    protected abstract static class Builder<P extends Packet> implements PacketRegistry.Builder<P> {

        @NonNull
        final Collection<PacketType<? extends P>> packetTypes;
        protected final Collection<PacketType<? extends P>> packetTypesView;

        protected Builder(@NonNull final Collection<PacketType<? extends P>> packetTypes) {
            this.packetTypesView = Collections.unmodifiableCollection(this.packetTypes = packetTypes);
        }

        protected Builder() {
            this(new ArrayList<>());
        }

        @Override
        public Builder addPacket(@NonNull final PacketType<? extends P> packetType) {
            packetTypes.removeIf(packetType1 -> !packetType.canCoexist(packetType1));
            packetTypes.add(packetType);

            return this;
        }

        @Override
        public Builder removePacket(final PacketType<? extends P> packetType) {
            packetTypes.remove(packetType);

            return this;
        }

        @Override
        public Builder clearPackets() {
            packetTypes.clear();

            return this;
        }

        @Override
        public Builder removeOnly(@NonNull final Predicate<PacketType<? extends P>> filter) {
            packetTypes.removeAll(packetTypes.stream().filter(filter).collect(Collectors.toList()));

            return this;
        }

        @Override
        public Builder retainOnly(@NonNull final Predicate<PacketType<? extends P>> filter) {
            packetTypes.retainAll(packetTypes.stream().filter(filter).collect(Collectors.toList()));

            return this;
        }

        @Override
        public Collection<@NonNull PacketType<? extends P>> getPackets() {
            return packetTypesView;
        }
    }
}
