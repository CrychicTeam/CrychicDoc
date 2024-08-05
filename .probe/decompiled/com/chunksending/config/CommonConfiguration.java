package com.chunksending.config;

import com.chunksending.ChunkSending;
import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig {

    public int maxChunksPerTick = 5;

    public boolean debugLogging = false;

    @Override
    public JsonObject serialize() {
        JsonObject root = new JsonObject();
        JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Maximum amount of chunks sent per tick to a player, increases dynamically with size of the backlog");
        entry.addProperty("maxChunksPerTick", this.maxChunksPerTick);
        root.add("maxChunksPerTick", entry);
        JsonObject entry23 = new JsonObject();
        entry23.addProperty("desc:", "Enable debug logging to show the amount of chunks sent/queued");
        entry23.addProperty("debugLogging", this.debugLogging);
        root.add("debugLogging", entry23);
        return root;
    }

    @Override
    public void deserialize(JsonObject data) {
        if (data == null) {
            ChunkSending.LOGGER.error("Config file was empty!");
        } else {
            this.maxChunksPerTick = data.get("maxChunksPerTick").getAsJsonObject().get("maxChunksPerTick").getAsInt();
            this.debugLogging = data.get("debugLogging").getAsJsonObject().get("debugLogging").getAsBoolean();
        }
    }
}