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

package org.feathercore.shared.util.thread;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility-methods used for creation of typical {@link ThreadFactory thread factories}.
 *
 * @author Dimasik
 * @author PROgrammer_JARvis
 */
@UtilityClass
public class ThreadFactories {

    /**
     * Creates a {@link ThreadFactory thread factory} which picks names of created threads using a counter.
     *
     * @param prefix prefix of the thread name
     * @param suffix suffix of the thread name
     * @param daemon daemon-flag of the thread
     * @return created thread factory
     *
     * @see #createPaginated(String, boolean) simpler alternative
     */
    public ThreadFactory createPaginated(@NonNull final String prefix,
                                         @NonNull final String suffix,
                                         @NonNull final boolean daemon) {
        // intialized here so that it gets captured by lambda
        val counter = new AtomicInteger();

        return (runnable) -> {
            val thread = new Thread(runnable);
            thread.setDaemon(daemon);
            thread.setName(prefix + counter.getAndIncrement() + suffix);

            return thread;
        };
    }

    /**
     * Creates a {@link ThreadFactory thread factory} which picks names of created threads using a counter.
     *
     * @param prefix prefix of the thread name
     * @param daemon daemon-flag of the thread
     * @return created thread factory
     *
     * @see #createPaginated(String, String, boolean) alternative method with richer naming control
     */
    public ThreadFactory createPaginated(@NonNull final String prefix,
                                         @NonNull final boolean daemon) {
        return createPaginated(prefix, "", daemon);
    }
}
