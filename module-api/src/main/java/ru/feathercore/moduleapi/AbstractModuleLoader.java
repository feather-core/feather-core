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

import com.google.common.base.Preconditions;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * An abstract implementation of {@link ModuleLoader<M>} sufficient for all operations it requires.
 *
 * @param <M> super-type of modules which this module-loader can manage
 */
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractModuleLoader<M extends Module> implements ModuleLoader<M> {

    @NonNull Set<M> modules, modulesView;

    /**
     * Creates new instance of module loader using the given set for storage of modules.
     *
     * @param modulesSet set which will be used for storage of loaded modules
     *
     * @throws IllegalArgumentException if the given set is non-empty
     */
    public AbstractModuleLoader(@NonNull final Set<M> modulesSet) {
        Preconditions.checkArgument(modulesSet.isEmpty(), "modulesSet should be empty");

        this.modules = modulesSet;
        modulesView = Collections.unmodifiableSet(modulesSet);
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
