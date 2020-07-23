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

import org.jetbrains.annotations.NotNull;
import ru.progrm_jarvis.javacommons.object.ValueContainer;

/**
 * Initializer of a module which is normally used by {@link ModuleLoader} to load a specific module.
 *
 * @param <M> type of a module initialized
 * @param <C> module configuration (use {@link Void} when none)
 */
@FunctionalInterface
public interface ModuleInitializer<M extends Module, C> {

    /**
     * Loads the associated module.
     *
     * @param configuration configuration of a module
     * @return initialized module
     * @throws ModuleConfigurationException if the configuration was proven to be inappropriate
     * @throws ModuleInitializationException if a module could not be initialized
     *
     * @apiNote if a {@link ModuleConfigurationException} happens at the very module initialization
     * then it is expected to be wrapped using {@link ModuleInitializationException}
     */
    M loadModule(C configuration) throws ModuleConfigurationException, ModuleInitializationException;

    /**
     * Gets the default configuration for the module.
     *
     * @return value container of the default configuration if it exists
     * or an empty one of this module initializer <b>requires</b> a configuration for which a default one does not exist
     *
     * @apiNote if type of {@link C} is {@link Void} then {@link ValueContainer#ofNull()} should be returned
     */
    @NotNull default ValueContainer<C> getDefaultConfiguration() {
        return ValueContainer.ofNull();
    }
}
