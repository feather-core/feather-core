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

package org.feathercore.shared.sound;

/**
 * @author xtrafrancyz
 */
public interface Sound {

    /**
     * Name of the sound. It usually consists of the <code>namespace:path</code>.
     *
     * @return name of the sound
     */
    String getName();

    /**
     * Gets the native id of sound used by Notchian client.
     *
     * @return native id
     */
    default int getNativeId() {
        return -1;
    }

    /**
     * @return {@link true} if this sound should be present in client and {@link false} otherwise
     */
    boolean isNative();
}
