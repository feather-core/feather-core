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

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Initializer of a module which is normally used by {@link ModuleLoader} to load a specific module.
 *
 * @param <M> type of a module initialized
 * @param <C> module configuration (use {@link Void} when none)
 */
@FunctionalInterface
public interface ModuleInitializer<M extends Module, C> {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Supplier<?>> OPTIONAL_NULL_SUPPLIER
            = Optional.of(() -> null);

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
     * @return optional containing supplier of the default configuration if it exists
     * and an empty optional otherwise which means that this object <b>requires</b> it but cannot provide a default one
     *
     * @apiNote if type of {@link C} is {@link Void} then {@link Optional optional} of {@link Supplier supplier}
     * of {@link null} should be returned which is recommended to be taken from {@link #OPTIONAL_NULL_SUPPLIER}
     */
    @SuppressWarnings("unchecked")
    @NotNull default Optional<Supplier<C>> getDefaultConfiguration() {
        // somehow the direct cast is impossible
        return (Optional<Supplier<C>>) (Object) OPTIONAL_NULL_SUPPLIER;
    }
}
