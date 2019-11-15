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
import java.util.Collections;
import java.util.Optional;

/**
 * Loader of {@link Module modules} which uses {@link ModuleInitializer initializers} for this purpose.
 *
 * @param <M> super-type of modules which this module-loader can manage
 */
public interface ModuleLoader<M extends Module> {

    /**
     * Gets all modules loaded by this module loader.
     *
     * @return all loaded modules
     *
     * @apiNote modifications to returned collection should not have side-effect on this module loader
     * however it is recommended to perform copying of it if loading/unloading modules while iterating
     * so that {@link java.util.ConcurrentModificationException} does not happen
     */
    Collection<M> getModules();

    /**
     * Finds a module of given type loaded by this module loader.
     *
     * @param type type of module for which to look
     * @param <T> exact type of module searched
     * @return {@link Optional} containing a module of given type if it was found or an empty {@link Optional} otherwise
     */
    <T extends M> Optional<? extends T> getModule(@NonNull final Class<T> type);

    /**
     * Checks whether there is a module of given type currently loaded by this module loader.
     *
     * @param type type of module to get checked
     * @return {@link true} if there is a module of given type currently loaded and {@link false} otherwise
     *
     * @apiNote multiple modules of given type may be loaded at once
     */
    boolean isModuleLoaded(@NonNull final Class<? extends M> type);

    /**
     * Checks whether the given module is currently loaded by this module loader.
     *
     * @param module module to check
     * @return {@link true} if the module is loaded and {@link false} otherwise
     */
    <T extends M> boolean isModuleLoaded(@NonNull final T module);

    /**
     * Loads a module provided by the specified initializer using the given configuration.
     *
     * @param <T> exact type of a module loaded
     * @param <C> configuration type of a module ({@link Void} if none is expected)
     * @param initializer initializer to use for module's initialization
     * @param configuration configuration required by the module
     * (might be {@link null} depending on the initializer implementation)
     * @param moduleTypes
     * @return initialized module
     */
    <T extends M, C> T loadModule(@NonNull ModuleInitializer<T, C> initializer, C configuration,
                                  Iterable<Class<T>> moduleTypes);

    /**
     * Loads a module provided by the specified initializer using its default configuration.
     *
     * @param <T> exact type of a module loaded
     * @param <C> configuration type of a module ({@link Void} if none is expected)
     * @param initializer initializer to use for module's initialization
     * @param moduleTypes types by which the modules will be known
     * @return initialized module
     * @throws ModuleConfigurationException if this initializer doesn't allow default configuration
     */
    default <T extends M, C> T loadModule(@NonNull final ModuleInitializer<T, C> initializer,
                                          final Iterable<Class<T>> moduleTypes) {
        return loadModule(initializer, initializer.getDefaultConfiguration().orElseThrow(
                () -> new ModuleConfigurationException(initializer + " does not allow usage of default configuration")
        ), moduleTypes);
    }

    default <T extends M, C> T loadModule(@NonNull final ModuleInitializer<T, C> initializer, C configuration,
                                          @NonNull final Class<T> moduleType) {
        return loadModule(initializer, configuration, Collections.singletonList(moduleType));
    }


    default <T extends M, C> T loadModule(@NonNull final ModuleInitializer<T, C> initializer,
                                          @NonNull final Class<T> moduleType) {
        return loadModule(initializer, Collections.singletonList(moduleType));
    }

    /**
     * Unloads the specified module.
     *
     * @param <T> exact type of a module unloaded
     * @param moduleType module which should unloaded
     * @return {@code true} if the specified module was loaded (and so got unloaded) and {@link false} otherwise
     */
    <T extends M> Optional<T> unloadModule(@NonNull Class<? extends T> moduleType);

    /**
     * Unloads all modules loaded at current time.
     *
     * @return all modules unloaded
     */
    Collection<? extends M> unloadAllModules();
}
