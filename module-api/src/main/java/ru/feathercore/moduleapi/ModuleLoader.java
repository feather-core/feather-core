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

/**
 * Loader of {@link Module modules} which uses {@link ModuleInitializer initializers} for this purpose.
 */
public interface ModuleLoader<M extends Module> {

    /**
     * Loads a module provided by the specified initializer
     *
     * @param initializer initializer to use for module's initialization
     * @param configuration configuration required by the module
     * (might be {@link null} depending on the initializer implementation)
     * @param <T> exact type of a module loaded
     * @param <C> configuration tye of a module ({@link Void} if none is expected)
     * @return initialized module
     */
    <T extends M, C> T loadModule(@NonNull ModuleInitializer<T, C> initializer, C configuration);

    default <T extends M, C> T loadModule(@NonNull ModuleInitializer<T, C> initializer) {
        return loadModule(initializer, null);
    }
}
