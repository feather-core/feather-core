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

import lombok.*;
import lombok.experimental.Delegate;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Created by k.shandurenko on 09/04/2019
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class SimpleEventManager<E extends Event> implements EventManager<E> {

    @NonNull Map<Class<? extends E>, HandlerQueue<? extends E>> handlers;

    public SimpleEventManager() {
        this(new ConcurrentHashMap<>());
    }

    @NotNull
    @SuppressWarnings("unchecked")
    protected <T extends E> HandlerQueue<T> getHandlerQueue(@NonNull final Class<T> eventType) {
        return (HandlerQueue<T>) handlers.computeIfAbsent(eventType, type -> new HandlerQueue<>());
    }

    @Nullable
    @SuppressWarnings("unchecked")
    protected <T extends E> HandlerQueue<E> getNullableHandlerQueue(@NonNull final Class<T> eventType) {
        return (HandlerQueue<E>) handlers.get(eventType);
    }

    @Override
    public <T extends E> void register(@NonNull final Class<T> eventType,
                                       @NonNull final Consumer<T> handler, final byte priority) {
        getHandlerQueue(eventType).add(new Handler<>(handler, priority));
    }

    /**
     * Register given listener (all events handling method within it).
     *
     * @param listener
     */
    @Override
    @SuppressWarnings("unchecked")
    public void register(@NonNull final Object listener) {
        val listenerClass = listener.getClass();
        for (val method : listenerClass.getDeclaredMethods()) {
            if (method.getParameterCount() != 1 || !method.isAnnotationPresent(EventHandler.class)) {
                continue;
            }

            val parameterType = method.getParameterTypes()[0];
            if (!Event.class.isAssignableFrom(parameterType)) {
                continue;
            }

            EventHandler annotation = method.getAnnotation(EventHandler.class);
            register((Class<? extends E>) parameterType, constructConsumer(listener, method), annotation.priority());
        }
    }

    /**
     * Call given event (invoke all methods in registered listeners, which are handling this event).
     * Doesn't invoke handle-methods for any parents of the given event.
     *
     * @param event
     */
    @Override
    public <T extends E> void call(@NotNull final T event) {
        // because generics are magic
        @SuppressWarnings("unchecked") val handlers = getNullableHandlerQueue((Class<? extends E>) event.getClass());
        if (handlers == null) {
            return;
        }

        for (val handler : handlers) {
            handler.accept(event);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <E extends Event> Consumer<E> constructConsumer(Object listener, Method method) {
        try {
            Lookup lookup = constructLookup(listener.getClass());
            return (Consumer<E>) LambdaMetafactory.metafactory(
                    lookup,
                    "accept",
                    MethodType.methodType(Consumer.class, listener.getClass()),
                    MethodType.methodType(void.class, Object.class),
                    lookup.unreflect(method),
                    MethodType.methodType(void.class, method.getParameterTypes()[0])
            ).getTarget().invoke(listener);
        } catch (Throwable t) {
            throw new RuntimeException("Could not create event listener", t);
        }
    }

    /**
     * We are in need of our own Lookup creation with an argument of given owner class, because we want to be able
     * to execute private and any other non-public methods.
     *
     * @param owner owner class
     * @return lookup with owner class privileges
     */
    @SneakyThrows
    protected static Lookup constructLookup(Class<?> owner) {
        val constructor = Lookup.class.getDeclaredConstructor(Class.class);
        constructor.setAccessible(true);

        return constructor.newInstance(owner);
    }

    @Data
    @FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
    private static class Handler<E extends Event> implements Comparable<Handler<E>>, Consumer<E> {
        @Delegate Consumer<E> consumer;

        byte priority;

        @Override
        public int compareTo(@NonNull final Handler<E> handler) {
            return priority - handler.priority;
        }
    }

    @Value
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
    protected static class HandlerQueue<E extends Event> extends PriorityQueue<Consumer<E>> {

        // because JDK is an evil guy
        private static final long serialVersionUID = 297336550679724498L;

        @NonNull Queue<Consumer<E>> queue;

        public HandlerQueue() {
            this(new PriorityQueue<>());
        }
    }
}