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

package org.feathercore.runtime.generation;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class InterfaceImplementor {

    private final static Pattern GETTER_PATTERN = Pattern.compile("/^get(?<varname>.+)$/");
    private final static Pattern SETTER_PATTERN = Pattern.compile("/^set(?<varname>.+)$/");

    public static <T> ImplementationGenerator<T> createFactory(Class<T> interfaceClass) {
        Map<String, Class<?>> fieldTypes = new HashMap<>();
        for (Method method : interfaceClass.getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.isDefault()) {
                continue;
            }
            String name = method.getName();
            Matcher matcher = GETTER_PATTERN.matcher(name);
            if (matcher.matches()) {
                if (method.getParameterCount() != 0) {
                    throw new IllegalArgumentException("Could not create getter (must be without args): " + interfaceClass.getName() + "#" + name);
                }
                Class<?> returnType = method.getReturnType();
                if (returnType == void.class || returnType == Void.class) {
                    throw new IllegalArgumentException("Could not create getter (must not return void): " + interfaceClass.getName() + "#" + name);
                }
                String varname = matcher.group("varname").toLowerCase();
                if (fieldTypes.containsKey(varname)) {
                    if (fieldTypes.get(varname) != returnType) {
                        throw new IllegalArgumentException("Could not create getter (type mismatch with setter for same field): " + interfaceClass.getName() + "#" + name);
                    }
                } else {
                    fieldTypes.put(varname, returnType);
                }
                continue;
            }
            matcher = SETTER_PATTERN.matcher(name);
            if (matcher.matches()) {
                if (method.getParameterCount() != 1) {
                    throw new IllegalArgumentException("Could not create setter (must be with a single arg): " + interfaceClass.getName() + "#" + name);
                }
                Class<?> returnType = method.getReturnType();
                if (returnType != void.class && returnType != Void.class) {
                    throw new IllegalArgumentException("Could not create setter (must return void): " + interfaceClass.getName() + "#" + name);
                }
                String varname = matcher.group("varname").toLowerCase();
                Class<?> argumentType = method.getParameterTypes()[0];
                if (fieldTypes.containsKey(varname)) {
                    if (fieldTypes.get(varname) != argumentType) {
                        throw new IllegalArgumentException("Could not create setter (type mismatch with getter for same field): " + interfaceClass.getName() + "#" + name);
                    }
                } else {
                    fieldTypes.put(varname, argumentType);
                }
                continue;
            }
            throw new IllegalArgumentException("Could not implement method that is not a getter/setter: " + interfaceClass.getName() + "#" + name);
        }
        return () -> {
            Map<String, Object> fields = new HashMap<>();
            //noinspection unchecked
            return (T) Proxy.newProxyInstance(InterfaceImplementor.class.getClassLoader(), new Class[]{interfaceClass}, (instance, method, args) -> {
                if (method.isDefault()) {
                    return method.invoke(instance, args);
                }
                String name = method.getName();
                Matcher matcher = GETTER_PATTERN.matcher(name);
                if (matcher.matches()) {
                    String varname = matcher.group("varname").toLowerCase();
                    synchronized (fields) {
                        return fields.get(varname);
                    }
                }
                matcher = SETTER_PATTERN.matcher(name);
                if (matcher.matches()) {
                    String varname = matcher.group("varname").toLowerCase();
                    synchronized (fields) {
                        fields.put(varname, args[0]);
                    }
                }
                return null;
            });
        };
    }

    public static <T> T create(Class<T> interfaceClass) {
        return createFactory(interfaceClass).create();
    }

}
