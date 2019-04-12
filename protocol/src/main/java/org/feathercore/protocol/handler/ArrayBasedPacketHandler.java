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
import lombok.val;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class ArrayBasedPacketHandler implements PacketHandler {

    private List<BiConsumer<Connection, Packet>>[] handlers;
    private final Map<Integer, List<BiConsumer<Connection, Packet>>> temporaryMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public void register() {
        this.handlers = new List[this.temporaryMap.keySet().stream().mapToInt(i -> i).max().orElse(0)];
        this.temporaryMap.forEach((id, handlers) -> this.handlers[id] = new ArrayList<>(handlers));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P extends Packet> void addHandler(PacketType<P> type, BiConsumer<Connection, P> handler) {
        if (this.handlers != null) {
            throw new IllegalStateException("ArrayBasedPacketHandler has already been registered: you can't add handlers anymore");
        }
        this.temporaryMap.computeIfAbsent(type.getId(), i -> new ArrayList<>()).add((BiConsumer<Connection, Packet>) handler);
    }

    @Override
    public void handle(@NotNull Connection connection, @NotNull Packet packet) {
        val handlers = this.handlers[packet.getId()];
        if (handlers == null) {
            return;
        }
        for (val handler : handlers) {
            handler.accept(connection, packet);
        }
    }

}
