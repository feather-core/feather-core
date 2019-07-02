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
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.apache.commons.lang3.RandomUtils;
import org.feathercore.protocol.annotation.PacketFactory;
import org.feathercore.protocol.annotation.PacketId;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PacketTypeTest {

    @Test
    public void testCreateValidations() {
        // test that there should be @PacketFactory
        assertThrows(
                IllegalArgumentException.class, () -> PacketType.create(PacketWithNoFactory.class)
        );
        // test that there should be only one @PacketFactory on static method
        assertThrows(
                IllegalArgumentException.class, () -> PacketType.create(PacketWithMultipleStaticMethodFactories.class)
        );

    }

    @Test
    public void testConstructorFactoryCreate() {
        val packetType = PacketType.create(PacketWithConstructorFactory.class);

        assertThat(packetType.getId(), is(PacketWithConstructorFactory.ID));

        val attempts = RandomUtils.nextInt(8, 16 + 1);
        for (var i = 0; i < attempts; i++) {
            val instanceId = PacketWithConstructorFactory.nextInstanceTestId();
            val packet = packetType.getFactory().get();
            assertThat(packet, notNullValue());

            assertThat(instanceId, is(packet.instanceId));
        }
    }

    @Test
    public void testStaticMethodFactoryCreate() {
        val packetType = PacketType.create(PacketWithStaticMethodFactory.class);

        assertThat(packetType.getId(), is(PacketWithStaticMethodFactory.ID));

        val attempts = RandomUtils.nextInt(8, 16 + 1);
        for (var i = 0; i < attempts; i++) {
            val instanceId = PacketWithStaticMethodFactory.nextInstanceTestId();
            val packet = packetType.getFactory().get();
            assertThat(packet, notNullValue());

            assertThat(instanceId, is(packet.instanceId));
        }
    }

    @Test
    public void testSupplierFactoryCreate() {
        val instanceIdCounter = new AtomicInteger();
        val packetType = PacketType.create(
                PacketWithNoFactory.class, () -> new PacketWithNoFactory(instanceIdCounter.incrementAndGet())
        );

        assertThat(packetType.getId(), is(PacketWithNoFactory.ID));

        val attempts = RandomUtils.nextInt(8, 16 + 1);
        for (var i = 0; i < attempts; i++) {
            val packet = packetType.getFactory().get();

            assertThat(packet, notNullValue());
            assertThat(packet.instanceId, is(instanceIdCounter.intValue()));
        }
    }

    @RequiredArgsConstructor
    @PacketId(PacketWithNoFactory.ID)
    private static final class PacketWithNoFactory implements Packet {

        private static final int ID = 0x10;

        private final int instanceId;

        @Override
        public int getId() {
            return ID;
        }
    }

    @PacketId(PacketWithMultipleStaticMethodFactories.ID)
    private static final class PacketWithMultipleStaticMethodFactories implements Packet {

        private static final int ID = 0x20;

        @Override
        public int getId() {
            return ID;
        }

        @PacketFactory
        public static PacketWithMultipleStaticMethodFactories create1() {
            return new PacketWithMultipleStaticMethodFactories();
        }

        @PacketFactory
        public static PacketWithMultipleStaticMethodFactories create2() {
            return new PacketWithMultipleStaticMethodFactories();
        }
    }

    @PacketId(PacketWithConstructorFactory.ID)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PacketWithConstructorFactory implements Packet {

        private static final ThreadLocal<AtomicInteger> PACKET_TEST_ID_COUNTER = new ThreadLocal<>();
        private static final int ID = 0x50;

        private final int instanceId;

        private static AtomicInteger instanceIdCounter() {
            var counter = PACKET_TEST_ID_COUNTER.get();
            if (counter == null) {
                counter = new AtomicInteger();
                PACKET_TEST_ID_COUNTER.set(counter);
            }

            return counter;
        }

        public static int nextInstanceTestId() {
            return instanceIdCounter().intValue();
        }

        @PacketFactory
        public PacketWithConstructorFactory() {
            instanceId = instanceIdCounter().getAndIncrement();
        }

        @Override
        public int getId() {
            return ID;
        }
    }

    @PacketId(PacketWithStaticMethodFactory.ID)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PacketWithStaticMethodFactory implements Packet {

        private static final ThreadLocal<AtomicInteger> PACKET_TEST_ID_COUNTER = new ThreadLocal<>();
        private static final int ID = 0x60;

        private final int instanceId;

        private static AtomicInteger testIdCounter() {
            var counter = PACKET_TEST_ID_COUNTER.get();
            if (counter == null) {
                counter = new AtomicInteger();
                PACKET_TEST_ID_COUNTER.set(counter);
            }

            return counter;
        }

        public static int nextInstanceTestId() {
            return testIdCounter().intValue();
        }

        @PacketFactory
        public static PacketWithStaticMethodFactory create() {
            return new PacketWithStaticMethodFactory(testIdCounter().getAndIncrement());
        }

        @Override
        public int getId() {
            return ID;
        }
    }
}