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

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class EventManager {

    private static final Map<Class<Event>, Set<Handler>> HANDLERS = new ConcurrentHashMap<>();

    /**
     * Register given listener (all events handling method within it).
     *
     * @param listener
     */
    @SuppressWarnings("unchecked")
    public static void register(IListener listener) {
        Class<? extends IListener> clazz = listener.getClass();
        Class<Event> event = Event.class;
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getParameterCount() != 1 || !m.isAnnotationPresent(EventHandler.class)) {
                continue;
            }
            Class<?> param = m.getParameterTypes()[0];
            if (!event.isAssignableFrom(param)) {
                continue;
            }
            @SuppressWarnings("SuspiciousMethodCalls") Set<Handler> handlers = HANDLERS.get(param);
            if (handlers == null) {
                handlers = new TreeSet<>(Comparator.comparing(Handler::priority).thenComparingInt(a -> a.id));
                handlers = Collections.synchronizedSet(handlers);
                HANDLERS.put((Class<Event>) param, handlers);
            }
            EventHandler annotation = m.getAnnotation(EventHandler.class);
            handlers.add(new Handler(annotation.priority(), annotation.ignoreCancelled(), constructConsumer(listener, m)));
        }
    }

    /**
     * Call given event (invoke all methods in registered listeners, which are handling this event).
     * Doesn't invoke handle-methods for any parents of the given event.
     *
     * @param event
     */
    public static void call(Event event) {
        @SuppressWarnings("SuspiciousMethodCalls") Set<Handler> handlers = HANDLERS.get(event.getClass());
        if (handlers == null) {
            return;
        }
        CancellableEvent ce = event instanceof CancellableEvent ? (CancellableEvent) event : null;
        for (Handler h : handlers) {
            if (h.ignoreCancelled && ce != null && ce.isCancelled()) {
                continue;
            }
            h.consumer.accept(event);
        }
    }

    @SuppressWarnings("unchecked")
    private static Consumer<Event> constructConsumer(IListener listener, Method method) {
        try {
            MethodHandles.Lookup lookup = constructLookup(listener.getClass());
            return (Consumer<Event>) LambdaMetafactory.metafactory(
                    lookup,
                    "accept",
                    MethodType.methodType(Consumer.class, listener.getClass()),
                    MethodType.methodType(void.class, Object.class),
                    lookup.unreflect(method),
                    MethodType.methodType(void.class, method.getParameterTypes()[0])
            ).getTarget().invoke(listener);
        } catch (Throwable t) {
            throw new AssertionError("Could not create event listener", t);
        }
    }

    /**
     * We are in need of our own Lookup creation with an argument of given owner class, because we want to be able
     * to execute private and any other non-public methods.
     *
     * @param owner owner class.
     * @return lookup with owner class privileges.
     * @throws Exception if something is wrong (?)
     */
    private static MethodHandles.Lookup constructLookup(Class<?> owner) throws Exception {
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
        constructor.setAccessible(true);
        return constructor.newInstance(owner);
    }

    private static class Handler {

        private static final AtomicInteger ID = new AtomicInteger();

        private final byte priority;
        private final boolean ignoreCancelled;
        private final Consumer<Event> consumer;
        private final int id;

        Handler(byte priority, boolean ignoreCancelled, Consumer<Event> consumer) {
            this.priority = priority;
            this.ignoreCancelled = ignoreCancelled;
            this.consumer = consumer;
            this.id = ID.incrementAndGet();
        }

        private byte priority() {
            return this.priority;
        }

    }

}