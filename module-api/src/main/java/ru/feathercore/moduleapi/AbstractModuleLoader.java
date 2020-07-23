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

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * An abstract implementation of {@link ModuleLoader<M>} sufficient for all operations it requires.
 *
 * @param <M> super-type of modules which this module-loader can manage
 *
 * // TODO: 15.11.2019 Docs
 */
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AbstractModuleLoader<M extends Module> implements ModuleLoader<M> {

    @NonNull Map<Class<? extends M>, M> modules;
    /**
     * Storage of unique instanced of loaded modules.
     */
    @NonNull Set<M> loadedModules, loadedModulesView;

    /**
     * Creates new instance of module loader using the given {@link Collection collections} for storage of modules.
     *
     * @param modulesMap set which will be used for storage of associations of module types with their implementations
     * @param loadedModulesSet set which will be used for storage of loaded modules
     * @throws IllegalArgumentException if the given set is non-empty
     */
    public AbstractModuleLoader(@NonNull final Map<Class<? extends M>, M> modulesMap,
                                @NonNull final Set<M> loadedModulesSet) {
        if (!modulesMap.isEmpty()) throw new IllegalArgumentException("modules map should be empty");
        if (!loadedModulesSet.isEmpty()) throw new IllegalArgumentException("loaded modules set should be empty");

        modules = modulesMap;
        loadedModules = loadedModulesSet;
        loadedModulesView = Collections.unmodifiableSet(loadedModules);
    }

    /* ************************************************* Callbacks ************************************************* */

    /**
     * Callback used by method-loading methods to notify that the module has been loaded.
     *
     * @param module module which has just been loaded
     */
    protected void onModuleLoad(@NotNull final M module) {}

    /**
     * Callback used by {@link #unloadModule(Class)} to notify that the module has been unloaded.
     *
     * @param module module which has just been unloaded
     */
    protected void onModuleUnload(@NotNull final M module) {}

    /* *************************************************** Logic *************************************************** */

    @Override
    public Collection<M> getModules() {
        return loadedModulesView;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends M> Optional<? extends T> getModule(@NonNull final Class<T> type) {
        return Optional.ofNullable((T) modules.get(type));
    }

    @Override
    public boolean isModuleLoaded(@NonNull final Class<? extends M> type) {
        return modules.containsKey(type);
    }

    @Override
    public <T extends M> boolean isModuleLoaded(@NonNull final T module) {
        return modules.containsValue(module);
    }

    @Override
    public <T extends M, C> T loadModule(@NonNull final ModuleInitializer<T, C> initializer, final C configuration,
                                         final Iterable<Class<T>> moduleTypes) {
        val module = initializer.loadModule(configuration);
        for (val moduleType : moduleTypes) modules.put(moduleType, module);
        loadedModules.add(module);

        onModuleLoad(module);

        return module;
    }

    @Override
    public <T extends M> Optional<T> unloadModule(@NonNull final Class<? extends T> moduleType) {
        @SuppressWarnings("unchecked") val module = (T) modules.remove(moduleType);
        if (module != null) {
            if (!modules.containsValue(module)) loadedModules.remove(module);
            onModuleUnload(module);

            return Optional.of(module);
        }

        return Optional.empty();
    }

    @Override
    public Collection<? extends M> unloadAllModules() {
        val modulesUnloaded = new ArrayList<>(loadedModules);

        for (val module : modulesUnloaded) onModuleUnload(module);

        modules.clear();
        loadedModules.clear();

        return modulesUnloaded;
    }
}
