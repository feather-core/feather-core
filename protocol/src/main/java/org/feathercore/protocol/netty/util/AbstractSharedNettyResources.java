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
import io.netty.util.concurrent.Future;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractSharedNettyResources implements SharedNettyResources {

    @NonNull @Getter TransportType transportType;
    boolean bossIsWorker;

    @NonNull final Lock readLock, writeLock;
    @NonNull final AtomicBoolean shutDown;

    @NonFinal @Nullable volatile EventLoopGroup bossLoopGroup;
    @NonFinal @Nullable volatile EventLoopGroup workerLoopGroup;

    public AbstractSharedNettyResources(@NonNull final TransportType transportType, final boolean bossIsWorker) {
        this.transportType = transportType;
        this.bossIsWorker = bossIsWorker;

        shutDown = new AtomicBoolean();
        {
            val lock = new ReentrantReadWriteLock();
            readLock = lock.readLock();
            writeLock = lock.writeLock();
        }
    }

    /* *********************************************** Stateful data *********************************************** */

    protected abstract EventLoopGroup newBossLoopGroup();

    protected abstract EventLoopGroup newWorkerLoopGroup();

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
                    // used not to duplicate GETFIELD invocations and to omit potential null-checks
                    var loopGroup = bossLoopGroup;
                    @Nullable final Future<?> bossShutDown;
                    /* Shutdown boss */
                    if (loopGroup != null) {
                        bossShutDown = loopGroup.shutdownGracefully();
                        bossLoopGroup = null;
                    } else {
                        bossShutDown = null;
                    }

                    loopGroup = workerLoopGroup;
                    @Nullable final Future<?> workerShutDown;
                    if (loopGroup != null) {
                        /* Shutdown worker */
                        workerShutDown = loopGroup.shutdownGracefully().await();
                        workerLoopGroup = null;
                    } else {
                        workerShutDown = null;
                    }

                    if (await) {
                        if (bossShutDown != null) {
                            bossShutDown.await();
                        }
                        if (workerShutDown != null) {
                            workerShutDown.await();
                        }
                    }
                } finally {
                    writeLock.unlock();
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
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

    @Override
    @NotNull public EventLoopGroup getBossLoopGroup() {
        assertNotShutDown();

        readLock.lock();
        try {
            val loopGroup = bossLoopGroup;
            if (loopGroup == null) {
                writeLock.lock();
                try {
                    return bossLoopGroup = newWorkerLoopGroup();
                } finally {
                    writeLock.unlock();
                }
            }

            return loopGroup;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    @NotNull public EventLoopGroup getWorkerLoopGroup() {
        assertNotShutDown();

        readLock.lock();
        try {
            val loopGroup = workerLoopGroup;
            if (loopGroup == null) {
                writeLock.lock();
                try {
                    return workerLoopGroup = newWorkerLoopGroup();
                } finally {
                    writeLock.unlock();
                }
            }

            return loopGroup;
        } finally {
            readLock.unlock();
        }
    }
}
