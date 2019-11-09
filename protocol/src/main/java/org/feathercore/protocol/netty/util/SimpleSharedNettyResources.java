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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.max;

@Builder
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@NonFinal public class SimpleSharedNettyResources implements SharedNettyResources {

    @NonNull final Lock readLock, writeLock;

    {
        val lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    @NonNull final AtomicBoolean shutDown = new AtomicBoolean();

    boolean bossIsWorker;
    @Builder.Default boolean useNativeTransport = true;
    @Builder.Default @Nonnegative int bossThreads = 2, workerThreads = 0;

    @NonFinal @Nullable volatile EventLoopGroup bossLoopGroup, workerLoopGroup;

    @NonNull Supplier<ThreadFactory>
            bossThreadFactorySupplier = () -> ThreadFactories.createPaginated("Netty Boss Thread #", false),
            workerThreadFactorySupplier = () -> ThreadFactories.createPaginated("Netty Worker Thread #", false);

    /**
     * Asserts that this instance is not shut down.
     *
     * @throws IllegalStateException if this instance is already shut down
     */
    protected void assertNotShutDown() {
        if (isShutDown()) {
            throw new IllegalStateException("LoopGroupGracefully is already shut down.");
        }
    }

    @Override
    public boolean isInitialized() {
        assertNotShutDown();

        readLock.lock();
        try {
            return bossLoopGroup != null && workerLoopGroup != null;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isShutDown() {
        return shutDown.get();
    }

    @SneakyThrows(InterruptedException.class)
    protected void performLoopGroupsShutdownGracefully(final boolean await) {
        readLock.lock();
        try {
            if (bossLoopGroup != null || workerLoopGroup != null) {
                writeLock.lock();
                try {
                    var loopGroup = bossLoopGroup;
                    /* Disable boss */
                    if (loopGroup != null) {
                        if (await) {
                            loopGroup.shutdownGracefully().await();
                        } else {
                            loopGroup.shutdownGracefully();
                        }
                        bossLoopGroup = null;
                    }

                    loopGroup = workerLoopGroup;
                    if (loopGroup != null) {
                        /* Disable worker */
                        if (await) {
                            loopGroup.shutdownGracefully().await();
                        } else {
                            loopGroup.shutdownGracefully();
                        }
                        workerLoopGroup = null;
                    }
                } finally {
                    writeLock.unlock();
                }
            }
        } finally {
            readLock.unlock();
        }
    }


    public void shutdownLoopGroupsGracefully(final boolean await) {
        if (shutDown.compareAndSet(false, true)) {
            performLoopGroupsShutdownGracefully(await);
        } else {
            throw new IllegalStateException("LoopGroupGracefully is already shut down.");
        }
    }

    @Override
    public boolean tryShutdownLoopGroupsGracefully(final boolean await) {
        if (shutDown.compareAndSet(false, true)) {
            performLoopGroupsShutdownGracefully(await);

            return true;
        }

        return false;
    }

    public EventLoopGroup getBossLoopGroup() {
        assertNotShutDown();

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
        assertNotShutDown();

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

    /*
     * Extended Lombok-generated builder. Extension is used in order to check preconditions.
     */
    public static class SimpleSharedNettyResourcesBuilder {

        public SimpleSharedNettyResourcesBuilder bossThreads(@Nonnegative final int bossThreads) {
            checkArgument(bossThreads > 0, "There should be at least 1 boss thread");

            this.bossThreads = bossThreads;

            return this;
        }

        public SimpleSharedNettyResourcesBuilder workerThreads(@Nonnegative final int workerThreads) {
            checkArgument(workerThreads <= 0, "Number of worker threads cannot be negative");

            this.workerThreads = workerThreads;

            return this;
        }
    }
}