package org.feathercore.util;

import com.google.gson.*;

/**
 * Created by k.shandurenko on 09/04/2019
 */
public class JsonUtils {

    /**
     * Does the given JsonObject contain a string field with the given name?
     */
    public static boolean isString(JsonObject holder, String stringKey) {
        return isJsonPrimitive(holder, stringKey) && holder.getAsJsonPrimitive(stringKey).isString();
    }

    /**
     * Is the given JsonElement a string?
     */
    public static boolean isString(JsonElement possibleString) {
        return possibleString.isJsonPrimitive() && possibleString.getAsJsonPrimitive().isString();
    }

    public static boolean isBoolean(JsonObject holder, String booleanKey) {
        return isJsonPrimitive(holder, booleanKey) && holder.getAsJsonPrimitive(booleanKey).isBoolean();
    }

    /**
     * Does the given JsonObject contain an array field with the given name?
     */
    public static boolean isJsonArray(JsonObject holder, String arrayKey) {
        return hasField(holder, arrayKey) && holder.get(arrayKey).isJsonArray();
    }

    /**
     * Does the given JsonObject contain a field with the given name whose type
     * is primitive (String, Java primitive, or Java primitive wrapper)?
     */
    public static boolean isJsonPrimitive(JsonObject holder, String key) {
        return hasField(holder, key) && holder.get(key).isJsonPrimitive();
    }

    /**
     * Does the given JsonObject contain a field with the given name?
     */
    public static boolean hasField(JsonObject holder, String key) {
        return holder != null && holder.get(key) != null;
    }

    /**
     * Gets the string value of the given JsonElement. Expects the second
     * parameter to be the name of the element's field if an error message needs
     * to be thrown.
     */
    public static String getString(JsonElement possibleString, String key) {
        if (possibleString.isJsonPrimitive()) {
            return possibleString.getAsString();
        } else {
            throw new JsonSyntaxException("Expected " + key + " to be a string, was " + toString(possibleString));
        }
    }

    /**
     * Gets the string value of the field on the JsonObject with the given name.
     */
    public static String getString(JsonObject holder, String key) {
        if (holder.has(key)) {
            return getString(holder.get(key), key);
        } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a string");
        }
    }

    /**
     * Gets the string value of the field on the JsonObject with the given name,
     * or the given default value if the field is missing.
     */
    public static String getString(JsonObject holder, String key, String defaultValue) {
        return holder.has(key) ? getString(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the boolean value of the given JsonElement. Expects the second
     * parameter to be the name of the element's field if an error message needs
     * to be thrown.
     */
    public static boolean getBoolean(JsonElement possibleBoolean, String key) {
        if (possibleBoolean.isJsonPrimitive()) {
            return possibleBoolean.getAsBoolean();
        } else {
            throw new JsonSyntaxException("Expected " + key + " to be a Boolean, was " + toString(possibleBoolean));
        }
    }

    /**
     * Gets the boolean value of the field on the JsonObject with the given
     * name.
     */
    public static boolean getBoolean(JsonObject holder, String key) {
        if (holder.has(key)) {
            return getBoolean(holder.get(key), key);
        } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a Boolean");
        }
    }

    /**
     * Gets the boolean value of the field on the JsonObject with the given
     * name, or the given default value if the field is missing.
     */
    public static boolean getBoolean(JsonObject holder, String key, boolean defaultValue) {
        return holder.has(key) ? getBoolean(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the float value of the given JsonElement. Expects the second
     * parameter to be the name of the element's field if an error message needs
     * to be thrown.
     */
    public static float getFloat(JsonElement possibleFloat, String key) {
        if (possibleFloat.isJsonPrimitive() && possibleFloat.getAsJsonPrimitive().isNumber()) {
            return possibleFloat.getAsFloat();
        } else {
            throw new JsonSyntaxException("Expected " + key + " to be a Float, was " + toString(possibleFloat));
        }
    }

    /**
     * Gets the float value of the field on the JsonObject with the given name.
     */
    public static float getFloat(JsonObject holder, String key) {
        if (holder.has(key)) {
            return getFloat(holder.get(key), key);
        } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a Float");
        }
    }

    /**
     * Gets the float value of the field on the JsonObject with the given name,
     * or the given default value if the field is missing.
     */
    public static float getFloat(JsonObject holder, String key, float defaultValue) {
        return holder.has(key) ? getFloat(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the integer value of the given JsonElement. Expects the second
     * parameter to be the name of the element's field if an error message needs
     * to be thrown.
     */
    public static int getInt(JsonElement possibleInt, String key) {
        if (possibleInt.isJsonPrimitive() && possibleInt.getAsJsonPrimitive().isNumber()) {
            return possibleInt.getAsInt();
        } else {
            throw new JsonSyntaxException("Expected " + key + " to be a Int, was " + toString(possibleInt));
        }
    }

    /**
     * Gets the integer value of the field on the JsonObject with the given
     * name.
     */
    public static int getInt(JsonObject holder, String key) {
        if (holder.has(key)) {
            return getInt(holder.get(key), key);
        } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a Int");
        }
    }

    /**
     * Gets the integer value of the field on the JsonObject with the given
     * name, or the given default value if the field is missing.
     */
    public static int getInt(JsonObject holder, String key, int defaultValue) {
        return holder.has(key) ? getInt(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the given JsonElement as a JsonObject. Expects the second parameter
     * to be the name of the element's field if an error message needs to be
     * thrown.
     */
    public static JsonObject getJsonObject(JsonElement possibleObject, String key) {
        if (possibleObject.isJsonObject()) {
            return possibleObject.getAsJsonObject();
        } else {
            throw new JsonSyntaxException("Expected " + key + " to be a JsonObject, was " + toString(possibleObject));
        }
    }

    public static JsonObject getJsonObject(JsonObject holder, String key) {
        if (holder.has(key)) {
            return getJsonObject(holder.get(key), key);
        } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonObject");
        }
    }

    /**
     * Gets the JsonObject field on the JsonObject with the given name, or the
     * given default value if the field is missing.
     */
    public static JsonObject getJsonObject(JsonObject holder, String key, JsonObject defaultValue) {
        return holder.has(key) ? getJsonObject(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets the given JsonElement as a JsonArray. Expects the second parameter
     * to be the name of the element's field if an error message needs to be
     * thrown.
     */
    public static JsonArray getJsonArray(JsonElement possibleArray, String key) {
        if (possibleArray.isJsonArray()) {
            return possibleArray.getAsJsonArray();
        } else {
            throw new JsonSyntaxException("Expected " + key + " to be a JsonArray, was " + toString(possibleArray));
        }
    }

    /**
     * Gets the JsonArray field on the JsonObject with the given name.
     */
    public static JsonArray getJsonArray(JsonObject holder, String key) {
        if (holder.has(key)) {
            return getJsonArray(holder.get(key), key);
        } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonArray");
        }
    }

    /**
     * Gets the JsonArray field on the JsonObject with the given name, or the
     * given default value if the field is missing.
     */
    public static JsonArray getJsonArray(JsonObject holder, String key, JsonArray defaultValue) {
        return holder.has(key) ? getJsonArray(holder.get(key), key) : defaultValue;
    }

    /**
     * Gets a human-readable description of the given JsonElement's type. For
     * example: "a number (4)"
     */
    public static String toString(JsonElement element) {
        String s = element == null ? null : element.toString();

        if (element == null) {
            return "null (missing)";
        } else if (element.isJsonNull()) {
            return "null (json)";
        } else if (element.isJsonArray()) {
            return "an array (" + s + ")";
        } else if (element.isJsonObject()) {
            return "an object (" + s + ")";
        } else {
            if (element.isJsonPrimitive()) {
                JsonPrimitive jsonprimitive = element.getAsJsonPrimitive();

                if (jsonprimitive.isNumber()) {
                    return "a number (" + s + ")";
                }

                if (jsonprimitive.isBoolean()) {
                    return "a boolean (" + s + ")";
                }
            }

            return s;
        }
    }
}
