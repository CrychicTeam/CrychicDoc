package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import javax.annotation.Nullable;
import org.slf4j.Logger;

public class WorldTemplate extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    public String id = "";

    public String name = "";

    public String version = "";

    public String author = "";

    public String link = "";

    @Nullable
    public String image;

    public String trailer = "";

    public String recommendedPlayers = "";

    public WorldTemplate.WorldTemplateType type = WorldTemplate.WorldTemplateType.WORLD_TEMPLATE;

    public static WorldTemplate parse(JsonObject jsonObject0) {
        WorldTemplate $$1 = new WorldTemplate();
        try {
            $$1.id = JsonUtils.getStringOr("id", jsonObject0, "");
            $$1.name = JsonUtils.getStringOr("name", jsonObject0, "");
            $$1.version = JsonUtils.getStringOr("version", jsonObject0, "");
            $$1.author = JsonUtils.getStringOr("author", jsonObject0, "");
            $$1.link = JsonUtils.getStringOr("link", jsonObject0, "");
            $$1.image = JsonUtils.getStringOr("image", jsonObject0, null);
            $$1.trailer = JsonUtils.getStringOr("trailer", jsonObject0, "");
            $$1.recommendedPlayers = JsonUtils.getStringOr("recommendedPlayers", jsonObject0, "");
            $$1.type = WorldTemplate.WorldTemplateType.valueOf(JsonUtils.getStringOr("type", jsonObject0, WorldTemplate.WorldTemplateType.WORLD_TEMPLATE.name()));
        } catch (Exception var3) {
            LOGGER.error("Could not parse WorldTemplate: {}", var3.getMessage());
        }
        return $$1;
    }

    public static enum WorldTemplateType {

        WORLD_TEMPLATE, MINIGAME, ADVENTUREMAP, EXPERIENCE, INSPIRATION
    }
}