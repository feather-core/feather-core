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
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.feathercore.protocol.packet.Packet;

import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract base for a {@link Server implementation}.
 *
 * @param <P> super-type of packets used by this server's protocol
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractServer<P extends Packet> implements Server<P> {

    @NonNull @Getter SocketAddress address;

    @NonNull Object startStopMutex = new Object[0];
    @NonNull @NonFinal volatile boolean running = false;

    protected abstract Future<Void> performStart();

    protected abstract Future<Void> performStop();

    @Override
    public Future<Void> start() {
        synchronized (startStopMutex) {
            if (running) throw new IllegalStateException("This server is already started");

            running = true;

            return performStart();
        }
    }

    @Override
    public Future<Void> stop() {
        synchronized (startStopMutex) {
            if (!running) throw new IllegalStateException("This server is not running");

            running = false;

            return performStop();
        }
    }
}
