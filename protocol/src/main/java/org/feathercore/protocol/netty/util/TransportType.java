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
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.ThreadFactory;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum TransportType {
    EPOLL(EpollSocketChannel.class, EpollServerSocketChannel.class) {
        @Override
        public boolean isAvailable() {
            return Epoll.isAvailable();
        }

        @Override
        public EventLoopGroup newEventLoopGroup(@NonNull final int threads,
                                                @NonNull final ThreadFactory threadFactory) {
            return new EpollEventLoopGroup(threads, threadFactory);
        }
    },
    KQUEUE(KQueueSocketChannel.class, KQueueServerSocketChannel.class) {
        @Override
        public boolean isAvailable() {
            return KQueue.isAvailable();
        }

        @Override
        public EventLoopGroup newEventLoopGroup(@NonNull final int threads,
                                                @NonNull final ThreadFactory threadFactory) {
            return new KQueueEventLoopGroup(threads, threadFactory);
        }
    },
    NIO(NioSocketChannel.class, NioServerSocketChannel.class) {
        @Override
        public boolean isAvailable() {
            return true;
        }

        @Override
        public EventLoopGroup newEventLoopGroup(@NonNull final int threads,
                                                @NonNull final ThreadFactory threadFactory) {
            return new NioEventLoopGroup(threads, threadFactory);
        }
    };

    private static final TransportType[] TRANSPORT_TYPES = values();

    Class<? extends SocketChannel> socketChannelClass;
    Class<? extends ServerChannel> serverChannelClass;

    public abstract boolean isAvailable();

    public abstract EventLoopGroup newEventLoopGroup(@NonNull int threads, @NonNull ThreadFactory threadFactory);

    public static TransportType getNative() {
        for (val transportType : TRANSPORT_TYPES) {
            if (transportType.isAvailable()) {
                return transportType;
            }
        }

        return getDefault();
    }

    public static TransportType getDefault() {
        return NIO;
    }
}
