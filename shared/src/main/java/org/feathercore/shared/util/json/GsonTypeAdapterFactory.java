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
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class GsonTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(@NonNull final Gson gson, @NonNull final TypeToken<T> token) {
        @SuppressWarnings("unchecked") val oclass = (Class<T>) token.getRawType();

        if (!oclass.isEnum()) {
            return null;
        } else {
            val map = new HashMap<String, T>();

            for (T t : oclass.getEnumConstants()) {
                map.put(deserialize(t), t);
            }

            return new TypeAdapter<T>() {
                public void write(@NonNull final JsonWriter writer, @NonNull final T value) throws IOException {
                    if (value == null) {
                        writer.nullValue();
                    } else {
                        writer.value(deserialize(value));
                    }
                }

                public T read(@NonNull final JsonReader reader) throws IOException {
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

    private String deserialize(@NonNull final Object value) {
        return value instanceof Enum
                ? ((Enum) value).name().toLowerCase(Locale.US) : value.toString().toLowerCase(Locale.US);
    }
}
