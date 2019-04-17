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

import org.feathercore.shared.nbt.exception.TagCreateException;
import org.feathercore.shared.nbt.tag.TagRegistry;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A tag containing a list of tags.
 */
public class ListTag extends Tag implements Iterable<Tag> {
    private Class<? extends Tag> type;
    private List<Tag> value;

    /**
     * Creates an empty list tag with the specified name and no defined type.
     *
     * @param name The name of the tag.
     */
    public ListTag(String name) {
        super(name);

        this.type = null;
        this.value = new ArrayList<>();
    }

    /**
     * Creates an empty list tag with the specified name and type.
     *
     * @param name The name of the tag.
     * @param type Tag type of the list.
     */
    public ListTag(String name, Class<? extends Tag> type) {
        this(name);

        this.type = type;
    }

    /**
     * Creates a list tag with the specified name and value.
     * The list tag's type will be set to that of the first tag being added, or null if the given list is empty.
     *
     * @param name The name of the tag.
     * @param value The value of the tag.
     * @throws IllegalArgumentException If all tags in the list are not of the same type.
     */
    public ListTag(String name, List<Tag> value) throws IllegalArgumentException {
        this(name);

        this.setValue(value);
    }

    @Override
    public List<Tag> getValue() {
        return new ArrayList<>(this.value);
    }

    /**
     * Sets the value of this tag.
     * The list tag's type will be set to that of the first tag being added, or null if the given list is empty.
     *
     * @param value New value of this tag.
     * @throws IllegalArgumentException If all tags in the list are not of the same type.
     */
    public void setValue(List<Tag> value) throws IllegalArgumentException {
        this.type = null;
        this.value.clear();

        for (Tag tag : value) {
            this.add(tag);
        }
    }

    /**
     * Gets the element type of the ListTag.
     *
     * @return The ListTag's element type, or null if the list does not yet have a defined type.
     */
    public Class<? extends Tag> getElementType() {
        return this.type;
    }

    /**
     * Adds a tag to this list tag.
     * If the list does not yet have a type, it will be set to the type of the tag being added.
     *
     * @param tag Tag to add. Should not be null.
     * @return If the list was changed as a result.
     * @throws IllegalArgumentException If the tag's type differs from the list tag's type.
     */
    public boolean add(Tag tag) throws IllegalArgumentException {
        if (tag == null) {
            return false;
        }

        // If empty list, use this as tag type.
        if (this.type == null) {
            this.type = tag.getClass();
        } else if (tag.getClass() != this.type) {
            throw new IllegalArgumentException("Tag type cannot differ from ListTag type.");
        }

        return this.value.add(tag);
    }

    /**
     * Removes a tag from this list tag.
     *
     * @param tag Tag to remove.
     * @return If the list contained the tag.
     */
    public boolean remove(Tag tag) {
        return this.value.remove(tag);
    }

    /**
     * Gets the tag at the given index of this list tag.
     *
     * @param <T> Type of tag to get
     * @param index Index of the tag.
     * @return The tag at the given index.
     */
    public <T extends Tag> T get(int index) {
        return (T) this.value.get(index);
    }

    /**
     * Gets the number of tags in this list tag.
     *
     * @return The size of this list tag.
     */
    public int size() {
        return this.value.size();
    }

    @Override
    public Iterator<Tag> iterator() {
        return this.value.iterator();
    }

    @Override
    public void read(DataInput in) throws IOException {
        this.type = null;
        this.value.clear();

        int id = in.readUnsignedByte();
        if (id != 0) {
            this.type = TagRegistry.getClassFor(id);
            if (this.type == null) {
                throw new IOException("Unknown tag ID in ListTag: " + id);
            }
        }

        int count = in.readInt();
        for (int index = 0; index < count; index++) {
            Tag tag;
            try {
                tag = TagRegistry.createInstance(id, "");
            } catch (TagCreateException e) {
                throw new IOException("Failed to create tag.", e);
            }

            tag.read(in);
            this.add(tag);
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        if (this.type == null) {
            out.writeByte(0);
        } else {
            int id = TagRegistry.getIdFor(this.type);
            if (id == -1) {
                throw new IOException("ListTag contains unregistered tag class.");
            }

            out.writeByte(id);
        }

        out.writeInt(this.value.size());
        for (Tag tag : this.value) {
            tag.write(out);
        }
    }

    @Override
    public ListTag clone() {
        List<Tag> newList = new ArrayList<Tag>();
        for (Tag value : this.value) {
            newList.add(value.clone());
        }

        return new ListTag(this.getName(), newList);
    }
}