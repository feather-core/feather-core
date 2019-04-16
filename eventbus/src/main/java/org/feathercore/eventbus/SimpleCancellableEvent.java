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

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Standard implementation of {@link CancellableEvent}.
 */
@Data
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class SimpleCancellableEvent implements CancellableEvent {

    boolean cancelled = false;

    @Override
    public boolean callCancellableGlobally() {
        EventManager.getGlobal().call(this);

        return cancelled;
    }
}