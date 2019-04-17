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

package org.feathercore.pool;

import lombok.NonNull;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public abstract class ConcurrentObjectPool<T extends Poolable<T>> extends ObjectPool<T> {

    private final Queue<T> queue;
    private final int retries;

    /**
     * Creates new concurrent pool
     *
     * @param maxSize maximum size of pool
     * @param retries maximum attempts to get object from pool before we give up
     */
    public ConcurrentObjectPool(final int maxSize, final int retries) {
        assert (maxSize > 0);
        assert (retries > 0);
        this.retries = retries;
        this.queue = new LinkedBlockingQueue<>(maxSize);
    }

    @Override
    public T get() {
        for (int i = 0; (i++ < retries); ) {
            T result = queue.poll();
            if (result != null) {
                return result;
            }
        }
        return createObject(this);
    }

    @Override
    public boolean release(@NonNull final T object) {
        return queue.offer(object);
    }

    /**
     * Creates new concurrent pool
     *
     * @param transformer transformer that is used to release an object back
     * @return created object pool
     * @see ConcurrentObjectPool#ConcurrentObjectPool(int, int)
     */
    public static <T extends Poolable<T>> ObjectPool<T> createNew(int maxSize, int retries,
                                                                  @NonNull Function<PoolHandle<T>, T> transformer) {
        return new ConcurrentObjectPool<T>(maxSize, retries) {
            @Override
            protected T createObject(final PoolHandle<T> handle) {
                return transformer.apply(handle);
            }
        };
    }
}
