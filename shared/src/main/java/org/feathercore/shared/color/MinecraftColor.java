/*
 * Copyright 2019 Feather Core
 *
 * Licensed under the Apache License, Version 2.0 (the "License"),
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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public enum MinecraftColor implements Color {

    WHITE("White", 0, 15, 16383998, 15790320, "§f", 15, 'f'),
    ORANGE("Orange", 1, 14, 16351261, 15435844, "§6", 6, '6'),
    MAGENTA("Magneta", 2, 13, 13061821, 12801229, "§d", 13, 'd'),
    AQUA("Aqua", 3, 12, 3847130, 6719955, "§b", 11, 'b'),
    YELLOW("Yellow", 4, 11, 16701501, 14602026, "§e", 14, 'e'),
    LIME("Lime", 5, 10, 8439583, 4312372, "§a", 10, 'a'),
    PINK("Pink", 6, 9, 15961002, 14188952, "§c", 12, 'c'),
    GRAY("Gray", 7, 8, 4673362, 4408131, "§8", 8, '8'),
    SILVER("Silver", 8, 7, 10329495, 11250603, "§7", 7, '7'),
    CYAN("Cyan", 9, 6, 1481884, 2651799, "§3", 3, '3'),
    PURPLE("Purple", 10, 5, 8991416, 8073150, "§5", 5, '5'),
    BLUE("Blue", 11, 4, 3949738, 2437522, "§1", 1, '1'),
    BROWN("Brown", 12, 3, 8606770, 5320730, "§9", 9, '9'),
    GREEN("Green", 13, 2, 6192150, 3887386, "§2", 2, '2'),
    RED("Red", 14, 1, 11546150, 11743532, "§4", 4, '4'),
    BLACK("Black", 15, 0, 1908001, 1973019, "§0", 0, '0');

    /**
     * Text color symbol
     */
    public static final char COLOR_CHAR = '§';
    /**
     * Pattern that is used to remove all colors
     */
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    /**
     * Maps where all the colors are stored by char code
     *
     * @see MinecraftColor#charCode
     */
    private static final Map<Character, MinecraftColor> COLORS_BY_CHAR;
    /**
     * All the color codes that will be used to translate alternative symbols
     */
    private static final String ALL_CODES;

    /**
     * Color name
     */
    private final String name;
    /**
     * Represents wool data value
     */
    private final int woolData;
    /**
     * Represents dye data value
     */
    private final int dyeData;
    /**
     * Represents RGB color
     */
    private final int colorRGB;
    /**
     * A color for firework
     */
    private final int fireworkColor;
    /**
     * Represents {@link String} color format
     */
    private final String format;
    /**
     * Represents chat format code
     */
    private final int chatCode;
    /**
     * Represents a single char code
     */
    private final char charCode;

    @Override
    public boolean isNative() {
        return true;
    }

    @Override
    public int getNativeId() {
        return this.chatCode;
    }

    @Override
    public String toString() {
        return this.format;
    }

    /**
     * Removes all the colors from given text
     *
     * @param input a text to remove colors from
     * @return text without colors
     */
    public static String stripColor(@NonNull final String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    /**
     * Returns a {@code Color} by it's char code
     */
    public static Color getByChar(final char code) {
        return COLORS_BY_CHAR.get(code);
    }

    /**
     * Translates a char code into {@link MinecraftColor#COLOR_CHAR}
     *
     * @param altColorChar char code to translate
     * @param textToTranslate an input text
     * @return formatted text
     */
    public static String translateAlternateColorCodes(final char altColorChar, final @NonNull String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && ALL_CODES.indexOf(b[i + 1]) > -1) {
                b[i] = COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    static {
        MinecraftColor[] values = values();
        COLORS_BY_CHAR = Arrays.stream(values)
                               .collect(Collectors.toMap(MinecraftColor::getCharCode, Function.identity()));
        StringBuilder builder = new StringBuilder(values.length);
        for (MinecraftColor color : values) {
            char code = color.getCharCode();
            builder.append(code);
            if (Character.isAlphabetic(code)) {
                builder.append(Character.toUpperCase(code));
            }
        }
        ALL_CODES = builder.toString();
    }
}
