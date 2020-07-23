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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
     * Creates new instance of module loader using the given collections for storage of modules.
     *
     * @param modulesMap set which will be used for storage of associations of module types with their implementations
     * @param loadedModulesSet set which will be used for storage of loaded modules
     *
     * @throws IllegalArgumentException if the given set is non-empty
     */
    protected SimpleModuleLoader(@NonNull final Map<Class<? extends M>, M> modulesMap,
                                 @NonNull final Set<M> loadedModulesSet) {
        super(modulesMap, loadedModulesSet);
    }

    public static <M extends Module> ModuleLoader<M> create() {
        return new SimpleModuleLoader<M>(new HashMap<>(), new HashSet<>());
    }

    public static <M extends Module> ModuleLoader<M> createConcurrent() {
        return new SimpleModuleLoader<M>(new ConcurrentHashMap<>(), ConcurrentHashMap.newKeySet());
    }
}
