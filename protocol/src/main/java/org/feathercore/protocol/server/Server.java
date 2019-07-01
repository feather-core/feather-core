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

import lombok.NonNull;
import org.feathercore.protocol.Connection;
import org.feathercore.protocol.packet.Packet;
import org.feathercore.protocol.registry.PacketRegistry;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.concurrent.Future;

/**
 * Base for IO-server based on {@link PacketRegistry}.
 *
 * @param <P> super-type of packets used by this server's protocol
 */
public interface Server<P extends Packet> {

    SocketAddress getAddress();

    Future<Void> start();

    Future<Void> stop();

    @NonNull PacketRegistry<P> getPacketRegistry();

    void handleConnect(@NotNull final Connection connection);

    void handleDisconnect(@NotNull final Connection connection);
}
