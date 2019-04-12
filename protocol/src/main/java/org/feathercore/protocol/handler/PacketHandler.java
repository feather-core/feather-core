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
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.packet.PacketType;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Created by k.shandurenko on 12/04/2019
 */
public interface PacketHandler {

    void register();

    <P extends Packet> void addHandler(@NonNull PacketType<P> type, @NonNull BiConsumer<Connection, P> handler);

    void handle(@NotNull Connection connection, @NotNull Packet packet);

    void onConnected(@NonNull Connection connection);

    void onDisconnected(@NonNull Connection connection);

}
