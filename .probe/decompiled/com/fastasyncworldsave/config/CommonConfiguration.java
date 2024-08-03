package com.fastasyncworldsave.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig {

    public boolean skipWeatherOnSleep = false;

    @Override
    public JsonObject serialize() {
        JsonObject root = new JsonObject();
        JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Whether to skip weather after sleeping: default:false");
        entry.addProperty("skipWeatherOnSleep", this.skipWeatherOnSleep);
        root.add("skipWeatherOnSleep", entry);
        return root;
    }

    @Override
    public void deserialize(JsonObject data) {
        this.skipWeatherOnSleep = data.get("skipWeatherOnSleep").getAsJsonObject().get("skipWeatherOnSleep").getAsBoolean();
    }
}