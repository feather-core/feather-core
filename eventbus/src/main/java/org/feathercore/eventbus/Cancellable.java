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

/**
 * An object which may be cancelled.
 */
public interface Cancellable {

    /**
     * Gets this object's cancellation state
     *
     * @return {@link true} if this object is marked as cancelled and {@link false} otherwise
     */
    boolean isCancelled();

    /**
     * Sets this object's cancellation state to the specified value.
     *
     * @param cancelled flag marking whether or not this object is marked as cancelled
     */
    void setCancelled(boolean cancelled);
}
