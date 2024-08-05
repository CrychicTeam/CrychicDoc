package com.mojang.realmsclient.dto;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.List;

public class ServerActivityList extends ValueObject {

    public long periodInMillis;

    public List<ServerActivity> serverActivities = Lists.newArrayList();

    public static ServerActivityList parse(String string0) {
        ServerActivityList $$1 = new ServerActivityList();
        JsonParser $$2 = new JsonParser();
        try {
            JsonElement $$3 = $$2.parse(string0);
            JsonObject $$4 = $$3.getAsJsonObject();
            $$1.periodInMillis = JsonUtils.getLongOr("periodInMillis", $$4, -1L);
            JsonElement $$5 = $$4.get("playerActivityDto");
            if ($$5 != null && $$5.isJsonArray()) {
                for (JsonElement $$7 : $$5.getAsJsonArray()) {
                    ServerActivity $$8 = ServerActivity.parse($$7.getAsJsonObject());
                    $$1.serverActivities.add($$8);
                }
            }
        } catch (Exception var10) {
        }
        return $$1;
    }
}