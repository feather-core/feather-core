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

package org.feathercore.shared.util;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by k.shandurenko on 12/04/2019
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class NamedThreadFactory implements ThreadFactory {

    @NonNull String name;
    boolean daemon;
    @NonNull AtomicInteger counter = new AtomicInteger(1);

    @Override
    public Thread newThread(@NonNull final Runnable runnable) {
        val thread = new Thread(runnable);
        thread.setDaemon(daemon);
        thread.setName(name + counter.getAndIncrement());

        return thread;
    }
}