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

package org.feathercore.shared.advancements;

import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import org.feathercore.shared.MinecraftNative;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Advancement extends MinecraftNative {

    /**
     * Get the {@code Advancement} that is this {@code Advancement}'s parent,
     * may be {@code null}.
     *
     * @return the parent {@code Advancement} of this {@code Advancement},
     * or {@code null} if this is root one
     */
    @Nullable Advancement getParent();

    /**
     * Get information that defines this {@code Advancement}'s appearance in GUI,
     * may be {@code null}.
     *
     * @return {@code Advancement}'s display information
     */
    @Nullable AdvancementDisplayInfo getDisplayInfo();

    /**
     * Get how many requirements this {@code Advancement} has.
     *
     * @return requirements' count
     */
    int getRequirementCount();

    /**
     * Adds child {@code Advancement} to this {@code Advancement}
     */
    void addChild(@NonNull Advancement advancement);

    /**
     * Returns {@code BaseComponent} that is shown in the chat when this {@code Advancement} is completed.
     */
    @NotNull BaseComponent getCompletionText();

    /**
     * Creates a new {@code Advancement} with the data from this {@code Advancement}
     */
    Advancement clone();
}
