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

package org.feathercore.shared.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.invoke.LambdaMetafactory.metafactory;
import static java.lang.invoke.MethodType.methodType;

/**
 * Utility for common scenarios related to Java Invoke API.
 */
@UtilityClass
public class InvokeUtil {

    /**
     * Lookup used by this utility
     */
    private final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    /**
     * Method of {@link Runnable} functional method
     */
    private final String RUNNABLE_FUNCTIONAL_METHOD_NAME = "run",
    /**
     * Method of {@link Supplier} functional method
     */
    SUPPLIER_FUNCTIONAL_METHOD_NAME = "get";

    /**
     * Method type of signature: <code>{@code void}()</code>
     */
    private final MethodType VOID__METHOD_TYPE = methodType(void.class),
    /**
     * Method type of signature: <code>{@link Object}()</code>
     */
    OBJECT__METHOD_TYPE = methodType(Object.class),
    /**
     * Method type of signature: <code>{@link Runnable}()</code>
     */
    RUNNABLE__METHOD_TYPE = methodType(Runnable.class),
    /**
     * Method type of signature: <code>{@link Supplier}()</code>
     */
    SUPPLIER__METHOD_TYPE = methodType(Supplier.class),
    /**
     * Method type of signature: <code>{@link Runnable}({@link Object})</code>
     */
    RUNNABLE_OBJECT__METHOD_TYPE = methodType(Runnable.class, Object.class),
    /**
     * Method type of signature: <code>{@link Supplier}({@link Object})</code>
     */
    SUPPLIER_OBJECT__METHOD_TYPE = methodType(Supplier.class, Object.class);

    /**
     * Creates a {@link Runnable} to call the static method.
     *
     * @param method static method from which to create a {@link Runnable}
     * @return {@link Runnable} calling the given method
     *
     * @throws IllegalArgumentException if the given method is not static
     * @throws IllegalArgumentException if the given method requires parameters
     */
    public Runnable toStaticRunnable(@NonNull final Method method) {
        {
            val parameterCount = method.getParameterCount();
            checkArgument(parameterCount == 0, "method should have no parameters, got " + parameterCount);
        }
        checkArgument(Modifier.isStatic(method.getModifiers()), "method should be static");

        val methodHandle = toMethodHandle(method);
        try {
            return (Runnable) metafactory(
                    LOOKUP, RUNNABLE_FUNCTIONAL_METHOD_NAME, RUNNABLE__METHOD_TYPE,
                    VOID__METHOD_TYPE, methodHandle, VOID__METHOD_TYPE
            ).getTarget().invokeExact();
        } catch (final Throwable throwable) {
            throw new RuntimeException(
                    "An exception occurred while trying to convert method " + method + "to Runnable"
            );
        }
    }

    /**
     * Creates a {@link Runnable} to call the non-static method bound to the given object.
     *
     * @param method non-static method from which to create a {@link Runnable}
     * @param target object on which the method should be invoked
     * @return {@link Runnable} calling the given method
     *
     * @throws IllegalArgumentException if the given method is not static
     * @throws IllegalArgumentException if the given method requires parameters
     */
    public Runnable toBoundRunnable(@NonNull final Method method, @NonNull final Object target) {
        {
            val parameterCount = method.getParameterCount();
            checkArgument(parameterCount == 0, "method should have no parameters, got " + parameterCount);
        }

        val methodHandle = toMethodHandle(method);
        try {
            return (Runnable) metafactory(
                    LOOKUP, RUNNABLE_FUNCTIONAL_METHOD_NAME,
                    RUNNABLE_OBJECT__METHOD_TYPE.changeParameterType(0, target.getClass()),
                    VOID__METHOD_TYPE, methodHandle, VOID__METHOD_TYPE
            ).getTarget().invoke(target);
        } catch (final Throwable throwable) {
            throw new RuntimeException(
                    "An exception occurred while trying to convert method " + method + "to Runnable"
            );
        }
    }

    /**
     * Creates a {@link Supplier} to call the static method getting its returned value.
     *
     * @param method static method from which to create a {@link Supplier}
     * @return {@link Runnable} calling the given method
     *
     * @throws IllegalArgumentException if the given method is not static
     * @throws IllegalArgumentException if the given method requires parameters
     */
    public <T> Supplier<T> toStaticSupplier(@NonNull final Method method) {
        {
            val parameterCount = method.getParameterCount();
            checkArgument(parameterCount == 0, "method should have no parameters, got " + parameterCount);
        }
        checkArgument(Modifier.isStatic(method.getModifiers()), "method should be static");

        val methodHandle = toMethodHandle(method);
        try {
            //noinspection unchecked: generic type o returned object
            return (Supplier<T>) metafactory(
                    LOOKUP, SUPPLIER_FUNCTIONAL_METHOD_NAME, SUPPLIER__METHOD_TYPE,
                    OBJECT__METHOD_TYPE, methodHandle, methodHandle.type()
            ).getTarget().invokeExact();
        } catch (final Throwable throwable) {
            throw new RuntimeException(
                    "An exception occurred while trying to convert method " + method + "to Runnable"
            );
        }
    }

    /**
     * Creates a {@link Runnable} to call the non-static method bound to the given object.
     *
     * @param method non-static method from which to create a {@link Supplier}
     * @param target object on which the method should be invoked
     * @return {@link Runnable} calling the given method
     *
     * @throws IllegalArgumentException if the given method is not static
     * @throws IllegalArgumentException if the given method requires parameters
     */
    public <T> Supplier<T> toBoundSupplier(@NonNull final Method method, @NonNull final Object target) {
        {
            val parameterCount = method.getParameterCount();
            checkArgument(parameterCount == 0, "method should have no parameters, got " + parameterCount);
        }

        val methodHandle = toMethodHandle(method);
        try {
            //noinspection unchecked: generic type o returned object
            return (Supplier<T>) metafactory(
                    LOOKUP, SUPPLIER_FUNCTIONAL_METHOD_NAME,
                    SUPPLIER_OBJECT__METHOD_TYPE.changeParameterType(0, target.getClass()),
                    OBJECT__METHOD_TYPE, methodHandle, methodType(method.getReturnType())
            ).getTarget().invoke(target);
        } catch (final Throwable throwable) {
            throw new RuntimeException(
                    "An exception occurred while trying to convert method " + method + "to Runnable"
            );
        }
    }

    /**
     * Creates a {@link Supplier} to call the constructor getting its returned value.
     *
     * @param constructor constructor from which to create a {@link Supplier}
     * @return {@link Runnable} calling the given method
     *
     * @throws IllegalArgumentException if the given method is not static
     * @throws IllegalArgumentException if the given method requires parameters
     */
    public <T> Supplier<T> toSupplier(@NonNull final Constructor<T> constructor) {
        {
            val parameterCount = constructor.getParameterCount();
            checkArgument(parameterCount == 0, "method should have no parameters, got " + parameterCount);
        }

        val methodHandle = toMethodHandle(constructor);
        try {
            //noinspection unchecked: generic type o returned object
            return (Supplier<T>) metafactory(
                    LOOKUP, SUPPLIER_FUNCTIONAL_METHOD_NAME, SUPPLIER__METHOD_TYPE,
                    OBJECT__METHOD_TYPE, methodHandle, methodHandle.type()
            ).getTarget().invokeExact();
        } catch (final Throwable throwable) {
            throw new RuntimeException(
                    "An exception occurred while trying to convert constructor " + constructor + "to Runnable"
            );
        }
    }

    /**
     * Converts the given method to a {@link MethodHandle}.
     *
     * @param method method to convert to {@link MethodHandle}
     * @return {@link MethodHandle} created from the given method
     */
    public static MethodHandle toMethodHandle(@NonNull final Method method) {
        final MethodHandle methodHandle;
        {
            val accessible = method.isAccessible();
            method.setAccessible(true);
            try {
                methodHandle = LOOKUP.unreflect(method);
            } catch (final IllegalAccessException e) {
                throw new RuntimeException("Method " + method + " cannot be accessed", e);
            } finally {
                method.setAccessible(accessible);
            }
        }
        return methodHandle;
    }

    /**
     * Converts the given constructor to a {@link MethodHandle}.
     *
     * @param constructor constructor to convert to {@link MethodHandle}
     * @return {@link MethodHandle} created from the given method
     */
    public static MethodHandle toMethodHandle(@NonNull final Constructor<?> constructor) {
        final MethodHandle methodHandle;
        {
            val accessible = constructor.isAccessible();
            constructor.setAccessible(true);
            try {
                methodHandle = LOOKUP.unreflectConstructor(constructor);
            } catch (final IllegalAccessException e) {
                throw new RuntimeException("Method " + constructor + " cannot be accessed", e);
            } finally {
                constructor.setAccessible(accessible);
            }
        }
        return methodHandle;
    }
}
