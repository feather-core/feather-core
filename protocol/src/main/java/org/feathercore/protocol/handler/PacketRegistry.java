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

package org.feathercore.protocol.handler;

import lombok.NonNull;
import org.feathercore.protocol.Buffer;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Object responsible for storing all registered packets.
 */
public interface PacketRegistry<P extends Packet> {

    P read(@NotNull Buffer buffer);

    void write(@NotNull P packet, @NotNull Buffer buffer);

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
