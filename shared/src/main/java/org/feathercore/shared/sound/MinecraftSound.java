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

public enum MinecraftSound implements Sound {
    AMBIENT_CAVE("ambient.cave", 0),
    AMBIENT_UNDERWATER_ENTER("ambient.underwater.enter", 1),
    AMBIENT_UNDERWATER_EXIT("ambient.underwater.exit", 2),
    AMBIENT_UNDERWATER_LOOP("ambient.underwater.loop", 3),
    AMBIENT_UNDERWATER_LOOP_ADDITIONS("ambient.underwater.loop.additions", 4),
    AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE("ambient.underwater.loop.additions.rare", 5),
    AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE("ambient.underwater.loop.additions.ultra_rare", 6),
    BLOCK_ANVIL_BREAK("block.anvil.break", 7),
    BLOCK_ANVIL_DESTROY("block.anvil.destroy", 8),
    BLOCK_ANVIL_FALL("block.anvil.fall", 9),
    BLOCK_ANVIL_HIT("block.anvil.hit", 10),
    BLOCK_ANVIL_LAND("block.anvil.land", 11),
    BLOCK_ANVIL_PLACE("block.anvil.place", 12),
    BLOCK_ANVIL_STEP("block.anvil.step", 13),
    BLOCK_ANVIL_USE("block.anvil.use", 14),
    BLOCK_BEACON_ACTIVATE("block.beacon.activate", 15),
    BLOCK_BEACON_AMBIENT("block.beacon.ambient", 16),
    BLOCK_BEACON_DEACTIVATE("block.beacon.deactivate", 17),
    BLOCK_BEACON_POWER_SELECT("block.beacon.power_select", 18),
    BLOCK_BREWING_STAND_BREW("block.brewing_stand.brew", 19),
    BLOCK_BUBBLE_COLUMN_BUBBLE_POP("block.bubble_column.bubble_pop", 20),
    BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT("block.bubble_column.upwards_ambient", 21),
    BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE("block.bubble_column.upwards_inside", 22),
    BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT("block.bubble_column.whirlpool_ambient", 23),
    BLOCK_BUBBLE_COLUMN_WHIRLPOOL_INSIDE("block.bubble_column.whirlpool_inside", 24),
    BLOCK_CHEST_CLOSE("block.chest.close", 25),
    BLOCK_CHEST_LOCKED("block.chest.locked", 26),
    BLOCK_CHEST_OPEN("block.chest.open", 27),
    BLOCK_CHORUS_FLOWER_DEATH("block.chorus_flower.death", 28),
    BLOCK_CHORUS_FLOWER_GROW("block.chorus_flower.grow", 29),
    BLOCK_COMPARATOR_CLICK("block.comparator.click", 35),
    BLOCK_CONDUIT_ACTIVATE("block.conduit.activate", 36),
    BLOCK_CONDUIT_AMBIENT("block.conduit.ambient", 37),
    BLOCK_CONDUIT_AMBIENT_SHORT("block.conduit.ambient.short", 38),
    BLOCK_CONDUIT_ATTACK_TARGET("block.conduit.attack.target", 39),
    BLOCK_CONDUIT_DEACTIVATE("block.conduit.deactivate", 40),
    BLOCK_CORAL_BLOCK_BREAK("block.coral_block.break", 70),
    BLOCK_CORAL_BLOCK_FALL("block.coral_block.fall", 71),
    BLOCK_CORAL_BLOCK_HIT("block.coral_block.hit", 72),
    BLOCK_CORAL_BLOCK_PLACE("block.coral_block.place", 73),
    BLOCK_CORAL_BLOCK_STEP("block.coral_block.step", 74),
    BLOCK_DISPENSER_DISPENSE("block.dispenser.dispense", 41),
    BLOCK_DISPENSER_FAIL("block.dispenser.fail", 42),
    BLOCK_DISPENSER_LAUNCH("block.dispenser.launch", 43),
    BLOCK_ENCHANTMENT_TABLE_USE("block.enchantment_table.use", 44),
    BLOCK_END_GATEWAY_SPAWN("block.end_gateway.spawn", 45),
    BLOCK_END_PORTAL_SPAWN("block.end_portal.spawn", 46),
    BLOCK_END_PORTAL_FRAME_FILL("block.end_portal_frame.fill", 47),
    BLOCK_ENDER_CHEST_CLOSE("block.ender_chest.close", 48),
    BLOCK_ENDER_CHEST_OPEN("block.ender_chest.open", 49),
    BLOCK_FENCE_GATE_CLOSE("block.fence_gate.close", 50),
    BLOCK_FENCE_GATE_OPEN("block.fence_gate.open", 51),
    BLOCK_FIRE_AMBIENT("block.fire.ambient", 52),
    BLOCK_FIRE_EXTINGUISH("block.fire.extinguish", 53),
    BLOCK_FURNACE_FIRE_CRACKLE("block.furnace.fire_crackle", 54),
    BLOCK_GLASS_BREAK("block.glass.break", 55),
    BLOCK_GLASS_FALL("block.glass.fall", 56),
    BLOCK_GLASS_HIT("block.glass.hit", 57),
    BLOCK_GLASS_PLACE("block.glass.place", 58),
    BLOCK_GLASS_STEP("block.glass.step", 59),
    BLOCK_GRASS_BREAK("block.grass.break", 60),
    BLOCK_GRASS_FALL("block.grass.fall", 61),
    BLOCK_GRASS_HIT("block.grass.hit", 62),
    BLOCK_GRASS_PLACE("block.grass.place", 63),
    BLOCK_GRASS_STEP("block.grass.step", 64),
    BLOCK_GRAVEL_BREAK("block.gravel.break", 75),
    BLOCK_GRAVEL_FALL("block.gravel.fall", 76),
    BLOCK_GRAVEL_HIT("block.gravel.hit", 77),
    BLOCK_GRAVEL_PLACE("block.gravel.place", 78),
    BLOCK_GRAVEL_STEP("block.gravel.step", 79),
    BLOCK_IRON_DOOR_CLOSE("block.iron_door.close", 80),
    BLOCK_IRON_DOOR_OPEN("block.iron_door.open", 81),
    BLOCK_IRON_TRAPDOOR_CLOSE("block.iron_trapdoor.close", 82),
    BLOCK_IRON_TRAPDOOR_OPEN("block.iron_trapdoor.open", 83),
    BLOCK_LADDER_BREAK("block.ladder.break", 84),
    BLOCK_LADDER_FALL("block.ladder.fall", 85),
    BLOCK_LADDER_HIT("block.ladder.hit", 86),
    BLOCK_LADDER_PLACE("block.ladder.place", 87),
    BLOCK_LADDER_STEP("block.ladder.step", 88),
    BLOCK_LAVA_AMBIENT("block.lava.ambient", 89),
    BLOCK_LAVA_EXTINGUISH("block.lava.extinguish", 90),
    BLOCK_LAVA_POP("block.lava.pop", 91),
    BLOCK_LEVER_CLICK("block.lever.click", 92),
    BLOCK_LILY_PAD_PLACE("block.lily_pad.place", 149),
    BLOCK_METAL_BREAK("block.metal.break", 93),
    BLOCK_METAL_FALL("block.metal.fall", 94),
    BLOCK_METAL_HIT("block.metal.hit", 95),
    BLOCK_METAL_PLACE("block.metal.place", 96),
    BLOCK_METAL_STEP("block.metal.step", 97),
    BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF("block.metal_pressure_plate.click_off", 98),
    BLOCK_METAL_PRESSURE_PLATE_CLICK_ON("block.metal_pressure_plate.click_on", 99),
    BLOCK_NOTE_BLOCK_BASEDRUM("block.note_block.basedrum", 100),
    BLOCK_NOTE_BLOCK_BASS("block.note_block.bass", 101),
    BLOCK_NOTE_BLOCK_BELL("block.note_block.bell", 102),
    BLOCK_NOTE_BLOCK_CHIME("block.note_block.chime", 103),
    BLOCK_NOTE_BLOCK_FLUTE("block.note_block.flute", 104),
    BLOCK_NOTE_BLOCK_GUITAR("block.note_block.guitar", 105),
    BLOCK_NOTE_BLOCK_HARP("block.note_block.harp", 106),
    BLOCK_NOTE_BLOCK_HAT("block.note_block.hat", 107),
    BLOCK_NOTE_BLOCK_PLING("block.note_block.pling", 108),
    BLOCK_NOTE_BLOCK_SNARE("block.note_block.snare", 109),
    BLOCK_NOTE_BLOCK_XYLOPHONE("block.note_block.xylophone", 110),
    BLOCK_PISTON_CONTRACT("block.piston.contract", 111),
    BLOCK_PISTON_EXTEND("block.piston.extend", 112),
    BLOCK_PORTAL_AMBIENT("block.portal.ambient", 113),
    BLOCK_PORTAL_TRAVEL("block.portal.travel", 114),
    BLOCK_PORTAL_TRIGGER("block.portal.trigger", 115),
    BLOCK_PUMPKIN_CARVE("block.pumpkin.carve", 116),
    BLOCK_REDSTONE_TORCH_BURNOUT("block.redstone_torch.burnout", 117),
    BLOCK_SAND_BREAK("block.sand.break", 118),
    BLOCK_SAND_FALL("block.sand.fall", 119),
    BLOCK_SAND_HIT("block.sand.hit", 120),
    BLOCK_SAND_PLACE("block.sand.place", 121),
    BLOCK_SAND_STEP("block.sand.step", 122),
    BLOCK_SHULKER_BOX_CLOSE("block.shulker_box.close", 123),
    BLOCK_SHULKER_BOX_OPEN("block.shulker_box.open", 124),
    BLOCK_SLIME_BLOCK_BREAK("block.slime_block.break", 125),
    BLOCK_SLIME_BLOCK_FALL("block.slime_block.fall", 126),
    BLOCK_SLIME_BLOCK_HIT("block.slime_block.hit", 127),
    BLOCK_SLIME_BLOCK_PLACE("block.slime_block.place", 128),
    BLOCK_SLIME_BLOCK_STEP("block.slime_block.step", 129),
    BLOCK_SNOW_BREAK("block.snow.break", 130),
    BLOCK_SNOW_FALL("block.snow.fall", 131),
    BLOCK_SNOW_HIT("block.snow.hit", 132),
    BLOCK_SNOW_PLACE("block.snow.place", 133),
    BLOCK_SNOW_STEP("block.snow.step", 134),
    BLOCK_STONE_BREAK("block.stone.break", 135),
    BLOCK_STONE_FALL("block.stone.fall", 136),
    BLOCK_STONE_HIT("block.stone.hit", 137),
    BLOCK_STONE_PLACE("block.stone.place", 138),
    BLOCK_STONE_STEP("block.stone.step", 139),
    BLOCK_STONE_BUTTON_CLICK_OFF("block.stone_button.click_off", 140),
    BLOCK_STONE_BUTTON_CLICK_ON("block.stone_button.click_on", 141),
    BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF("block.stone_pressure_plate.click_off", 142),
    BLOCK_STONE_PRESSURE_PLATE_CLICK_ON("block.stone_pressure_plate.click_on", 143),
    BLOCK_TRIPWIRE_ATTACH("block.tripwire.attach", 144),
    BLOCK_TRIPWIRE_CLICK_OFF("block.tripwire.click_off", 145),
    BLOCK_TRIPWIRE_CLICK_ON("block.tripwire.click_on", 146),
    BLOCK_TRIPWIRE_DETACH("block.tripwire.detach", 147),
    BLOCK_WATER_AMBIENT("block.water.ambient", 148),
    BLOCK_WET_GRASS_BREAK("block.wet_grass.break", 65),
    BLOCK_WET_GRASS_FALL("block.wet_grass.fall", 66),
    BLOCK_WET_GRASS_HIT("block.wet_grass.hit", 67),
    BLOCK_WET_GRASS_PLACE("block.wet_grass.place", 68),
    BLOCK_WET_GRASS_STEP("block.wet_grass.step", 69),
    BLOCK_WOOD_BREAK("block.wood.break", 150),
    BLOCK_WOOD_FALL("block.wood.fall", 151),
    BLOCK_WOOD_HIT("block.wood.hit", 152),
    BLOCK_WOOD_PLACE("block.wood.place", 153),
    BLOCK_WOOD_STEP("block.wood.step", 154),
    BLOCK_WOODEN_BUTTON_CLICK_OFF("block.wooden_button.click_off", 155),
    BLOCK_WOODEN_BUTTON_CLICK_ON("block.wooden_button.click_on", 156),
    BLOCK_WOODEN_DOOR_CLOSE("block.wooden_door.close", 159),
    BLOCK_WOODEN_DOOR_OPEN("block.wooden_door.open", 160),
    BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF("block.wooden_pressure_plate.click_off", 157),
    BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON("block.wooden_pressure_plate.click_on", 158),
    BLOCK_WOODEN_TRAPDOOR_CLOSE("block.wooden_trapdoor.close", 161),
    BLOCK_WOODEN_TRAPDOOR_OPEN("block.wooden_trapdoor.open", 162),
    BLOCK_WOOL_BREAK("block.wool.break", 30),
    BLOCK_WOOL_FALL("block.wool.fall", 31),
    BLOCK_WOOL_HIT("block.wool.hit", 32),
    BLOCK_WOOL_PLACE("block.wool.place", 33),
    BLOCK_WOOL_STEP("block.wool.step", 34),
    ENCHANT_THORNS_HIT("enchant.thorns.hit", 163),
    ENTITY_ARMOR_STAND_BREAK("entity.armor_stand.break", 164),
    ENTITY_ARMOR_STAND_FALL("entity.armor_stand.fall", 165),
    ENTITY_ARMOR_STAND_HIT("entity.armor_stand.hit", 166),
    ENTITY_ARMOR_STAND_PLACE("entity.armor_stand.place", 167),
    ENTITY_ARROW_HIT("entity.arrow.hit", 168),
    ENTITY_ARROW_HIT_PLAYER("entity.arrow.hit_player", 169),
    ENTITY_ARROW_SHOOT("entity.arrow.shoot", 170),
    ENTITY_BAT_AMBIENT("entity.bat.ambient", 171),
    ENTITY_BAT_DEATH("entity.bat.death", 172),
    ENTITY_BAT_HURT("entity.bat.hurt", 173),
    ENTITY_BAT_LOOP("entity.bat.loop", 174),
    ENTITY_BAT_TAKEOFF("entity.bat.takeoff", 175),
    ENTITY_BLAZE_AMBIENT("entity.blaze.ambient", 176),
    ENTITY_BLAZE_BURN("entity.blaze.burn", 177),
    ENTITY_BLAZE_DEATH("entity.blaze.death", 178),
    ENTITY_BLAZE_HURT("entity.blaze.hurt", 179),
    ENTITY_BLAZE_SHOOT("entity.blaze.shoot", 180),
    ENTITY_BOAT_PADDLE_LAND("entity.boat.paddle_land", 181),
    ENTITY_BOAT_PADDLE_WATER("entity.boat.paddle_water", 182),
    ENTITY_CAT_AMBIENT("entity.cat.ambient", 186),
    ENTITY_CAT_DEATH("entity.cat.death", 187),
    ENTITY_CAT_HISS("entity.cat.hiss", 188),
    ENTITY_CAT_HURT("entity.cat.hurt", 189),
    ENTITY_CAT_PURR("entity.cat.purr", 190),
    ENTITY_CAT_PURREOW("entity.cat.purreow", 191),
    ENTITY_CHICKEN_AMBIENT("entity.chicken.ambient", 192),
    ENTITY_CHICKEN_DEATH("entity.chicken.death", 193),
    ENTITY_CHICKEN_EGG("entity.chicken.egg", 194),
    ENTITY_CHICKEN_HURT("entity.chicken.hurt", 195),
    ENTITY_CHICKEN_STEP("entity.chicken.step", 196),
    ENTITY_COD_AMBIENT("entity.cod.ambient", 197),
    ENTITY_COD_DEATH("entity.cod.death", 198),
    ENTITY_COD_FLOP("entity.cod.flop", 199),
    ENTITY_COD_HURT("entity.cod.hurt", 200),
    ENTITY_COW_AMBIENT("entity.cow.ambient", 201),
    ENTITY_COW_DEATH("entity.cow.death", 202),
    ENTITY_COW_HURT("entity.cow.hurt", 203),
    ENTITY_COW_MILK("entity.cow.milk", 204),
    ENTITY_COW_STEP("entity.cow.step", 205),
    ENTITY_CREEPER_DEATH("entity.creeper.death", 206),
    ENTITY_CREEPER_HURT("entity.creeper.hurt", 207),
    ENTITY_CREEPER_PRIMED("entity.creeper.primed", 208),
    ENTITY_DOLPHIN_AMBIENT("entity.dolphin.ambient", 209),
    ENTITY_DOLPHIN_AMBIENT_WATER("entity.dolphin.ambient_water", 210),
    ENTITY_DOLPHIN_ATTACK("entity.dolphin.attack", 211),
    ENTITY_DOLPHIN_DEATH("entity.dolphin.death", 212),
    ENTITY_DOLPHIN_EAT("entity.dolphin.eat", 213),
    ENTITY_DOLPHIN_HURT("entity.dolphin.hurt", 214),
    ENTITY_DOLPHIN_JUMP("entity.dolphin.jump", 215),
    ENTITY_DOLPHIN_PLAY("entity.dolphin.play", 216),
    ENTITY_DOLPHIN_SPLASH("entity.dolphin.splash", 217),
    ENTITY_DOLPHIN_SWIM("entity.dolphin.swim", 218),
    ENTITY_DONKEY_AMBIENT("entity.donkey.ambient", 219),
    ENTITY_DONKEY_ANGRY("entity.donkey.angry", 220),
    ENTITY_DONKEY_CHEST("entity.donkey.chest", 221),
    ENTITY_DONKEY_DEATH("entity.donkey.death", 222),
    ENTITY_DONKEY_HURT("entity.donkey.hurt", 223),
    ENTITY_DRAGON_FIREBALL_EXPLODE("entity.dragon_fireball.explode", 248),
    ENTITY_DROWNED_AMBIENT("entity.drowned.ambient", 224),
    ENTITY_DROWNED_AMBIENT_WATER("entity.drowned.ambient_water", 225),
    ENTITY_DROWNED_DEATH("entity.drowned.death", 226),
    ENTITY_DROWNED_DEATH_WATER("entity.drowned.death_water", 227),
    ENTITY_DROWNED_HURT("entity.drowned.hurt", 228),
    ENTITY_DROWNED_HURT_WATER("entity.drowned.hurt_water", 229),
    ENTITY_DROWNED_SHOOT("entity.drowned.shoot", 230),
    ENTITY_DROWNED_STEP("entity.drowned.step", 231),
    ENTITY_DROWNED_SWIM("entity.drowned.swim", 232),
    ENTITY_EGG_THROW("entity.egg.throw", 233),
    ENTITY_ELDER_GUARDIAN_AMBIENT("entity.elder_guardian.ambient", 234),
    ENTITY_ELDER_GUARDIAN_AMBIENT_LAND("entity.elder_guardian.ambient_land", 235),
    ENTITY_ELDER_GUARDIAN_CURSE("entity.elder_guardian.curse", 236),
    ENTITY_ELDER_GUARDIAN_DEATH("entity.elder_guardian.death", 237),
    ENTITY_ELDER_GUARDIAN_DEATH_LAND("entity.elder_guardian.death_land", 238),
    ENTITY_ELDER_GUARDIAN_FLOP("entity.elder_guardian.flop", 239),
    ENTITY_ELDER_GUARDIAN_HURT("entity.elder_guardian.hurt", 240),
    ENTITY_ELDER_GUARDIAN_HURT_LAND("entity.elder_guardian.hurt_land", 241),
    ENTITY_ENDER_DRAGON_AMBIENT("entity.ender_dragon.ambient", 242),
    ENTITY_ENDER_DRAGON_DEATH("entity.ender_dragon.death", 243),
    ENTITY_ENDER_DRAGON_FLAP("entity.ender_dragon.flap", 244),
    ENTITY_ENDER_DRAGON_GROWL("entity.ender_dragon.growl", 245),
    ENTITY_ENDER_DRAGON_HURT("entity.ender_dragon.hurt", 246),
    ENTITY_ENDER_DRAGON_SHOOT("entity.ender_dragon.shoot", 247),
    ENTITY_ENDER_EYE_DEATH("entity.ender_eye.death", 249),
    ENTITY_ENDER_EYE_LAUNCH("entity.ender_eye.launch", 250),
    ENTITY_ENDER_PEARL_THROW("entity.ender_pearl.throw", 261),
    ENTITY_ENDERMAN_AMBIENT("entity.enderman.ambient", 251),
    ENTITY_ENDERMAN_DEATH("entity.enderman.death", 252),
    ENTITY_ENDERMAN_HURT("entity.enderman.hurt", 253),
    ENTITY_ENDERMAN_SCREAM("entity.enderman.scream", 254),
    ENTITY_ENDERMAN_STARE("entity.enderman.stare", 255),
    ENTITY_ENDERMAN_TELEPORT("entity.enderman.teleport", 256),
    ENTITY_ENDERMITE_AMBIENT("entity.endermite.ambient", 257),
    ENTITY_ENDERMITE_DEATH("entity.endermite.death", 258),
    ENTITY_ENDERMITE_HURT("entity.endermite.hurt", 259),
    ENTITY_ENDERMITE_STEP("entity.endermite.step", 260),
    ENTITY_EVOKER_AMBIENT("entity.evoker.ambient", 262),
    ENTITY_EVOKER_CAST_SPELL("entity.evoker.cast_spell", 263),
    ENTITY_EVOKER_DEATH("entity.evoker.death", 264),
    ENTITY_EVOKER_HURT("entity.evoker.hurt", 265),
    ENTITY_EVOKER_PREPARE_ATTACK("entity.evoker.prepare_attack", 266),
    ENTITY_EVOKER_PREPARE_SUMMON("entity.evoker.prepare_summon", 267),
    ENTITY_EVOKER_PREPARE_WOLOLO("entity.evoker.prepare_wololo", 268),
    ENTITY_EVOKER_FANGS_ATTACK("entity.evoker_fangs.attack", 269),
    ENTITY_EXPERIENCE_BOTTLE_THROW("entity.experience_bottle.throw", 270),
    ENTITY_EXPERIENCE_ORB_PICKUP("entity.experience_orb.pickup", 271),
    ENTITY_FIREWORK_ROCKET_BLAST("entity.firework_rocket.blast", 272),
    ENTITY_FIREWORK_ROCKET_BLAST_FAR("entity.firework_rocket.blast_far", 273),
    ENTITY_FIREWORK_ROCKET_LARGE_BLAST("entity.firework_rocket.large_blast", 274),
    ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR("entity.firework_rocket.large_blast_far", 275),
    ENTITY_FIREWORK_ROCKET_LAUNCH("entity.firework_rocket.launch", 276),
    ENTITY_FIREWORK_ROCKET_SHOOT("entity.firework_rocket.shoot", 277),
    ENTITY_FIREWORK_ROCKET_TWINKLE("entity.firework_rocket.twinkle", 278),
    ENTITY_FIREWORK_ROCKET_TWINKLE_FAR("entity.firework_rocket.twinkle_far", 279),
    ENTITY_FISH_SWIM("entity.fish.swim", 280),
    ENTITY_FISHING_BOBBER_RETRIEVE("entity.fishing_bobber.retrieve", 183),
    ENTITY_FISHING_BOBBER_SPLASH("entity.fishing_bobber.splash", 184),
    ENTITY_FISHING_BOBBER_THROW("entity.fishing_bobber.throw", 185),
    ENTITY_GENERIC_BIG_FALL("entity.generic.big_fall", 281),
    ENTITY_GENERIC_BURN("entity.generic.burn", 282),
    ENTITY_GENERIC_DEATH("entity.generic.death", 283),
    ENTITY_GENERIC_DRINK("entity.generic.drink", 284),
    ENTITY_GENERIC_EAT("entity.generic.eat", 285),
    ENTITY_GENERIC_EXPLODE("entity.generic.explode", 286),
    ENTITY_GENERIC_EXTINGUISH_FIRE("entity.generic.extinguish_fire", 287),
    ENTITY_GENERIC_HURT("entity.generic.hurt", 288),
    ENTITY_GENERIC_SMALL_FALL("entity.generic.small_fall", 289),
    ENTITY_GENERIC_SPLASH("entity.generic.splash", 290),
    ENTITY_GENERIC_SWIM("entity.generic.swim", 291),
    ENTITY_GHAST_AMBIENT("entity.ghast.ambient", 292),
    ENTITY_GHAST_DEATH("entity.ghast.death", 293),
    ENTITY_GHAST_HURT("entity.ghast.hurt", 294),
    ENTITY_GHAST_SCREAM("entity.ghast.scream", 295),
    ENTITY_GHAST_SHOOT("entity.ghast.shoot", 296),
    ENTITY_GHAST_WARN("entity.ghast.warn", 297),
    ENTITY_GUARDIAN_AMBIENT("entity.guardian.ambient", 298),
    ENTITY_GUARDIAN_AMBIENT_LAND("entity.guardian.ambient_land", 299),
    ENTITY_GUARDIAN_ATTACK("entity.guardian.attack", 300),
    ENTITY_GUARDIAN_DEATH("entity.guardian.death", 301),
    ENTITY_GUARDIAN_DEATH_LAND("entity.guardian.death_land", 302),
    ENTITY_GUARDIAN_FLOP("entity.guardian.flop", 303),
    ENTITY_GUARDIAN_HURT("entity.guardian.hurt", 304),
    ENTITY_GUARDIAN_HURT_LAND("entity.guardian.hurt_land", 305),
    ENTITY_HORSE_AMBIENT("entity.horse.ambient", 306),
    ENTITY_HORSE_ANGRY("entity.horse.angry", 307),
    ENTITY_HORSE_ARMOR("entity.horse.armor", 308),
    ENTITY_HORSE_BREATHE("entity.horse.breathe", 309),
    ENTITY_HORSE_DEATH("entity.horse.death", 310),
    ENTITY_HORSE_EAT("entity.horse.eat", 311),
    ENTITY_HORSE_GALLOP("entity.horse.gallop", 312),
    ENTITY_HORSE_HURT("entity.horse.hurt", 313),
    ENTITY_HORSE_JUMP("entity.horse.jump", 314),
    ENTITY_HORSE_LAND("entity.horse.land", 315),
    ENTITY_HORSE_SADDLE("entity.horse.saddle", 316),
    ENTITY_HORSE_STEP("entity.horse.step", 317),
    ENTITY_HORSE_STEP_WOOD("entity.horse.step_wood", 318),
    ENTITY_HOSTILE_BIG_FALL("entity.hostile.big_fall", 319),
    ENTITY_HOSTILE_DEATH("entity.hostile.death", 320),
    ENTITY_HOSTILE_HURT("entity.hostile.hurt", 321),
    ENTITY_HOSTILE_SMALL_FALL("entity.hostile.small_fall", 322),
    ENTITY_HOSTILE_SPLASH("entity.hostile.splash", 323),
    ENTITY_HOSTILE_SWIM("entity.hostile.swim", 324),
    ENTITY_HUSK_AMBIENT("entity.husk.ambient", 325),
    ENTITY_HUSK_CONVERTED_TO_ZOMBIE("entity.husk.converted_to_zombie", 326),
    ENTITY_HUSK_DEATH("entity.husk.death", 327),
    ENTITY_HUSK_HURT("entity.husk.hurt", 328),
    ENTITY_HUSK_STEP("entity.husk.step", 329),
    ENTITY_ILLUSIONER_AMBIENT("entity.illusioner.ambient", 330),
    ENTITY_ILLUSIONER_CAST_SPELL("entity.illusioner.cast_spell", 331),
    ENTITY_ILLUSIONER_DEATH("entity.illusioner.death", 332),
    ENTITY_ILLUSIONER_HURT("entity.illusioner.hurt", 333),
    ENTITY_ILLUSIONER_MIRROR_MOVE("entity.illusioner.mirror_move", 334),
    ENTITY_ILLUSIONER_PREPARE_BLINDNESS("entity.illusioner.prepare_blindness", 335),
    ENTITY_ILLUSIONER_PREPARE_MIRROR("entity.illusioner.prepare_mirror", 336),
    ENTITY_IRON_GOLEM_ATTACK("entity.iron_golem.attack", 337),
    ENTITY_IRON_GOLEM_DEATH("entity.iron_golem.death", 338),
    ENTITY_IRON_GOLEM_HURT("entity.iron_golem.hurt", 339),
    ENTITY_IRON_GOLEM_STEP("entity.iron_golem.step", 340),
    ENTITY_ITEM_BREAK("entity.item.break", 341),
    ENTITY_ITEM_PICKUP("entity.item.pickup", 342),
    ENTITY_ITEM_FRAME_ADD_ITEM("entity.item_frame.add_item", 343),
    ENTITY_ITEM_FRAME_BREAK("entity.item_frame.break", 344),
    ENTITY_ITEM_FRAME_PLACE("entity.item_frame.place", 345),
    ENTITY_ITEM_FRAME_REMOVE_ITEM("entity.item_frame.remove_item", 346),
    ENTITY_ITEM_FRAME_ROTATE_ITEM("entity.item_frame.rotate_item", 347),
    ENTITY_LEASH_KNOT_BREAK("entity.leash_knot.break", 348),
    ENTITY_LEASH_KNOT_PLACE("entity.leash_knot.place", 349),
    ENTITY_LIGHTNING_BOLT_IMPACT("entity.lightning_bolt.impact", 350),
    ENTITY_LIGHTNING_BOLT_THUNDER("entity.lightning_bolt.thunder", 351),
    ENTITY_LINGERING_POTION_THROW("entity.lingering_potion.throw", 352),
    ENTITY_LLAMA_AMBIENT("entity.llama.ambient", 353),
    ENTITY_LLAMA_ANGRY("entity.llama.angry", 354),
    ENTITY_LLAMA_CHEST("entity.llama.chest", 355),
    ENTITY_LLAMA_DEATH("entity.llama.death", 356),
    ENTITY_LLAMA_EAT("entity.llama.eat", 357),
    ENTITY_LLAMA_HURT("entity.llama.hurt", 358),
    ENTITY_LLAMA_SPIT("entity.llama.spit", 359),
    ENTITY_LLAMA_STEP("entity.llama.step", 360),
    ENTITY_LLAMA_SWAG("entity.llama.swag", 361),
    ENTITY_MAGMA_CUBE_DEATH("entity.magma_cube.death", 362),
    ENTITY_MAGMA_CUBE_DEATH_SMALL("entity.magma_cube.death_small", 498),
    ENTITY_MAGMA_CUBE_HURT("entity.magma_cube.hurt", 363),
    ENTITY_MAGMA_CUBE_HURT_SMALL("entity.magma_cube.hurt_small", 499),
    ENTITY_MAGMA_CUBE_JUMP("entity.magma_cube.jump", 364),
    ENTITY_MAGMA_CUBE_SQUISH("entity.magma_cube.squish", 365),
    ENTITY_MAGMA_CUBE_SQUISH_SMALL("entity.magma_cube.squish_small", 500),
    ENTITY_MINECART_INSIDE("entity.minecart.inside", 366),
    ENTITY_MINECART_RIDING("entity.minecart.riding", 367),
    ENTITY_MOOSHROOM_SHEAR("entity.mooshroom.shear", 368),
    ENTITY_MULE_AMBIENT("entity.mule.ambient", 369),
    ENTITY_MULE_CHEST("entity.mule.chest", 370),
    ENTITY_MULE_DEATH("entity.mule.death", 371),
    ENTITY_MULE_HURT("entity.mule.hurt", 372),
    ENTITY_PAINTING_BREAK("entity.painting.break", 373),
    ENTITY_PAINTING_PLACE("entity.painting.place", 374),
    ENTITY_PARROT_AMBIENT("entity.parrot.ambient", 375),
    ENTITY_PARROT_DEATH("entity.parrot.death", 376),
    ENTITY_PARROT_EAT("entity.parrot.eat", 377),
    ENTITY_PARROT_FLY("entity.parrot.fly", 378),
    ENTITY_PARROT_HURT("entity.parrot.hurt", 379),
    ENTITY_PARROT_IMITATE_BLAZE("entity.parrot.imitate.blaze", 380),
    ENTITY_PARROT_IMITATE_CREEPER("entity.parrot.imitate.creeper", 381),
    ENTITY_PARROT_IMITATE_DROWNED("entity.parrot.imitate.drowned", 382),
    ENTITY_PARROT_IMITATE_ELDER_GUARDIAN("entity.parrot.imitate.elder_guardian", 383),
    ENTITY_PARROT_IMITATE_ENDER_DRAGON("entity.parrot.imitate.ender_dragon", 384),
    ENTITY_PARROT_IMITATE_ENDERMAN("entity.parrot.imitate.enderman", 385),
    ENTITY_PARROT_IMITATE_ENDERMITE("entity.parrot.imitate.endermite", 386),
    ENTITY_PARROT_IMITATE_EVOKER("entity.parrot.imitate.evoker", 387),
    ENTITY_PARROT_IMITATE_GHAST("entity.parrot.imitate.ghast", 388),
    ENTITY_PARROT_IMITATE_HUSK("entity.parrot.imitate.husk", 389),
    ENTITY_PARROT_IMITATE_ILLUSIONER("entity.parrot.imitate.illusioner", 390),
    ENTITY_PARROT_IMITATE_MAGMA_CUBE("entity.parrot.imitate.magma_cube", 391),
    ENTITY_PARROT_IMITATE_PHANTOM("entity.parrot.imitate.phantom", 392),
    ENTITY_PARROT_IMITATE_POLAR_BEAR("entity.parrot.imitate.polar_bear", 393),
    ENTITY_PARROT_IMITATE_SHULKER("entity.parrot.imitate.shulker", 394),
    ENTITY_PARROT_IMITATE_SILVERFISH("entity.parrot.imitate.silverfish", 395),
    ENTITY_PARROT_IMITATE_SKELETON("entity.parrot.imitate.skeleton", 396),
    ENTITY_PARROT_IMITATE_SLIME("entity.parrot.imitate.slime", 397),
    ENTITY_PARROT_IMITATE_SPIDER("entity.parrot.imitate.spider", 398),
    ENTITY_PARROT_IMITATE_STRAY("entity.parrot.imitate.stray", 399),
    ENTITY_PARROT_IMITATE_VEX("entity.parrot.imitate.vex", 400),
    ENTITY_PARROT_IMITATE_VINDICATOR("entity.parrot.imitate.vindicator", 401),
    ENTITY_PARROT_IMITATE_WITCH("entity.parrot.imitate.witch", 402),
    ENTITY_PARROT_IMITATE_WITHER("entity.parrot.imitate.wither", 403),
    ENTITY_PARROT_IMITATE_WITHER_SKELETON("entity.parrot.imitate.wither_skeleton", 404),
    ENTITY_PARROT_IMITATE_WOLF("entity.parrot.imitate.wolf", 405),
    ENTITY_PARROT_IMITATE_ZOMBIE("entity.parrot.imitate.zombie", 406),
    ENTITY_PARROT_IMITATE_ZOMBIE_PIGMAN("entity.parrot.imitate.zombie_pigman", 407),
    ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER("entity.parrot.imitate.zombie_villager", 408),
    ENTITY_PARROT_STEP("entity.parrot.step", 409),
    ENTITY_PHANTOM_AMBIENT("entity.phantom.ambient", 410),
    ENTITY_PHANTOM_BITE("entity.phantom.bite", 411),
    ENTITY_PHANTOM_DEATH("entity.phantom.death", 412),
    ENTITY_PHANTOM_FLAP("entity.phantom.flap", 413),
    ENTITY_PHANTOM_HURT("entity.phantom.hurt", 414),
    ENTITY_PHANTOM_SWOOP("entity.phantom.swoop", 415),
    ENTITY_PIG_AMBIENT("entity.pig.ambient", 416),
    ENTITY_PIG_DEATH("entity.pig.death", 417),
    ENTITY_PIG_HURT("entity.pig.hurt", 418),
    ENTITY_PIG_SADDLE("entity.pig.saddle", 419),
    ENTITY_PIG_STEP("entity.pig.step", 420),
    ENTITY_PLAYER_ATTACK_CRIT("entity.player.attack.crit", 421),
    ENTITY_PLAYER_ATTACK_KNOCKBACK("entity.player.attack.knockback", 422),
    ENTITY_PLAYER_ATTACK_NODAMAGE("entity.player.attack.nodamage", 423),
    ENTITY_PLAYER_ATTACK_STRONG("entity.player.attack.strong", 424),
    ENTITY_PLAYER_ATTACK_SWEEP("entity.player.attack.sweep", 425),
    ENTITY_PLAYER_ATTACK_WEAK("entity.player.attack.weak", 426),
    ENTITY_PLAYER_BIG_FALL("entity.player.big_fall", 427),
    ENTITY_PLAYER_BREATH("entity.player.breath", 428),
    ENTITY_PLAYER_BURP("entity.player.burp", 429),
    ENTITY_PLAYER_DEATH("entity.player.death", 430),
    ENTITY_PLAYER_HURT("entity.player.hurt", 431),
    ENTITY_PLAYER_HURT_DROWN("entity.player.hurt_drown", 432),
    ENTITY_PLAYER_HURT_ON_FIRE("entity.player.hurt_on_fire", 433),
    ENTITY_PLAYER_LEVELUP("entity.player.levelup", 434),
    ENTITY_PLAYER_SMALL_FALL("entity.player.small_fall", 435),
    ENTITY_PLAYER_SPLASH("entity.player.splash", 436),
    ENTITY_PLAYER_SPLASH_HIGH_SPEED("entity.player.splash.high_speed", 437),
    ENTITY_PLAYER_SWIM("entity.player.swim", 438),
    ENTITY_POLAR_BEAR_AMBIENT("entity.polar_bear.ambient", 439),
    ENTITY_POLAR_BEAR_AMBIENT_BABY("entity.polar_bear.ambient_baby", 440),
    ENTITY_POLAR_BEAR_DEATH("entity.polar_bear.death", 441),
    ENTITY_POLAR_BEAR_HURT("entity.polar_bear.hurt", 442),
    ENTITY_POLAR_BEAR_STEP("entity.polar_bear.step", 443),
    ENTITY_POLAR_BEAR_WARNING("entity.polar_bear.warning", 444),
    ENTITY_PUFFER_FISH_AMBIENT("entity.puffer_fish.ambient", 445),
    ENTITY_PUFFER_FISH_BLOW_OUT("entity.puffer_fish.blow_out", 446),
    ENTITY_PUFFER_FISH_BLOW_UP("entity.puffer_fish.blow_up", 447),
    ENTITY_PUFFER_FISH_DEATH("entity.puffer_fish.death", 448),
    ENTITY_PUFFER_FISH_FLOP("entity.puffer_fish.flop", 449),
    ENTITY_PUFFER_FISH_HURT("entity.puffer_fish.hurt", 450),
    ENTITY_PUFFER_FISH_STING("entity.puffer_fish.sting", 451),
    ENTITY_RABBIT_AMBIENT("entity.rabbit.ambient", 452),
    ENTITY_RABBIT_ATTACK("entity.rabbit.attack", 453),
    ENTITY_RABBIT_DEATH("entity.rabbit.death", 454),
    ENTITY_RABBIT_HURT("entity.rabbit.hurt", 455),
    ENTITY_RABBIT_JUMP("entity.rabbit.jump", 456),
    ENTITY_SALMON_AMBIENT("entity.salmon.ambient", 457),
    ENTITY_SALMON_DEATH("entity.salmon.death", 458),
    ENTITY_SALMON_FLOP("entity.salmon.flop", 459),
    ENTITY_SALMON_HURT("entity.salmon.hurt", 460),
    ENTITY_SHEEP_AMBIENT("entity.sheep.ambient", 461),
    ENTITY_SHEEP_DEATH("entity.sheep.death", 462),
    ENTITY_SHEEP_HURT("entity.sheep.hurt", 463),
    ENTITY_SHEEP_SHEAR("entity.sheep.shear", 464),
    ENTITY_SHEEP_STEP("entity.sheep.step", 465),
    ENTITY_SHULKER_AMBIENT("entity.shulker.ambient", 466),
    ENTITY_SHULKER_CLOSE("entity.shulker.close", 467),
    ENTITY_SHULKER_DEATH("entity.shulker.death", 468),
    ENTITY_SHULKER_HURT("entity.shulker.hurt", 469),
    ENTITY_SHULKER_HURT_CLOSED("entity.shulker.hurt_closed", 470),
    ENTITY_SHULKER_OPEN("entity.shulker.open", 471),
    ENTITY_SHULKER_SHOOT("entity.shulker.shoot", 472),
    ENTITY_SHULKER_TELEPORT("entity.shulker.teleport", 473),
    ENTITY_SHULKER_BULLET_HIT("entity.shulker_bullet.hit", 474),
    ENTITY_SHULKER_BULLET_HURT("entity.shulker_bullet.hurt", 475),
    ENTITY_SILVERFISH_AMBIENT("entity.silverfish.ambient", 476),
    ENTITY_SILVERFISH_DEATH("entity.silverfish.death", 477),
    ENTITY_SILVERFISH_HURT("entity.silverfish.hurt", 478),
    ENTITY_SILVERFISH_STEP("entity.silverfish.step", 479),
    ENTITY_SKELETON_AMBIENT("entity.skeleton.ambient", 480),
    ENTITY_SKELETON_DEATH("entity.skeleton.death", 481),
    ENTITY_SKELETON_HURT("entity.skeleton.hurt", 482),
    ENTITY_SKELETON_SHOOT("entity.skeleton.shoot", 483),
    ENTITY_SKELETON_STEP("entity.skeleton.step", 484),
    ENTITY_SKELETON_HORSE_AMBIENT("entity.skeleton_horse.ambient", 485),
    ENTITY_SKELETON_HORSE_AMBIENT_WATER("entity.skeleton_horse.ambient_water", 489),
    ENTITY_SKELETON_HORSE_DEATH("entity.skeleton_horse.death", 486),
    ENTITY_SKELETON_HORSE_GALLOP_WATER("entity.skeleton_horse.gallop_water", 490),
    ENTITY_SKELETON_HORSE_HURT("entity.skeleton_horse.hurt", 487),
    ENTITY_SKELETON_HORSE_JUMP_WATER("entity.skeleton_horse.jump_water", 491),
    ENTITY_SKELETON_HORSE_STEP_WATER("entity.skeleton_horse.step_water", 492),
    ENTITY_SKELETON_HORSE_SWIM("entity.skeleton_horse.swim", 488),
    ENTITY_SLIME_ATTACK("entity.slime.attack", 493),
    ENTITY_SLIME_DEATH("entity.slime.death", 494),
    ENTITY_SLIME_DEATH_SMALL("entity.slime.death_small", 501),
    ENTITY_SLIME_HURT("entity.slime.hurt", 495),
    ENTITY_SLIME_HURT_SMALL("entity.slime.hurt_small", 502),
    ENTITY_SLIME_JUMP("entity.slime.jump", 496),
    ENTITY_SLIME_JUMP_SMALL("entity.slime.jump_small", 503),
    ENTITY_SLIME_SQUISH("entity.slime.squish", 497),
    ENTITY_SLIME_SQUISH_SMALL("entity.slime.squish_small", 504),
    ENTITY_SNOW_GOLEM_AMBIENT("entity.snow_golem.ambient", 505),
    ENTITY_SNOW_GOLEM_DEATH("entity.snow_golem.death", 506),
    ENTITY_SNOW_GOLEM_HURT("entity.snow_golem.hurt", 507),
    ENTITY_SNOW_GOLEM_SHOOT("entity.snow_golem.shoot", 508),
    ENTITY_SNOWBALL_THROW("entity.snowball.throw", 509),
    ENTITY_SPIDER_AMBIENT("entity.spider.ambient", 510),
    ENTITY_SPIDER_DEATH("entity.spider.death", 511),
    ENTITY_SPIDER_HURT("entity.spider.hurt", 512),
    ENTITY_SPIDER_STEP("entity.spider.step", 513),
    ENTITY_SPLASH_POTION_BREAK("entity.splash_potion.break", 514),
    ENTITY_SPLASH_POTION_THROW("entity.splash_potion.throw", 515),
    ENTITY_SQUID_AMBIENT("entity.squid.ambient", 516),
    ENTITY_SQUID_DEATH("entity.squid.death", 517),
    ENTITY_SQUID_HURT("entity.squid.hurt", 518),
    ENTITY_SQUID_SQUIRT("entity.squid.squirt", 519),
    ENTITY_STRAY_AMBIENT("entity.stray.ambient", 520),
    ENTITY_STRAY_DEATH("entity.stray.death", 521),
    ENTITY_STRAY_HURT("entity.stray.hurt", 522),
    ENTITY_STRAY_STEP("entity.stray.step", 523),
    ENTITY_TNT_PRIMED("entity.tnt.primed", 524),
    ENTITY_TROPICAL_FISH_AMBIENT("entity.tropical_fish.ambient", 525),
    ENTITY_TROPICAL_FISH_DEATH("entity.tropical_fish.death", 526),
    ENTITY_TROPICAL_FISH_FLOP("entity.tropical_fish.flop", 527),
    ENTITY_TROPICAL_FISH_HURT("entity.tropical_fish.hurt", 528),
    ENTITY_TURTLE_AMBIENT_LAND("entity.turtle.ambient_land", 529),
    ENTITY_TURTLE_DEATH("entity.turtle.death", 530),
    ENTITY_TURTLE_DEATH_BABY("entity.turtle.death_baby", 531),
    ENTITY_TURTLE_EGG_BREAK("entity.turtle.egg_break", 532),
    ENTITY_TURTLE_EGG_CRACK("entity.turtle.egg_crack", 533),
    ENTITY_TURTLE_EGG_HATCH("entity.turtle.egg_hatch", 534),
    ENTITY_TURTLE_HURT("entity.turtle.hurt", 535),
    ENTITY_TURTLE_HURT_BABY("entity.turtle.hurt_baby", 536),
    ENTITY_TURTLE_LAY_EGG("entity.turtle.lay_egg", 537),
    ENTITY_TURTLE_SHAMBLE("entity.turtle.shamble", 538),
    ENTITY_TURTLE_SHAMBLE_BABY("entity.turtle.shamble_baby", 539),
    ENTITY_TURTLE_SWIM("entity.turtle.swim", 540),
    ENTITY_VEX_AMBIENT("entity.vex.ambient", 541),
    ENTITY_VEX_CHARGE("entity.vex.charge", 542),
    ENTITY_VEX_DEATH("entity.vex.death", 543),
    ENTITY_VEX_HURT("entity.vex.hurt", 544),
    ENTITY_VILLAGER_AMBIENT("entity.villager.ambient", 545),
    ENTITY_VILLAGER_DEATH("entity.villager.death", 546),
    ENTITY_VILLAGER_HURT("entity.villager.hurt", 547),
    ENTITY_VILLAGER_NO("entity.villager.no", 548),
    ENTITY_VILLAGER_TRADE("entity.villager.trade", 549),
    ENTITY_VILLAGER_YES("entity.villager.yes", 550),
    ENTITY_VINDICATOR_AMBIENT("entity.vindicator.ambient", 551),
    ENTITY_VINDICATOR_DEATH("entity.vindicator.death", 552),
    ENTITY_VINDICATOR_HURT("entity.vindicator.hurt", 553),
    ENTITY_WITCH_AMBIENT("entity.witch.ambient", 554),
    ENTITY_WITCH_DEATH("entity.witch.death", 555),
    ENTITY_WITCH_DRINK("entity.witch.drink", 556),
    ENTITY_WITCH_HURT("entity.witch.hurt", 557),
    ENTITY_WITCH_THROW("entity.witch.throw", 558),
    ENTITY_WITHER_AMBIENT("entity.wither.ambient", 559),
    ENTITY_WITHER_BREAK_BLOCK("entity.wither.break_block", 560),
    ENTITY_WITHER_DEATH("entity.wither.death", 561),
    ENTITY_WITHER_HURT("entity.wither.hurt", 562),
    ENTITY_WITHER_SHOOT("entity.wither.shoot", 563),
    ENTITY_WITHER_SPAWN("entity.wither.spawn", 564),
    ENTITY_WITHER_SKELETON_AMBIENT("entity.wither_skeleton.ambient", 565),
    ENTITY_WITHER_SKELETON_DEATH("entity.wither_skeleton.death", 566),
    ENTITY_WITHER_SKELETON_HURT("entity.wither_skeleton.hurt", 567),
    ENTITY_WITHER_SKELETON_STEP("entity.wither_skeleton.step", 568),
    ENTITY_WOLF_AMBIENT("entity.wolf.ambient", 569),
    ENTITY_WOLF_DEATH("entity.wolf.death", 570),
    ENTITY_WOLF_GROWL("entity.wolf.growl", 571),
    ENTITY_WOLF_HOWL("entity.wolf.howl", 572),
    ENTITY_WOLF_HURT("entity.wolf.hurt", 573),
    ENTITY_WOLF_PANT("entity.wolf.pant", 574),
    ENTITY_WOLF_SHAKE("entity.wolf.shake", 575),
    ENTITY_WOLF_STEP("entity.wolf.step", 576),
    ENTITY_WOLF_WHINE("entity.wolf.whine", 577),
    ENTITY_ZOMBIE_AMBIENT("entity.zombie.ambient", 578),
    ENTITY_ZOMBIE_ATTACK_IRON_DOOR("entity.zombie.attack_iron_door", 580),
    ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR("entity.zombie.attack_wooden_door", 579),
    ENTITY_ZOMBIE_BREAK_WOODEN_DOOR("entity.zombie.break_wooden_door", 581),
    ENTITY_ZOMBIE_CONVERTED_TO_DROWNED("entity.zombie.converted_to_drowned", 582),
    ENTITY_ZOMBIE_DEATH("entity.zombie.death", 583),
    ENTITY_ZOMBIE_DESTROY_EGG("entity.zombie.destroy_egg", 584),
    ENTITY_ZOMBIE_HURT("entity.zombie.hurt", 585),
    ENTITY_ZOMBIE_INFECT("entity.zombie.infect", 586),
    ENTITY_ZOMBIE_STEP("entity.zombie.step", 587),
    ENTITY_ZOMBIE_HORSE_AMBIENT("entity.zombie_horse.ambient", 588),
    ENTITY_ZOMBIE_HORSE_DEATH("entity.zombie_horse.death", 589),
    ENTITY_ZOMBIE_HORSE_HURT("entity.zombie_horse.hurt", 590),
    ENTITY_ZOMBIE_PIGMAN_AMBIENT("entity.zombie_pigman.ambient", 591),
    ENTITY_ZOMBIE_PIGMAN_ANGRY("entity.zombie_pigman.angry", 592),
    ENTITY_ZOMBIE_PIGMAN_DEATH("entity.zombie_pigman.death", 593),
    ENTITY_ZOMBIE_PIGMAN_HURT("entity.zombie_pigman.hurt", 594),
    ENTITY_ZOMBIE_VILLAGER_AMBIENT("entity.zombie_villager.ambient", 595),
    ENTITY_ZOMBIE_VILLAGER_CONVERTED("entity.zombie_villager.converted", 596),
    ENTITY_ZOMBIE_VILLAGER_CURE("entity.zombie_villager.cure", 597),
    ENTITY_ZOMBIE_VILLAGER_DEATH("entity.zombie_villager.death", 598),
    ENTITY_ZOMBIE_VILLAGER_HURT("entity.zombie_villager.hurt", 599),
    ENTITY_ZOMBIE_VILLAGER_STEP("entity.zombie_villager.step", 600),
    ITEM_ARMOR_EQUIP_CHAIN("item.armor.equip_chain", 601),
    ITEM_ARMOR_EQUIP_DIAMOND("item.armor.equip_diamond", 602),
    ITEM_ARMOR_EQUIP_ELYTRA("item.armor.equip_elytra", 603),
    ITEM_ARMOR_EQUIP_GENERIC("item.armor.equip_generic", 604),
    ITEM_ARMOR_EQUIP_GOLD("item.armor.equip_gold", 605),
    ITEM_ARMOR_EQUIP_IRON("item.armor.equip_iron", 606),
    ITEM_ARMOR_EQUIP_LEATHER("item.armor.equip_leather", 607),
    ITEM_ARMOR_EQUIP_TURTLE("item.armor.equip_turtle", 608),
    ITEM_AXE_STRIP("item.axe.strip", 609),
    ITEM_BOTTLE_EMPTY("item.bottle.empty", 610),
    ITEM_BOTTLE_FILL("item.bottle.fill", 611),
    ITEM_BOTTLE_FILL_DRAGONBREATH("item.bottle.fill_dragonbreath", 612),
    ITEM_BUCKET_EMPTY("item.bucket.empty", 613),
    ITEM_BUCKET_EMPTY_FISH("item.bucket.empty_fish", 614),
    ITEM_BUCKET_EMPTY_LAVA("item.bucket.empty_lava", 615),
    ITEM_BUCKET_FILL("item.bucket.fill", 616),
    ITEM_BUCKET_FILL_FISH("item.bucket.fill_fish", 617),
    ITEM_BUCKET_FILL_LAVA("item.bucket.fill_lava", 618),
    ITEM_CHORUS_FRUIT_TELEPORT("item.chorus_fruit.teleport", 619),
    ITEM_ELYTRA_FLYING("item.elytra.flying", 620),
    ITEM_FIRECHARGE_USE("item.firecharge.use", 621),
    ITEM_FLINTANDSTEEL_USE("item.flintandsteel.use", 622),
    ITEM_HOE_TILL("item.hoe.till", 623),
    ITEM_SHIELD_BLOCK("item.shield.block", 624),
    ITEM_SHIELD_BREAK("item.shield.break", 625),
    ITEM_SHOVEL_FLATTEN("item.shovel.flatten", 626),
    ITEM_TOTEM_USE("item.totem.use", 627),
    ITEM_TRIDENT_HIT("item.trident.hit", 628),
    ITEM_TRIDENT_HIT_GROUND("item.trident.hit_ground", 629),
    ITEM_TRIDENT_RETURN("item.trident.return", 630),
    ITEM_TRIDENT_RIPTIDE_1("item.trident.riptide_1", 631),
    ITEM_TRIDENT_RIPTIDE_2("item.trident.riptide_2", 632),
    ITEM_TRIDENT_RIPTIDE_3("item.trident.riptide_3", 633),
    ITEM_TRIDENT_THROW("item.trident.throw", 634),
    ITEM_TRIDENT_THUNDER("item.trident.thunder", 635),
    MUSIC_CREATIVE("music.creative", 636),
    MUSIC_CREDITS("music.credits", 637),
    MUSIC_DRAGON("music.dragon", 638),
    MUSIC_END("music.end", 639),
    MUSIC_GAME("music.game", 640),
    MUSIC_MENU("music.menu", 641),
    MUSIC_NETHER("music.nether", 642),
    MUSIC_UNDER_WATER("music.under_water", 643),
    MUSIC_DISC_11("music_disc.11", 644),
    MUSIC_DISC_13("music_disc.13", 645),
    MUSIC_DISC_BLOCKS("music_disc.blocks", 646),
    MUSIC_DISC_CAT("music_disc.cat", 647),
    MUSIC_DISC_CHIRP("music_disc.chirp", 648),
    MUSIC_DISC_FAR("music_disc.far", 649),
    MUSIC_DISC_MALL("music_disc.mall", 650),
    MUSIC_DISC_MELLOHI("music_disc.mellohi", 651),
    MUSIC_DISC_STAL("music_disc.stal", 652),
    MUSIC_DISC_STRAD("music_disc.strad", 653),
    MUSIC_DISC_WAIT("music_disc.wait", 654),
    MUSIC_DISC_WARD("music_disc.ward", 655),
    UI_BUTTON_CLICK("ui.button.click", 656),
    UI_TOAST_CHALLENGE_COMPLETE("ui.toast.challenge_complete", 657),
    UI_TOAST_IN("ui.toast.in", 658),
    UI_TOAST_OUT("ui.toast.out", 659),
    WEATHER_RAIN("weather.rain", 660),
    WEATHER_RAIN_ABOVE("weather.rain.above", 661);

    private final String name;
    private final int nativeId;

    MinecraftSound(String name, int nativeId) {
        this.name = name;
        this.nativeId = nativeId;
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
    public boolean isNative() {
        return true;
    }

}
