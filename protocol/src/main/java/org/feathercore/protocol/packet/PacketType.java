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
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.val;
import org.feathercore.protocol.annotation.PacketFactory;
import org.feathercore.protocol.annotation.PacketId;
import ru.progrm_jarvis.reflector.invoke.InvokeUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A value-class containing all data identifying packet type.
 *
 * @param <P> type of packet
 */
@NonFinal
@Value(staticConstructor = "create")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PacketType<P extends Packet> {

    /**
     * ID of the packet
     */
    int id;
    /**
     * Factory used for instantiation of packets
     */
    @NonNull Supplier<P> factory;

    /**
     * Creates a packet type from the given class using its meta-structure.
     * <p>
     * This will look for constructors and methods annotated with {@link PacketFactory} which will be used as factories
     * for the packets of given IDs.
     *
     * @param type class which should be analyzed to create packet type from it
     * @param <P> type of the packet
     * @return created packet type
     *
     * @throws IllegalArgumentException if the given class contains multiple methods annotated with {@link PacketFactory}
     */
    public static <P extends Packet> PacketType<P> create(@NonNull final Class<P> type) {
        {
            @SuppressWarnings("unchecked") val optionalConstructor
                    = lookupPacketFactory(Arrays.stream((Constructor<P>[]) type.getDeclaredConstructors()));
            if (optionalConstructor.isPresent()) {
                val constructor = optionalConstructor.get();

                return create(type, InvokeUtil.toSupplier(constructor));
            }
        }
        {
            val optionalMethod = lookupPacketFactory(
                    Arrays.stream(type.getDeclaredMethods())
                          .filter(method -> Modifier.isStatic(method.getModifiers()))
            );
            if (optionalMethod.isPresent()) {
                val method = optionalMethod.get();

                return create(type, InvokeUtil.toStaticSupplier(method));
            }
        }

        throw new IllegalArgumentException(
                "There should be a no-args method or constructor annotated with @PacketFactory"
        );
    }

    /**
     * Creates a packet-type for the given class or its sub-type using the given factory.
     * <p>
     * This will try to use {@link PacketFactory} annotation on this class
     * and {@link PacketId} on its static field or method to get the ID of the packet.
     *
     * @param type type of the packet whose type is being created
     * @param factory factory to be used for instantiation of packets
     * @param <P> exact type of packet
     * @return created packet-type
     */
    public static <P extends Packet> PacketType<P> create(@NonNull final Class<P> type,
                                                          @NonNull final Supplier<P> factory) {
        val packetIdAnnotation = type.getAnnotation(PacketId.class);
        checkArgument(packetIdAnnotation != null, "Type should be annotated with @PacketId");

        return create(packetIdAnnotation.value(), factory);
    }

    /**
     * Finds an executable which may be used as a packet-type factory.
     *
     * @param possibleFactories array of elements some of which which may be packet factories
     * @param <T> exact type of passed executable
     * @return {@link Optional} containing found factory (if there was only one found) or
     * an empty one if there were no possible packet-factories in the given array
     *
     * @throws IllegalArgumentException if there is more than one possible packet-factory in the given array
     */
    private static <T extends Executable> Optional<T> lookupPacketFactory(@NonNull final Stream<T> possibleFactories) {
        val factories = possibleFactories
                .filter(possibleFactory -> possibleFactory.getParameterCount() == 0)
                .filter(possibleFactory -> possibleFactory.isAnnotationPresent(PacketFactory.class))
                .collect(Collectors.toList());

        if (factories.isEmpty()) return Optional.empty();
        checkArgument(
                factories.size() == 1,
                "There should only be one no-args method or constructor annotated with @PacketFactory"
        );

        return Optional.of(factories.get(0));
    }
}
