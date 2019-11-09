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
import org.feathercore.shared.util.thread.ThreadFactories;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnegative;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.max;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@Value
@Builder
@FieldDefaults(level = AccessLevel.PROTECTED)
@NonFinal
public class SharedNettyResources {

    @NonNull protected final ReadWriteLock lock;

    boolean bossIsWorker;
    @Builder.Default boolean useNativeTransport = true;
    @Builder.Default @Nonnegative int bossThreads = 2, workerThreads = 0;

    @NonFinal @Nullable volatile EventLoopGroup bossLoopGroup, workerLoopGroup;

    @NonNull Supplier<ThreadFactory>
            bossThreadFactorySupplier = () -> ThreadFactories.createPaginated("Netty Boss Thread #", false),
            workerThreadFactorySupplier = () -> ThreadFactories.createPaginated("Netty Worker Thread #", false);

    public boolean isInitialized() {
        lock.readLock().lock();
        try {
            return bossLoopGroup != null && workerLoopGroup != null;
        } finally {
            lock.readLock().unlock();
        }
    }

    @SneakyThrows(InterruptedException.class)
    public void shutdownLoopGroupsGracefully(final boolean await) {
        lock.readLock().lock();
        try {
            if (bossLoopGroup != null) {
                lock.writeLock().lock();
                try {
                    if (await) {
                        bossLoopGroup.shutdownGracefully().await();
                    } else {
                        bossLoopGroup.shutdownGracefully();
                    }

                    bossLoopGroup = null;
                } finally {
                    lock.writeLock().unlock();
                }
            }
            if (workerLoopGroup != null) {
                lock.writeLock().lock();
                try {
                    if (await) {
                        workerLoopGroup.shutdownGracefully().await();
                    }
                    workerLoopGroup.shutdownGracefully();

                    workerLoopGroup = null;
                } finally {
                    lock.writeLock().unlock();
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void shutdownLoopGroupsGracefully() {
        shutdownLoopGroupsGracefully(false);
    }

    public EventLoopGroup getBossLoopGroup() {
        lock.readLock().lock();
        try {
            if (bossLoopGroup == null) {
                lock.writeLock().lock();
                try {
                    bossLoopGroup = (useNativeTransport ? TransportType.getNative() : TransportType.getDefault())
                            .newEventLoopGroup(bossThreads, bossThreadFactorySupplier.get());
                } finally {
                    lock.writeLock().unlock();
                }
            }

            return bossLoopGroup;
        } finally {
            lock.readLock().unlock();
        }
    }

    public EventLoopGroup getWorkerLoopGroup() {
        lock.readLock().lock();
        try {
            if (workerLoopGroup == null) {
                lock.writeLock().lock();
                try {
                    if (bossIsWorker) {
                        workerLoopGroup = getWorkerLoopGroup();
                    } else {
                        workerLoopGroup = (useNativeTransport ? TransportType.getNative() : TransportType.getDefault())
                                .newEventLoopGroup(max(1, workerThreads), workerThreadFactorySupplier.get());
                    }
                } finally {
                    lock.writeLock().unlock();
                }
            }

            return workerLoopGroup;
        } finally {
            lock.readLock().unlock();
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

        protected SharedNettyResourcesBuilder lock(@NonNull final ReadWriteLock lock) {
            this.lock = lock;

            return this;
        }
    }
}