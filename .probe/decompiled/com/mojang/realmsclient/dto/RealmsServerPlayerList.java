package com.mojang.realmsclient.dto;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.List;
import org.slf4j.Logger;

public class RealmsServerPlayerList extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final JsonParser JSON_PARSER = new JsonParser();

    public long serverId;

    public List<String> players;

    public static RealmsServerPlayerList parse(JsonObject jsonObject0) {
        RealmsServerPlayerList $$1 = new RealmsServerPlayerList();
        try {
            $$1.serverId = JsonUtils.getLongOr("serverId", jsonObject0, -1L);
            String $$2 = JsonUtils.getStringOr("playerList", jsonObject0, null);
            if ($$2 != null) {
                JsonElement $$3 = JSON_PARSER.parse($$2);
                if ($$3.isJsonArray()) {
                    $$1.players = parsePlayers($$3.getAsJsonArray());
                } else {
                    $$1.players = Lists.newArrayList();
                }
            } else {
                $$1.players = Lists.newArrayList();
            }
        } catch (Exception var4) {
            LOGGER.error("Could not parse RealmsServerPlayerList: {}", var4.getMessage());
        }
        return $$1;
    }

    private static List<String> parsePlayers(JsonArray jsonArray0) {
        List<String> $$1 = Lists.newArrayList();
        for (JsonElement $$2 : jsonArray0) {
            try {
                $$1.add($$2.getAsString());
            } catch (Exception var5) {
            }
        }
        return $$1;
    }
}