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

import java.util.Collection;
import java.util.Collections;

/**
 * An abstract implementation of {@link ModuleLoader<M>} sufficient for all operations it requires.
 *
 * @param <M> super-type of all modules managed
 */
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractModuleLoader<M extends Module> implements ModuleLoader<M> {

    @NonNull Collection<M> modules, modulesView;

    public AbstractModuleLoader(@NonNull final Collection<M> modules) {
        this.modules = modules;
        modulesView = Collections.unmodifiableCollection(modules);
    }

    @Override
    public Collection<? extends M> getModules() {
        return modulesView;
    }

    @Override
    public <T extends M, C> T loadModule(@NonNull final ModuleInitializer<T, C> initializer, final C configuration) {
        val module = initializer.loadModule(configuration);
        if (modules.add(module)) onModuleLoad(module);

        return module;
    }

    @Override
    public <T extends M> boolean unloadModule(@NonNull final T module) {
        val unloaded = modules.remove(module);
        if (unloaded) onModuleUnload(module);

        return unloaded;
    }

    /**
     * Callback used by {@link #loadModule(ModuleInitializer, Object)} to notify that the module has been loaded.
     *
     * @param module module which has just been loaded
     */
    protected void onModuleLoad(@NotNull final M module) {}

    /**
     * Callback used by {@link #unloadModule(Module)} to notify that the module has been unloaded.
     *
     * @param module module which has just been loaded
     */
    protected void onModuleUnload(@NotNull final M module) {}
}
