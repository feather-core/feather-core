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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.feathercore.shared.util.thread.ThreadFactories;

import static com.google.common.base.Preconditions.checkArgument;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@NonFinal public class SimpleSharedNettyResources extends AbstractSharedNettyResources {

    protected int bossThreads, workerThreads;

    @Builder
    public SimpleSharedNettyResources(@NonNull final TransportType transportType, final boolean bossIsWorker,
                                      final int bossThreads, final int workerThreads) {
        super(transportType, bossIsWorker);

        checkArgument(bossThreads >= 0, "There should be at least 1 boss thread (or 0 for default)");
        checkArgument(workerThreads >= 0, "There should be at least 1 worker thread (or 0 for default)");

        this.bossThreads = bossThreads;
        this.workerThreads = workerThreads;
    }

    public static SimpleSharedNettyResources createDefault() {
        return SimpleSharedNettyResources.builder()
                                         .transportType(DefaultTransportType.getNative())
                                         .bossThreads(2)
                                         .workerThreads(0)
                                         .build();
    }

    @Override
    protected EventLoopGroup newBossLoopGroup() {
        return transportType.newEventLoopGroup(
                bossThreads, ThreadFactories.createPaginated("Netty Worker Thread #", false)
        );
    }

    @Override
    protected EventLoopGroup newWorkerLoopGroup() {
        return transportType.newEventLoopGroup(
                workerThreads, ThreadFactories.createPaginated("Netty Worker Thread #", false)
        );
    }
}