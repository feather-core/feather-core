package org.feathercore.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class TypeAdapterFactory implements com.google.gson.TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> token) {
        Class<T> oclass = (Class<T>) token.getRawType();

        if (!oclass.isEnum()) {
            return null;
        } else {
            final Map<String, T> map = Maps.newHashMap();

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
