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

package org.feathercore.shared.nbt.tag.exact;

import lombok.NonNull;
import org.feathercore.shared.nbt.NBTIO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * A compound tag containing other tags.
 */
public class CompoundTag extends Tag implements Iterable<Tag> {

    private Map<String, Tag> value;

    /**
     * Creates a tag with the specified name.
     *
     * @param name The name of the tag.
     */
    public CompoundTag(@NonNull String name) {
        this(name, new LinkedHashMap<>());
    }

    /**
     * Creates a tag with the specified name.
     *
     * @param name The name of the tag.
     * @param value The value of the tag.
     */
    public CompoundTag(@NonNull String name, @NonNull Map<String, Tag> value) {
        super(name);
        this.value = new LinkedHashMap<>(value);
    }

    @Override
    public @NotNull Map<String, Tag> getValue() {
        return Collections.unmodifiableMap(this.value);
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(@NonNull Map<String, Tag> value) {
        this.value = new LinkedHashMap<>(value);
    }

    /**
     * Checks whether the compound tag is empty.
     *
     * @return Whether the compound tag is empty.
     */
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    /**
     * Checks whether the compound tag contains a tag with the specified name.
     *
     * @param tagName Name of the tag to check for.
     * @return Whether the compound tag contains a tag with the specified name.
     */
    public boolean contains(@NonNull String tagName) {
        return this.value.containsKey(tagName);
    }

    /**
     * Gets the tag with the specified name.
     *
     * @param <T> Type of tag to get.
     * @param tagName Name of the tag.
     * @return The tag with the specified name.
     */
    @SuppressWarnings("unchecked")
    public @Nullable <T extends Tag> T get(@NonNull String tagName) {
        return (T) this.value.get(tagName);
    }

    /**
     * Gets the byte-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the byte-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public byte getByte(@NonNull String tagName, byte defaultValue) {
        ByteTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValuePrimitive();
    }

    /**
     * Gets the byte-array-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the byte-array-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public byte[] getByteArray(@NonNull String tagName, @Nullable byte[] defaultValue) {
        ByteArrayTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValue();
    }

    /**
     * Gets the double-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the double-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public double getDouble(@NonNull String tagName, double defaultValue) {
        DoubleTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValuePrimitive();
    }

    /**
     * Gets the float-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the float-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public float getFloat(@NonNull String tagName, float defaultValue) {
        FloatTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValuePrimitive();
    }

    /**
     * Gets the int-array-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the int-array-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public int[] getIntArray(@NonNull String tagName, @Nullable int[] defaultValue) {
        IntArrayTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValue();
    }

    /**
     * Gets the int-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the int-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public int getInt(@NonNull String tagName, int defaultValue) {
        IntTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValuePrimitive();
    }

    /**
     * Gets the list-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns empty list.
     *
     * @param tagName Name of the list-tag.
     * @return The value of the tag or default value.
     */
    public @NotNull List<Tag> getTagList(@NonNull String tagName) {
        ListTag tag = get(tagName);
        return tag == null ? Collections.emptyList() : tag.getValue();
    }

    /**
     * Gets the long-array-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the long-array-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public long[] getLongArray(@NonNull String tagName, @Nullable long[] defaultValue) {
        LongArrayTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValue();
    }

    /**
     * Gets the long-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the long-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public long getLong(@NonNull String tagName, long defaultValue) {
        LongTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValuePrimitive();
    }

    /**
     * Gets the short-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the short-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public short getShort(@NonNull String tagName, short defaultValue) {
        ShortTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValuePrimitive();
    }

    /**
     * Gets the string-tag with the specified name and retrieves it's value.
     * Whether tag with the specified name is not present, returns given default value.
     *
     * @param tagName Name of the string-tag.
     * @param defaultValue Value to be returned whether tag with the specified name is not present.
     * @return The value of the tag or default value.
     */
    public String getString(@NonNull String tagName, @Nullable String defaultValue) {
        StringTag tag = get(tagName);
        return tag == null ? defaultValue : tag.getValue();
    }

    /**
     * Puts the tag into this compound tag.
     *
     * @param <T> Type of tag to put.
     * @param tag Tag to put into this compound tag.
     * @return The previous tag associated with its name, or null if there wasn't one.
     */
    @SuppressWarnings("unchecked")
    public @Nullable <T extends Tag> T put(@NonNull T tag) {
        return (T) this.value.put(tag.getName(), tag);
    }

    /**
     * Removes a tag from this compound tag.
     *
     * @param <T> Type of tag to remove.
     * @param tagName Name of the tag to remove.
     * @return The removed tag.
     */
    @SuppressWarnings("unchecked")
    public @Nullable <T extends Tag> T remove(@NonNull String tagName) {
        return (T) this.value.remove(tagName);
    }

    /**
     * Gets a set of keys in this compound tag.
     *
     * @return The compound tag's key set.
     */
    public @NotNull Set<String> keySet() {
        return this.value.keySet();
    }

    /**
     * Gets a collection of tags in this compound tag.
     *
     * @return This compound tag's tags.
     */
    public @NotNull Collection<Tag> values() {
        return this.value.values();
    }

    /**
     * Gets the number of tags in this compound tag.
     *
     * @return This compound tag's size.
     */
    public int size() {
        return this.value.size();
    }

    /**
     * Clears all tags from this compound tag.
     */
    public void clear() {
        this.value.clear();
    }

    @Override
    public @NotNull Iterator<Tag> iterator() {
        return this.values().iterator();
    }

    @Override
    public void read(DataInput in) throws IOException {
        List<Tag> tags = new ArrayList<>();
        try {
            Tag tag;
            while ((tag = NBTIO.readTag(in)) != null) {
                tags.add(tag);
            }
        } catch (EOFException e) {
            throw new IOException("Closing EndTag was not found!");
        }

        for (Tag tag : tags) {
            this.put(tag);
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        for (Tag tag : this.value.values()) {
            NBTIO.writeTag(out, tag);
        }

        out.writeByte(0);
    }

    @Override
    public CompoundTag clone() {
        Map<String, Tag> newMap = new LinkedHashMap<>();
        for (Entry<String, Tag> entry : this.value.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue().clone());
        }

        return new CompoundTag(this.getName(), newMap);
    }
}