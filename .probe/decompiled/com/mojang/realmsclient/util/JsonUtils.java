package com.mojang.realmsclient.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.Nullable;

public class JsonUtils {

    public static <T> T getRequired(String string0, JsonObject jsonObject1, Function<JsonObject, T> functionJsonObjectT2) {
        JsonElement $$3 = jsonObject1.get(string0);
        if ($$3 == null || $$3.isJsonNull()) {
            throw new IllegalStateException("Missing required property: " + string0);
        } else if (!$$3.isJsonObject()) {
            throw new IllegalStateException("Required property " + string0 + " was not a JsonObject as espected");
        } else {
            return (T) functionJsonObjectT2.apply($$3.getAsJsonObject());
        }
    }

    public static String getRequiredString(String string0, JsonObject jsonObject1) {
        String $$2 = getStringOr(string0, jsonObject1, null);
        if ($$2 == null) {
            throw new IllegalStateException("Missing required property: " + string0);
        } else {
            return $$2;
        }
    }

    @Nullable
    public static String getStringOr(String string0, JsonObject jsonObject1, @Nullable String string2) {
        JsonElement $$3 = jsonObject1.get(string0);
        if ($$3 != null) {
            return $$3.isJsonNull() ? string2 : $$3.getAsString();
        } else {
            return string2;
        }
    }

    @Nullable
    public static UUID getUuidOr(String string0, JsonObject jsonObject1, @Nullable UUID uUID2) {
        String $$3 = getStringOr(string0, jsonObject1, null);
        return $$3 == null ? uUID2 : UUID.fromString($$3);
    }

    public static int getIntOr(String string0, JsonObject jsonObject1, int int2) {
        JsonElement $$3 = jsonObject1.get(string0);
        if ($$3 != null) {
            return $$3.isJsonNull() ? int2 : $$3.getAsInt();
        } else {
            return int2;
        }
    }

    public static long getLongOr(String string0, JsonObject jsonObject1, long long2) {
        JsonElement $$3 = jsonObject1.get(string0);
        if ($$3 != null) {
            return $$3.isJsonNull() ? long2 : $$3.getAsLong();
        } else {
            return long2;
        }
    }

    public static boolean getBooleanOr(String string0, JsonObject jsonObject1, boolean boolean2) {
        JsonElement $$3 = jsonObject1.get(string0);
        if ($$3 != null) {
            return $$3.isJsonNull() ? boolean2 : $$3.getAsBoolean();
        } else {
            return boolean2;
        }
    }

    public static Date getDateOr(String string0, JsonObject jsonObject1) {
        JsonElement $$2 = jsonObject1.get(string0);
        return $$2 != null ? new Date(Long.parseLong($$2.getAsString())) : new Date();
    }
}