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

package org.feathercore.shared.particle;

import org.feathercore.shared.MinecraftNative;

/**
 * @author xtrafrancyz
 */
public interface Particle  extends MinecraftNative {
    /**
     * Gets the name of the particle
     *
     * @return name of the particle
     */
    String getName();

    /**
     * If the particle has additional arguments, such as color or block id, then this particle is complex.
     *
     * @return {@link true} if the particle is complex
     */
    boolean isComplex();
}
