/*
 * Copyright (C) 2013-2017 Steveice10
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * - The above copyright notice and this permission notice shall be included
 *   in all copies or substantial portions of the Software.
 *
 * - THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *   TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 *   THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *   CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 *   OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
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

package org.feathercore.shared.nbt.tag;

import org.feathercore.shared.nbt.exception.TagCreateException;
import org.feathercore.shared.nbt.exception.TagRegisterException;
import org.feathercore.shared.nbt.tag.exact.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by k.shandurenko on 18/04/2019
 */
public class TagRegistry {
    private static final Map<Integer, Class<? extends Tag>> idToTag = new HashMap<>();
    private static final Map<Class<? extends Tag>, Integer> tagToId = new HashMap<>();

    static {
        register(1, ByteTag.class);
        register(2, ShortTag.class);
        register(3, IntTag.class);
        register(4, LongTag.class);
        register(5, FloatTag.class);
        register(6, DoubleTag.class);
        register(7, ByteArrayTag.class);
        register(8, StringTag.class);
        register(9, ListTag.class);
        register(10, CompoundTag.class);
        register(11, IntArrayTag.class);
        register(12, LongArrayTag.class);
    }

    /**
     * Registers a tag class.
     *
     * @param id ID of the tag.
     * @param tag Tag class to register.
     * @throws TagRegisterException If an error occurs while registering the tag.
     */
    public static void register(int id, Class<? extends Tag> tag) throws TagRegisterException {
        if (idToTag.containsKey(id)) {
            throw new TagRegisterException("Tag ID \"" + id + "\" is already in use.");
        }

        if (tagToId.containsKey(tag)) {
            throw new TagRegisterException("Tag \"" + tag.getSimpleName() + "\" is already registered.");
        }

        idToTag.put(id, tag);
        tagToId.put(tag, id);
    }

    /**
     * Unregisters a tag class.
     *
     * @param id ID of the tag to unregister.
     */
    public static void unregister(int id) {
        tagToId.remove(getClassFor(id));
        idToTag.remove(id);
    }

    /**
     * Gets the tag class with the given id.
     *
     * @param id Id of the tag.
     * @return The tag class with the given id, or null if it cannot be found.
     */
    public static Class<? extends Tag> getClassFor(int id) {
        if (!idToTag.containsKey(id)) {
            return null;
        }

        return idToTag.get(id);
    }

    /**
     * Gets the id of the given tag class.
     *
     * @param clazz The tag class to get the id of.
     * @return The id of the given tag class, or -1 if it cannot be found.
     */
    public static int getIdFor(Class<? extends Tag> clazz) {
        if (!tagToId.containsKey(clazz)) {
            return -1;
        }

        return tagToId.get(clazz);
    }

    /**
     * Creates an instance of the tag with the given id, using the String constructor.
     *
     * @param id Id of the tag.
     * @param tagName Name to give the tag.
     * @return The created tag.
     * @throws TagCreateException If an error occurs while creating the tag.
     */
    public static Tag createInstance(int id, String tagName) throws TagCreateException {
        Class<? extends Tag> clazz = idToTag.get(id);
        if (clazz == null) {
            throw new TagCreateException("Could not find tag with ID \"" + id + "\".");
        }

        try {
            Constructor<? extends Tag> constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(tagName);
        } catch (Exception e) {
            throw new TagCreateException("Failed to create instance of tag \"" + clazz.getSimpleName() + "\".", e);
        }
    }
}