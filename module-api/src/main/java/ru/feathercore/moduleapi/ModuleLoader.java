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
import lombok.val;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Loader of {@link Module modules} which uses {@link ModuleInitializer initializers} for this purpose.
 *
 * @param <M> super-type of all modules managed
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
    Collection<? extends M> getModules();

    /**
     * Gets all modules of given type loaded by this module loader.
     *
     * @param type type of modules for which to look
     * @param <T> exact type of modules searched
     * @return all loaded modules of given type
     */
    @SuppressWarnings("unchecked")
    default <T extends M> Collection<? extends T> getModules(@NonNull final Class<T> type) {
        return (Collection<? extends T>) getModules()
                .stream()
                .filter(module -> type.isAssignableFrom(module.getClass()))
                .collect(Collectors.toSet());
    }

    /**
     * Finds a module of given type loaded by this module loader.
     *
     * @param type type of module for which to look
     * @param <T> exact type of module searched
     * @return {@link Optional} containing a module of given type if it was found or an empty {@link Optional} otherwise
     */
    @SuppressWarnings("unchecked")
    default <T extends M> Optional<? extends T> getModule(@NonNull final Class<T> type) {
        return (Optional<? extends T>) getModules()
                .stream()
                .filter(module -> type.isAssignableFrom(module.getClass()))
                .findAny();
    }

    /**
     * Checks whether there is a module of given type currently loaded by this module loader.
     *
     * @param type type of module to get checked
     * @return {@link true} if there is a module of given type currently loaded and {@link false} otherwise
     *
     * @apiNote multiple modules of given type may be loaded at once
     */
    default boolean isModuleLoaded(@NonNull final Class<? extends M> type) {
        return getModules().stream().anyMatch(module -> type.isAssignableFrom(module.getClass()));
    }

    /**
     * Checks whether the given module is currently loaded by this module loader.
     *
     * @param module module to check
     * @return {@link true} if the module is loaded and {@link false} otherwise
     */
    default <T extends M> boolean isModuleLoaded(@NonNull final T module) {
        return getModules().contains(module);
    }

    /**
     * Loads a module provided by the specified initializer using the given configuration.
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
        ));
    }

    /**
     * Unloads all modules of the specified type.
     *
     * @param moduleTypes type of modules to unload
     * @param <T> exact type of modules unloaded
     * @return collection (possibly empty) of unloaded modules of given type
     */
    default <T extends M> Collection<? extends T> unloadModules(@NonNull final Class<T> moduleTypes) {
        val modules = getModules(moduleTypes);
        for (val loadedModule : modules) unloadModule(loadedModule);

        return modules;
    }

    /**
     * Unloads the specified module.
     *
     * @param module module which should unloaded
     * @param <T> exact type of a module unloaded
     * @return {@code true} if the specified module was loaded (and so got unloaded) and {@link false} otherwise
     */
    <T extends M> boolean unloadModule(@NonNull T module);

    /**
     * Unloads all modules loaded at current time.
     *
     * @return all modules unloaded
     */
    default Collection<? extends M> unloadAllModules() {
        val modules = new ArrayList<>(getModules());
        for (val loadedModule : modules) unloadModule(loadedModule);

        return modules;
    }
}
