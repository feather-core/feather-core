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

package org.feathercore.protocol.netty.util;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.SocketChannel;

/**
 * Holder of shared data used by Netty's basic elements.
 */
public interface SharedNettyResources {

    boolean isInitialized();

    boolean isShutDown();

    void shutdownLoopGroupsGracefully(final boolean await);

    default void shutdownLoopGroupsGracefully() {
        shutdownLoopGroupsGracefully(true);
    }

    boolean tryShutdownLoopGroupsGracefully(final boolean await);

    default boolean tryShutdownLoopGroupsGracefully() {
        return tryShutdownLoopGroupsGracefully(true);
    }

    EventLoopGroup getBossLoopGroup();

    EventLoopGroup getWorkerLoopGroup();

    Class<? extends SocketChannel> getSocketChannelClass();

    Class<? extends ServerChannel> getServerChannelClass();
}
