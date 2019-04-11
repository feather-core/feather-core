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

package org.feathercore.protocol.packet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.function.Supplier;

/**
 * A value-class containing all data identifying packet type.
 *
 * @param <P> type of packet
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@NonFinal public abstract class PacketType<P extends Packet> {

    @NonNull Direction direction;
    int id;
    @NonNull Class<P> type;
    @NonNull Supplier<P> supplier;

    public abstract boolean canCoexist(@NonNull final PacketType packetType);

    public static <P extends Packet> PacketType<P> incoming(@NonNull final int id, @NonNull final Class<P> type,
                                                            @NonNull final Supplier<P> supplier) {
        return new PacketType<P>(Direction.INCOMING, id, type, supplier) {
            @Override
            public boolean canCoexist(final @NonNull PacketType packetType) {
                return packetType.getDirection() == Direction.OUTCOMING || packetType.getId() != id;
            }
        };
    }

    public static <P extends Packet> PacketType<P> outcoming(@NonNull final int id, @NonNull final Class<P> type,
                                                             @NonNull final Supplier<P> supplier) {
        return new PacketType<P>(Direction.OUTCOMING, id, type, supplier) {
            @Override
            public boolean canCoexist(final @NonNull PacketType packetType) {
                return packetType.getDirection() == Direction.INCOMING || packetType.getType() != type;
            }
        };
    }

    public enum Direction {
        INCOMING, OUTCOMING
    }
}
