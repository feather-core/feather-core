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
 * An abstract implementation of {@link ModuleLoader<M>} sufficient for all its requires operations.
 *
 * @param <M> super-type of all modules managed
 */
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AbstractModuleLoader<M extends Module> implements ModuleLoader<M> {

    @NonNull Collection<? extends M> modules, modulesView;

    public AbstractModuleLoader(@NonNull final Collection<? extends M> modules) {
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
        onModuleLoad(module);

        return module;
    }

    protected void onModuleLoad(@NotNull final M module) {}
}
