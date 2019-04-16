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

package org.feathercore.eventbus;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * A manager for handling events.
 *
 * @param <E> super-type of events handled
 */
public interface EventManager<E extends Event> {

    /**
     * Registers an event-handler for the specified event type.
     *
     * @param eventType type of the event
     * @param handler handler of the event
     * @param priority event priority (higher priority means latter call)
     *
     * @param <T> generic type of the event
     */
    <T extends E> void register(@NonNull Class<T> eventType,
                                @NonNull Consumer<T> handler, byte priority);

    /**
     * Registers an event-handler for the specified event type using <i>normal</i> priority.
     *
     * @param eventType type of the event
     * @param handler handler of the event
     *
     * @param <T> generic type of the event
     */
    default <T extends E> void register(@NonNull Class<T> eventType, @NonNull Consumer<T> handler) {
        register(eventType, handler, (byte) 0);
    }

    /**
     * Registers all methods of this object annotated as {@link EventHandler}
     * as listeners using the annotations configuration.
     *
     * @param listener object whose annotated methods should be registered as listeners
     * and who should be used for calling them
     */
    void register(@NonNull Object listener);

    /**
     * Calls the specified event.
     *
     * @param event event to call
     * @param <T> exact type of the event to call
     */
    <T extends E> void call(@NotNull T event);
}
