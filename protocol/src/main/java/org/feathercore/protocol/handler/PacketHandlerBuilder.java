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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PacketHandlerBuilder<H extends PacketHandler> {

    public static PacketHandlerTypeBuilder newBuilder() {
        return new PacketHandlerTypeBuilder();
    }

    private final H packetHandler;

    public <P extends Packet> PacketHandlerBuilder<H> addHandler(@NonNull PacketType<P> type,
                                                                 @NonNull BiConsumer<Connection, P> handler) {
        this.packetHandler.addHandler(type, handler);
        return this;
    }

    public <P extends Packet> PacketHandlerBuilder<H> addHandler(@NonNull PacketType<P> type,
                                                                 @NonNull Consumer<P> handler) {
        return addHandler(type, (connection, packet) -> handler.accept(packet));
    }

    public H build() {
        this.packetHandler.register();
        return this.packetHandler;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PacketHandlerTypeBuilder {

        private Consumer<@NonNull Connection> onConnected;
        private Consumer<@NonNull Connection> onDisconnected;

        public PacketHandlerTypeBuilder onConnected(Consumer<@NonNull Connection> consumer) {
            this.onConnected = consumer;
            return this;
        }

        public PacketHandlerTypeBuilder onDisconnected(Consumer<@NonNull Connection> consumer) {
            this.onDisconnected = consumer;
            return this;
        }

        public PacketHandlerBuilder<ArrayBasedPacketHandler> buildArrayBased() {
            return new PacketHandlerBuilder<>(new ArrayBasedPacketHandler() {
                @Override
                public void onConnected(@NonNull Connection connection) {
                    if (onConnected != null) {
                        onConnected.accept(connection);
                    }
                }

                @Override
                public void onDisconnected(@NonNull Connection connection) {
                    if (onDisconnected != null) {
                        onDisconnected.accept(connection);
                    }
                }
            });
        }

    }

}
