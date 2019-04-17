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
import lombok.val;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Object responsible for storing all registered packets.
 * Implementations of the registry are commonly used for inbound packets decoding
 * when an instance should be allocated by the ID and the exact type of the object depends on the very ID.
 */
public interface PacketRegistry<P extends Packet> {

    /**
     * Gets packet type of given ID.
     *
     * @param id identifier of the packet type.
     * @return packet type of the specified packet or {@code null} if none is registered by this ID.
     */
    @Nullable PacketType<? extends P> getTypeById(final int id);

    /**
     * Creates empty packet by it's ID.
     *
     * @param id identifier of the packet.
     * @return instance of a packet by the specified ID or {@code null} if there is no packet type registered by this ID
     */
    @Nullable
    default P createById(final int id) {
        val type = getTypeById(id);

        return type == null ? null : type.getSupplier().get();
    }

    void handlePacket(@NotNull Connection connection, @NotNull Packet packet);

    void registryAttached(@NonNull Connection connection);

    interface Builder<P extends Packet> {

        /**
         * Adds a packet by its packet type without any handler.
         *
         * @param packetType data identifying this packet
         * @return self for chaining
         * @apiNote if a packet is already registered by this ID and direction then it should be overridden
         */
        default <T extends P> Builder addPacket(@NonNull PacketType<T> packetType) {
            return addPacket(packetType, (BiConsumer<Connection, T>) null);
        }

        /**
         * Adds a packet by its packet type with specified handler.
         *
         * @param packetType data identifying this packet
         * @param handler action that will be executed when packet arrives.
         * @return self for chaining
         * @apiNote if a packet is already registered by this ID and direction then it should be overridden
         */
        default <T extends P> Builder addPacket(@NonNull PacketType<T> packetType, @Nullable Consumer<T> handler) {
            //noinspection ConstantConditions
            return addPacket(packetType, (connection, packet) -> handler.accept(packet));
        }

        /**
         * Adds a packet by its packet type with specified handler.
         *
         * @param packetType data identifying this packet
         * @param handler action that will be executed when packet arrives.
         * @return self for chaining
         * @apiNote if a packet is already registered by this ID and direction then it should be overridden
         */
        <T extends P> Builder addPacket(@NonNull PacketType<T> packetType, @Nullable BiConsumer<Connection, T> handler);

        /**
         * Removes a packet by its packet type.
         *
         * @param packetType packet type of the packet
         * @return self for chaining
         * @apiNote if a packet is not registered by this ID then nothing should happen
         */
        Builder removePacket(PacketType<? extends P> packetType);

        /**
         * Removes all the packets added.
         *
         * @return self for chaining
         */
        Builder clearPackets();

        /**
         * Removes all the packet types matching the filter.
         *
         * @param filter filter to select packet types for removal
         * @return self for chaining
         */
        Builder removeOnly(@NonNull Predicate<PacketType<? extends P>> filter);

        /**
         * Keeps only the packet types matching the filter removing all others.
         *
         * @param filter filter to select packet types to keep
         * @return self for chaining
         */
        Builder retainOnly(@NonNull Predicate<PacketType<? extends P>> filter);

        /**
         * Gets all packets added.
         *
         * @return packet types added
         */
        Collection<@NonNull PacketType<? extends P>> getPackets();

        /**
         * Sets registry attach listener
         *
         * @param listener attach listener
         * @return self for chaining
         */
        Builder attachListener(@Nullable Consumer<Connection> listener);

        /**
         * Sets registry detach listener
         *
         * @param listener detach listener
         * @return self for chaining
         */
        Builder detachListener(@Nullable Consumer<Connection> listener);

        /**
         * Builds a new packet registry.
         *
         * @return created packet registry
         */
        PacketRegistry<P> build();
    }
}
