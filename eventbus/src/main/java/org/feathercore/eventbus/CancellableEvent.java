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
 * {@link Event} which may be cancelled.
 */
public interface CancellableEvent extends Event {

    /**
     * Gets this event's cancellation state
     *
     * @return {@link true} if this event is cancelled and {@link false} otherwise
     */
    boolean isCancelled();

    /**
     * Sets this event's cancellation state to the specified value.
     *
     * @param cancelled {@link true} if this event should be marked as cancelled and {@link false} otherwise
     */
    void setCancelled(boolean cancelled);
}
