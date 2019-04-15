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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by k.shandurenko on 09/04/2019
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface EventHandler {

    /**
     * Priority of execution.
     * All methods, which are handling the same event as the method, marked with this annotation,
     * will be executed in order from Byte.MIN_VALUE to Byte.MAX_VALUE priority.
     *
     * @return execution priority of annotated method.
     */
    byte priority() default 0;

    /**
     * Determines, whether or not this method should handle events that were cancelled
     * (maybe, on previous handler-methods).
     *
     * @return whether annotated method ignores cancelled events
     */
    boolean ignoreCancelled() default true;

}