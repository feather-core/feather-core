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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.Nullable;

/**
 * Created by k.shandurenko on 09/04/2019
 */
@UtilityClass
public class JsonUtil {

    /**
     * Does the given JsonObject contain a string field with the given name?
     */
    public boolean isString(@NonNull final JsonObject holder, @NonNull final String stringKey) {
        return isJsonPrimitive(holder, stringKey) && holder.getAsJsonPrimitive(stringKey).isString();
    }

    /**
     * Is the given JsonElement a string?
     */
    public boolean isString(@NonNull final JsonElement possibleString) {
        return possibleString.isJsonPrimitive() && possibleString.getAsJsonPrimitive().isString();
    }

    public boolean isBoolean(@NonNull final JsonObject holder, @NonNull final String booleanKey) {
        return isJsonPrimitive(holder, booleanKey) && holder.getAsJsonPrimitive(booleanKey).isBoolean();
    }

    /**
     * Does the given JsonObject contain an array field with the given name?
     */
    public boolean isJsonArray(@NonNull final JsonObject holder, @NonNull final String arrayKey) {
        return hasField(holder, arrayKey) && holder.get(arrayKey).isJsonArray();
    }

    /**
     * Does the given JsonObject contain a field with the given name whose type
     * is primitive (String, Java primitive, or Java primitive wrapper)?
     */
    public boolean isJsonPrimitive(@NonNull final JsonObject holder, @NonNull final String key) {
        return hasField(holder, key) && holder.get(key).isJsonPrimitive();
    }

    /**
     * Does the given JsonObject contain a field with the given name?
     */
    public boolean hasField(@NonNull final JsonObject holder, @NonNull final String key) {
        return holder != null && holder.get(key) != null;
    }

    /**
     * Gets the string value of the given JsonElement. Expects the second
     * parameter to be the name of the element's field if an error message needs
     * to be thrown.
     */
    public String getString(@NonNull final JsonElement possibleString, @NonNull final String key) {
        if (possibleString.isJsonPrimitive()) return possibleString.getAsString();
        throw new JsonSyntaxException("Expected " + key + " to be a string, was " + toString(possibleString));
    }

    /**
     * Gets the string value of the field on the JsonObject with the given name.
     */
    public String getString(@NonNull final JsonObject holder, @NonNull final String key) {
        if (holder.has(key)) return getString(holder.get(key), key);
        throw new JsonSyntaxException("Missing " + key + ", expected to find a string");
    }

    /**
     * Gets the string value of the field on the JsonObject with the given name,
     * or the given default value if the field is missing.
     */
    public String getString(@NonNull final JsonObject holder,
                            @NonNull final String key, @Nullable final String defaultValue) {
        return holder.has(key) ? getString(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the boolean value of the given JsonElement. Expects the second
     * parameter to be the name of the element's field if an error message needs
     * to be thrown.
     */
    public boolean getBoolean(@NonNull final JsonElement possibleBoolean, @NonNull final String key) {
        if (possibleBoolean.isJsonPrimitive()) return possibleBoolean.getAsBoolean();
        throw new JsonSyntaxException("Expected " + key + " to be a Boolean, was " + toString(possibleBoolean));
    }

    /**
     * Gets the boolean value of the field on the JsonObject with the given
     * name.
     */
    public boolean getBoolean(@NonNull final JsonObject holder, @NonNull final String key) {
        if (holder.has(key)) return getBoolean(holder.get(key), key);
        throw new JsonSyntaxException("Missing " + key + ", expected to find a Boolean");
    }

    /**
     * Gets the boolean value of the field on the JsonObject with the given
     * name, or the given default value if the field is missing.
     */
    public boolean getBoolean(@NonNull final JsonObject holder, @NonNull final String key, boolean defaultValue) {
        return holder.has(key) ? getBoolean(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the float value of the given JsonElement. Expects the second
     * parameter to be the name of the element's field if an error message needs
     * to be thrown.
     */
    public float getFloat(JsonElement possibleFloat, @NonNull final String key) {
        if (possibleFloat.isJsonPrimitive()
                && possibleFloat.getAsJsonPrimitive().isNumber()) return possibleFloat.getAsFloat();
        throw new JsonSyntaxException("Expected " + key + " to be a Float, was " + toString(possibleFloat));
    }

    /**
     * Gets the float value of the field on the JsonObject with the given name.
     */
    public float getFloat(@NonNull final JsonObject holder, @NonNull final String key) {
        if (holder.has(key)) return getFloat(holder.get(key), key);
        throw new JsonSyntaxException("Missing " + key + ", expected to find a Float");
    }

    /**
     * Gets the float value of the field on the JsonObject with the given name,
     * or the given default value if the field is missing.
     */
    public float getFloat(@NonNull final JsonObject holder, @NonNull final String key, float defaultValue) {
        return holder.has(key) ? getFloat(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the integer value of the given JsonElement. Expects the second
     * parameter to be the name of the element's field if an error message needs
     * to be thrown.
     */
    public int getInt(@NonNull final JsonElement possibleInt, @NonNull final String key) {
        if (possibleInt.isJsonPrimitive() && possibleInt.getAsJsonPrimitive().isNumber()) return possibleInt.getAsInt();
        throw new JsonSyntaxException("Expected " + key + " to be a Int, was " + toString(possibleInt));
    }

    /**
     * Gets the integer value of the field on the JsonObject with the given
     * name.
     */
    public int getInt(@NonNull final JsonObject holder, @NonNull final String key) {
        if (holder.has(key)) return getInt(holder.get(key), key);
        throw new JsonSyntaxException("Missing " + key + ", expected to find a Int");
    }

    /**
     * Gets the integer value of the field on the JsonObject with the given
     * name, or the given default value if the field is missing.
     */
    public int getInt(@NonNull final JsonObject holder, @NonNull final String key, int defaultValue) {
        return holder.has(key) ? getInt(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the given JsonElement as a JsonObject. Expects the second parameter
     * to be the name of the element's field if an error message needs to be
     * thrown.
     */
    public JsonObject getJsonObject(@NonNull final JsonElement possibleObject, @NonNull final String key) {
        if (possibleObject.isJsonObject()) return possibleObject.getAsJsonObject();
        throw new JsonSyntaxException("Expected " + key + " to be a JsonObject, was " + toString(possibleObject));
    }

    public JsonObject getJsonObject(@NonNull final JsonObject holder, @NonNull final String key) {
        if (holder.has(key)) return getJsonObject(holder.get(key), key);
        throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonObject");
    }

    /**
     * Gets the JsonObject field on the JsonObject with the given name, or the
     * given default value if the field is missing.
     */
    public JsonObject getJsonObject(@NonNull final JsonObject holder, @NonNull final String key,
                                    @Nullable final JsonObject defaultValue) {
        return holder.has(key) ? getJsonObject(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the given JsonElement as a JsonArray. Expects the second parameter
     * to be the name of the element's field if an error message needs to be
     * thrown.
     */
    public JsonArray getJsonArray(@NonNull final JsonElement possibleArray, @NonNull final String key) {
        if (possibleArray.isJsonArray()) return possibleArray.getAsJsonArray();
        throw new JsonSyntaxException("Expected " + key + " to be a JsonArray, was " + toString(possibleArray));
    }

    /**
     * Gets the JsonArray field on the JsonObject with the given name.
     */
    public JsonArray getJsonArray(@NonNull final JsonObject holder, @NonNull final String key) {
        if (holder.has(key))  return getJsonArray(holder.get(key), key);
        throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonArray");
    }

    /**
     * Gets the JsonArray field on the JsonObject with the given name, or the
     * given default value if the field is missing.
     */
    public JsonArray getJsonArray(@NonNull final JsonObject holder,
                                  @NonNull final String key, @Nullable final JsonArray defaultValue) {
        return holder.has(key) ? getJsonArray(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets a human-readable description of the given JsonElement's type. For
     * example: "a number (4)"
     */
    public String toString(@NonNull final JsonElement element) {
        if (element == null) return "null (missing)";
        if (element.isJsonNull()) return "null (json)";
        if (element.isJsonArray()) return "an array (" + element.toString() + ")";
        if (element.isJsonObject()) return "an object (" + element.toString() + ")";
        if (element.isJsonPrimitive()) {
            val primitive = element.getAsJsonPrimitive();

            if (primitive.isNumber()) return "a number (" + element.toString() + ")";
            if (primitive.isBoolean()) return "a boolean (" + element.toString() + ")";
        }

        return element.toString();
    }
}
