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

@SuppressWarnings("unused")
public enum MinecraftParticle implements Particle {
    AMBIENT_ENTITY_EFFECT("minecraft:ambient_entity_effect", 0),
    ANGRY_VILLAGER("minecraft:angry_villager", 1),
    BARRIER("minecraft:barrier", 2),
    BLOCK("minecraft:block", 3, true),
    BUBBLE("minecraft:bubble", 4),
    CLOUD("minecraft:cloud", 5),
    CRIT("minecraft:crit", 6),
    DAMAGE_INDICATOR("minecraft:damage_indicator", 7),
    DRAGON_BREATH("minecraft:dragon_breath", 8),
    DRIPPING_LAVA("minecraft:dripping_lava", 9),
    DRIPPING_WATER("minecraft:dripping_water", 10),
    DUST("minecraft:dust", 11, true),
    EFFECT("minecraft:effect", 12),
    ELDER_GUARDIAN("minecraft:elder_guardian", 13),
    ENCHANTED_HIT("minecraft:enchanted_hit", 14),
    ENCHANT("minecraft:enchant", 15),
    END_ROD("minecraft:end_rod", 16),
    ENTITY_EFFECT("minecraft:entity_effect", 17),
    EXPLOSION_EMITTER("minecraft:explosion_emitter", 18),
    EXPLOSION("minecraft:explosion", 19),
    FALLING_DUST("minecraft:falling_dust", 20, true),
    FIREWORK("minecraft:firework", 21),
    FISHING("minecraft:fishing", 22),
    FLAME("minecraft:flame", 23),
    HAPPY_VILLAGER("minecraft:happy_villager", 24),
    HEART("minecraft:heart", 25),
    INSTANT_EFFECT("minecraft:instant_effect", 26),
    ITEM("minecraft:item", 27, true),
    ITEM_SLIME("minecraft:item_slime", 28),
    ITEM_SNOWBALL("minecraft:item_snowball", 29),
    LARGE_SMOKE("minecraft:large_smoke", 30),
    LAVA("minecraft:lava", 31),
    MYCELIUM("minecraft:mycelium", 32),
    NOTE("minecraft:note", 33),
    POOF("minecraft:poof", 34),
    PORTAL("minecraft:portal", 35),
    RAIN("minecraft:rain", 36),
    SMOKE("minecraft:smoke", 37),
    SPIT("minecraft:spit", 38),
    SQUID_INK("minecraft:squid_ink", 39),
    SWEEP_ATTACK("minecraft:sweep_attack", 40),
    TOTEM_OF_UNDYING("minecraft:totem_of_undying", 41),
    UNDERWATER("minecraft:underwater", 42),
    SPLASH("minecraft:splash", 43),
    WITCH("minecraft:witch", 44),
    BUBBLE_POP("minecraft:bubble_pop", 45),
    CURRENT_DOWN("minecraft:current_down", 46),
    BUBBLE_COLUMN_UP("minecraft:bubble_column_up", 47),
    NAUTILUS("minecraft:nautilus", 48),
    DOLPHIN("minecraft:dolphin", 49);

    private final String name;
    private final int nativeId;
    private final boolean complex;

    MinecraftParticle(String name, int nativeId) {
        this(name, nativeId, false);
    }

    MinecraftParticle(String name, int nativeId, boolean complex) {
        this.name = name;
        this.nativeId = nativeId;
        this.complex = complex;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getNativeId() {
        return this.nativeId;
    }

    @Override
    public boolean isComplex() {
        return this.complex;
    }

}
