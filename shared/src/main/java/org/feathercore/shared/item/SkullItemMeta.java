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

package org.feathercore.shared.item;

import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

public interface SkullItemMeta extends ItemMeta {

    /**
     * Sets the owner of the skull
     *
     * @deprecated Use {@link SkullItemMeta#setSkullProfile(GameProfile)}
     */
    void setSkullOwner(@Nullable String skullOwner);

    /**
     * Sets the owner of the skull
     */
    void setSkullProfile(@Nullable GameProfile skullProfile);

    /**
     * @return {@code true} if the skull owner is set, {@code false} otherwise
     */
    boolean hasOwner();

    /**
     * @return {@code GameProfile} of the skull, may be {@code null}
     */
    GameProfile getSkulProfile();

    /**
     * @see ItemMeta#clone()
     */
    SkullItemMeta clone();
}