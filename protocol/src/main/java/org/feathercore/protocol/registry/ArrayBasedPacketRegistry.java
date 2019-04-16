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
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@ToString
public class ArrayBasedPacketRegistry<P extends Packet> extends AbstractPacketRegistry<P> {

    private final PacketType[] types;
    private final BiConsumer<Connection, Packet>[] handlers;

    protected ArrayBasedPacketRegistry(@NonNull final Collection<PacketType<? extends P>> types,
                                     Map<Integer, BiConsumer<Connection, Packet>> handlers) {
        this.types = new PacketType[types
                .stream()
                .mapToInt(PacketType::getId)
                .max()
                .orElse(0)
                ];

        for (val type : types) {
            this.types[type.getId()] = type;
        }

        //noinspection unchecked
        this.handlers = new BiConsumer[handlers.keySet().stream().mapToInt(i -> i).max().orElse(0)];
        handlers.forEach((id, handler) -> this.handlers[id] = handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PacketType<? extends P> getTypeById(int id) {
        return id < 0 || id >= this.types.length ? null : this.types[id];
    }

    @Override
    public void handlePacket(@NotNull final Connection connection, @NotNull final Packet packet) {
        val handler = this.handlers[packet.getId()];
        if (handler != null) {
            handler.accept(connection, packet);
        }
    }

    public static class Builder<P extends Packet> extends AbstractPacketRegistry.Builder<P> {

        public static <P extends Packet> Builder create(@NonNull final Collection<PacketType<? extends P>> packetTypes) {
            return new Builder<>(packetTypes);
        }

        public static Builder create() {
            return new Builder<>();
        }

        protected Builder(@NonNull final Collection<PacketType<? extends P>> packetTypes) {
            super(packetTypes);
        }

        protected Builder() {}

        @Override
        public PacketRegistry<P> build() {
            return new ArrayBasedPacketRegistry<>(super.packetTypes, super.packetHandlers);
        }
    }

}