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

import java.util.Collection;

/**
 * Loader of {@link Module modules} which uses {@link ModuleInitializer initializers} for this purpose.
 */
public interface ModuleLoader<M extends Module> {

    /**
     * Loads a module provided by the specified initializer and using the given.
     *
     * @param initializer initializer to use for module's initialization
     * @param configuration configuration required by the module
     * (might be {@link null} depending on the initializer implementation)
     * @param <T> exact type of a module loaded
     * @param <C> configuration type of a module ({@link Void} if none is expected)
     * @return initialized module
     */
    <T extends M, C> T loadModule(@NonNull ModuleInitializer<T, C> initializer, C configuration);

    /**
     * Loads a module provided by the specified initializer using its default configuration.
     *
     * @param initializer initializer to use for module's initialization
     * @param <T> exact type of a module loaded
     * @param <C> configuration type of a module ({@link Void} if none is expected)
     * @return initialized module
     * @throws ModuleConfigurationException if this initializer doesn't allow default configuration
     */
    default <T extends M, C> T loadModule(@NonNull ModuleInitializer<T, C> initializer) {
        return loadModule(initializer, initializer.getDefaultConfiguration().orElseThrow(
                () -> new ModuleConfigurationException(initializer + " does not allow usage of default configuration")
        ).get());
    }

    /**
     * Gets all modules loaded by this module loader.
     *
     * @return all loaded modules
     *
     * @apiNote modifications to returned collection should not have side-effect on this module loader
     */
    Collection<? extends M> getModules();
}
