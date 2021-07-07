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

package org.feathercore.shared.color;

import org.feathercore.shared.MinecraftNative;

public interface Color extends MinecraftNative {

    /**
     * Gets color name
     *
     * @return color's name
     */
    String getName();

    /**
     * Returns wool data for this color
     *
     * @return wool data
     */
    int getWoolData();

    /**
     * Returns dye data for this color
     *
     * @return wool data
     */
    int getDyeData();

    /**
     * Returns RGB representation for this color
     *
     * @return RGB color representation
     */
    int getColorRGB();

    /**
     * Returns a color for firework
     *
     * @return firework's color
     */
    int getFireworkColor();

    /**
     * Returns {@link String} color format
     *
     * @return {@link String} color's format
     */
    String getFormat();

    /**
     * Represents chat format code
     *
     * @return chat format code
     */
    int getChatCode();

    /**
     * Represents a simple char code
     *
     * @return char code
     */
    char getCharCode();
}
