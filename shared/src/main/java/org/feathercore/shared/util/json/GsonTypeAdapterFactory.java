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

package org.feathercore.shared.util.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class GsonTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> token) {
        Class<T> oclass = (Class<T>) token.getRawType();

        if (!oclass.isEnum()) {
            return null;
        } else {
            final Map<String, T> map = new HashMap<>();

            for (T t : oclass.getEnumConstants()) {
                map.put(deserialize(t), t);
            }

            return new TypeAdapter<T>() {
                public void write(JsonWriter writer, T value) throws IOException {
                    if (value == null) {
                        writer.nullValue();
                    } else {
                        writer.value(deserialize(value));
                    }
                }

                public T read(JsonReader reader) throws IOException {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull();
                        return null;
                    } else {
                        return map.get(reader.nextString());
                    }
                }
            };
        }
    }

    private String deserialize(Object value) {
        return value instanceof Enum ? ((Enum) value).name().toLowerCase(Locale.US) : value.toString().toLowerCase(Locale.US);
    }
}
