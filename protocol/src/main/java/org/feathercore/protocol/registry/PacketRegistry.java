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
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
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
    @Nullable default P createById(final int id) {
        val type = getTypeById(id);

        return type == null ? null : type.getSupplier().get();
    }

    interface Builder<P extends Packet> {

        /**
         * Adds a packet by its packet type.
         *
         * @param packetType data identifying this packet
         * @return self for chaining
         *
         * @throws NullPointerException if {@code packetSupplier} is {@link null}
         *
         * @apiNote if a packet is already registered by this ID and direction then it should be overridden
         */
        Builder addPacket(@NonNull PacketType<? extends P> packetType);

        /**
         * Removes a packet by its packet type.
         *
         * @param packetType packet type of the packet
         * @return self for chaining
         *
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
         * Builds a new packet registry.
         *
         * @return created packet registry
         */
        PacketRegistry<P> build();
    }
}
