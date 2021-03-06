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

package org.feathercore.protocol.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.registry.PacketRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;

/**
 * Created by k.shandurenko on 16/04/2019
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseServer {

    @NonNull protected final String host;
    protected final int port;

    public abstract Future<Void> start();

    public abstract Future<Void> stop();

    public abstract void onConnected(@NotNull Connection connection);

    public abstract void onDisconnected(@NotNull Connection connection);

    public abstract @NonNull PacketRegistry<? extends Packet> getInitialPacketRegistry();

}
