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

package ru.feathercore.moduleapi;

import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple module loader based on {@link AbstractModuleLoader}.
 * It provides shorthand constructors for most commons use-cases.
 *
 * @param <M> super-type of modules which this module-loader can manage
 */
public class SimpleModuleLoader<M extends Module> extends AbstractModuleLoader<M> {

    /**
     * Creates new instance of module loader using the given set for storage of modules.
     *
     * @param modulesSet set which will be used for storage of loaded modules
     *
     * @throws IllegalArgumentException if the given set is non-empty
     */
    public SimpleModuleLoader(@NonNull final Set<M> modulesSet) {
        super(modulesSet);
    }

    /**
     * Creates new instance of module loader enabling concurrency support if needed.
     *
     * @param concurrent {@code true} if the module loader should allow concurrent operations
     * and {@code false} otherwise
     */
    public SimpleModuleLoader(final boolean concurrent) {
        this(concurrent ? ConcurrentHashMap.newKeySet() : new HashSet<>());
    }

    /**
     * Creates new instance of module loader enabling concurrency support.
     */
    public SimpleModuleLoader() {
        this(true);
    }
}
