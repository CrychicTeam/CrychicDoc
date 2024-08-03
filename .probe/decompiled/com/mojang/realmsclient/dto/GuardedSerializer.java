package com.mojang.realmsclient.dto;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import javax.annotation.Nullable;

public class GuardedSerializer {

    private final Gson gson = new Gson();

    public String toJson(ReflectionBasedSerialization reflectionBasedSerialization0) {
        return this.gson.toJson(reflectionBasedSerialization0);
    }

    public String toJson(JsonElement jsonElement0) {
        return this.gson.toJson(jsonElement0);
    }

    @Nullable
    public <T extends ReflectionBasedSerialization> T fromJson(String string0, Class<T> classT1) {
        return (T) this.gson.fromJson(string0, classT1);
    }
}