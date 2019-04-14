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
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.feathercore.shared.util.NamedThreadFactory;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnegative;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.max;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@Value
@Builder
@FieldDefaults(level = AccessLevel.PROTECTED)
@NonFinal public class SharedNettyResources {

    @NonNull private final Lock readLock, writeLock;

    {
        val lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    boolean bossIsWorker;
    @Builder.Default boolean useNativeTransport = true;
    @Builder.Default @Nonnegative int bossThreads = 2, workerThreads = 0;

    @NonFinal @Nullable volatile EventLoopGroup bossLoopGroup, workerLoopGroup;

    @NonNull Supplier<ThreadFactory>
            bossThreadFactorySupplier = () -> new NamedThreadFactory("Netty Boss Thread #", false),
            workerThreadFactorySupplier = () -> new NamedThreadFactory("Netty Worker Thread #", false);

    public boolean isInitialized() {
        readLock.lock();
        try {
            return bossLoopGroup != null && workerLoopGroup != null;
        } finally {
            readLock.unlock();
        }
    }

    @SneakyThrows(InterruptedException.class)
    public void shutdownLoopGroupsGracefully(final boolean await) {
        readLock.lock();
        try {
            if (bossLoopGroup != null) {
                writeLock.lock();
                try {
                    if (await) bossLoopGroup.shutdownGracefully().await();
                    else bossLoopGroup.shutdownGracefully();

                    bossLoopGroup = null;
                } finally {
                    writeLock.unlock();
                }
            }
            if (workerLoopGroup != null) {
                writeLock.lock();
                try {
                    if (await) workerLoopGroup.shutdownGracefully().await();
                    workerLoopGroup.shutdownGracefully();

                    workerLoopGroup = null;
                } finally {
                    writeLock.unlock();
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    public void shutdownLoopGroupsGracefully() {
        shutdownLoopGroupsGracefully(false);
    }

    public EventLoopGroup getBossLoopGroup() {
        readLock.lock();
        try {
            if (bossLoopGroup == null) {
                writeLock.lock();
                try {
                    bossLoopGroup = (useNativeTransport ? TransportType.getNative() : TransportType.getDefault())
                            .newEventLoopGroup(bossThreads, bossThreadFactorySupplier.get());
                } finally {
                    writeLock.unlock();
                }
            }

            return bossLoopGroup;
        } finally {
            readLock.unlock();
        }
    }

    public EventLoopGroup getWorkerLoopGroup() {
        readLock.lock();
        try {
            if (workerLoopGroup == null) {
                writeLock.lock();
                try {
                    if (bossIsWorker) {
                        workerLoopGroup = getWorkerLoopGroup();
                    } else {
                        workerLoopGroup = (useNativeTransport ? TransportType.getNative() : TransportType.getDefault())
                                .newEventLoopGroup(max(1, workerThreads), workerThreadFactorySupplier.get());
                    }
                } finally {
                    writeLock.unlock();
                }
            }

            return workerLoopGroup;
        } finally {
            readLock.unlock();
        }
    }

    public Class<? extends SocketChannel> getSocketChannelClass() {
        return (useNativeTransport ? TransportType.getNative() : TransportType.getDefault()).getSocketChannelClass();
    }

    public Class<? extends ServerChannel> getServerChannelClass() {
        return (useNativeTransport ? TransportType.getNative() : TransportType.getDefault()).getServerChannelClass();
    }

    public static class SharedNettyResourcesBuilder {

        public SharedNettyResourcesBuilder bossThreads(@Nonnegative final int bossThreads) {
            checkArgument(bossThreads > 0, "There should be at least 1 boss thread");

            this.bossThreads = bossThreads;

            return this;
        }

        public SharedNettyResourcesBuilder workerThreads(@Nonnegative final int workerThreads) {
            checkArgument(workerThreads <= 0, "Number of worker threads cannot be negative");

            this.workerThreads = workerThreads;

            return this;
        }

        protected SharedNettyResourcesBuilder readLock(@NonNull final Lock readLock) {
            this.readLock = readLock;

            return this;
        }

        protected SharedNettyResourcesBuilder writeLock(@NonNull final Lock writeLock) {
            this.writeLock = writeLock;

            return this;
        }
    }
}