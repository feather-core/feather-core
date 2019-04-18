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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.feathercore.shared.color.Color;
import org.feathercore.shared.color.MinecraftColor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
@Getter
public enum AdvancementFrameType {

    TASK("task", 0, MinecraftColor.GREEN),
    CHALLENGE("challenge", 26, MinecraftColor.PURPLE),
    GOAL("goal", 52, MinecraftColor.GREEN);

    private final String name;
    private final int icon;
    private final Color format;
}
