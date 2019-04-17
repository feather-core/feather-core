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

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractPacketRegistry<P extends Packet> implements PacketRegistry<P> {

    @ToString
    @FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
    protected abstract static class Builder<P extends Packet> implements PacketRegistry.Builder<P> {
        @NonNull Collection<PacketType<? extends P>> packetTypes;
        @NonNull Collection<PacketType<? extends P>> packetTypesView;
        @Nullable @NonFinal Consumer<Connection> attachListener;
        @Nullable @NonFinal Consumer<Connection> detachListener;
        @NonNull Map<Integer, BiConsumer<Connection, Packet>> packetHandlers;

        protected Builder(@NonNull final Collection<PacketType<? extends P>> packetTypes) {
            this.packetTypesView = Collections.unmodifiableCollection(this.packetTypes = packetTypes);
            this.packetHandlers = new HashMap<>();
        }

        protected Builder() {
            this(new ArrayList<>());
        }

        @Override
        public <T extends P> PacketRegistry.Builder addPacket(@NonNull final PacketType<T> packetType,
                                                              @Nullable final BiConsumer<Connection, T> handler) {
            packetTypes.removeIf(type -> type.getId() == packetType.getId());
            packetHandlers.remove(packetType.getId());
            packetTypes.add(packetType);

            return this;
        }

        @Override
        public Builder removePacket(final PacketType<? extends P> packetType) {
            packetTypes.remove(packetType);
            packetHandlers.remove(packetType.getId());

            return this;
        }

        @Override
        public Builder clearPackets() {
            packetTypes.clear();
            packetHandlers.clear();

            return this;
        }

        @Override
        public Builder removeOnly(@NonNull final Predicate<PacketType<? extends P>> filter) {
            new HashSet<>(packetTypes).stream().filter(filter).forEach(type -> {
                packetTypes.remove(type);
                packetHandlers.remove(type.getId());
            });

            return this;
        }

        @Override
        public Builder retainOnly(@NonNull final Predicate<PacketType<? extends P>> filter) {
            return removeOnly(filter.negate());
        }

        @Override
        public PacketRegistry.Builder attachListener(@Nullable final Consumer<Connection> listener) {
            this.attachListener = listener;
            return this;
        }

        @Override
        public PacketRegistry.Builder detachListener(@Nullable final Consumer<Connection> listener) {
            this.detachListener = listener;
            return this;
        }

        @Override
        public Collection<@NonNull PacketType<? extends P>> getPackets() {
            return packetTypesView;
        }

    }
}
