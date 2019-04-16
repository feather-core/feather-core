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
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.function.Supplier;

/**
 * A value-class containing all data identifying packet type.
 *
 * @param <P> type of packet
 */
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@NonFinal
public class PacketType<P extends Packet> {

    int id;
    @NonNull Class<P> type;
    @NonNull Supplier<P> supplier;

    private PacketType(Class<P> type, Supplier<P> supplier) {
        try {
            this.id = type.getField("ID").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(
                    "Could not instantiate packet type, because there is not ID field in given packet class or it is "
                            + "not accessible",
                    e
            );
        }
        this.type = type;
        this.supplier = supplier;
    }

    public static <P extends Packet> PacketType<P> create(@NonNull final Class<P> type,
                                                             @NonNull final Supplier<P> supplier) {
        return new PacketType<>(type, supplier);
    }

}
