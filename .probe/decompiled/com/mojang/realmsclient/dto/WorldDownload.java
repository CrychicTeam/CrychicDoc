package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import org.slf4j.Logger;

public class WorldDownload extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    public String downloadLink;

    public String resourcePackUrl;

    public String resourcePackHash;

    public static WorldDownload parse(String string0) {
        JsonParser $$1 = new JsonParser();
        JsonObject $$2 = $$1.parse(string0).getAsJsonObject();
        WorldDownload $$3 = new WorldDownload();
        try {
            $$3.downloadLink = JsonUtils.getStringOr("downloadLink", $$2, "");
            $$3.resourcePackUrl = JsonUtils.getStringOr("resourcePackUrl", $$2, "");
            $$3.resourcePackHash = JsonUtils.getStringOr("resourcePackHash", $$2, "");
        } catch (Exception var5) {
            LOGGER.error("Could not parse WorldDownload: {}", var5.getMessage());
        }
        return $$3;
    }
}