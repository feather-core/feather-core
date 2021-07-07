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

import lombok.Builder;
import lombok.Data;
import net.md_5.bungee.api.chat.BaseComponent;
import org.feathercore.shared.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Builder(builderClassName = "Builder")
@Data
public class AdvancementDisplayInfo {

    @Nullable private final BaseComponent title;
    @Nullable private final BaseComponent description;
    private final ItemStack icon;
    @NotNull private final AdvancementFrameType frameType;
    private final boolean showToast;
    private final boolean announceToChat;
    private final boolean hidden;
    private final float x;
    private final float y;
}
