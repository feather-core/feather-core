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

package org.feathercore.shared.nbt.tag.exact;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A tag containing a string.
 */
public class StringTag extends Tag {
    private String value;

    /**
     * Creates a tag with the specified name.
     *
     * @param name The name of the tag.
     */
    public StringTag(String name) {
        this(name, "");
    }

    /**
     * Creates a tag with the specified name.
     *
     * @param name The name of the tag.
     * @param value The value of the tag.
     */
    public StringTag(String name, String value) {
        super(name);
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void read(DataInput in) throws IOException {
        this.value = in.readUTF();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.value);
    }

    @Override
    public StringTag clone() {
        return new StringTag(this.getName(), this.getValue());
    }
}