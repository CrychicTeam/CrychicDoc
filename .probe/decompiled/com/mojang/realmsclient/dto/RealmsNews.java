package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import org.slf4j.Logger;

public class RealmsNews extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    public String newsLink;

    public static RealmsNews parse(String string0) {
        RealmsNews $$1 = new RealmsNews();
        try {
            JsonParser $$2 = new JsonParser();
            JsonObject $$3 = $$2.parse(string0).getAsJsonObject();
            $$1.newsLink = JsonUtils.getStringOr("newsLink", $$3, null);
        } catch (Exception var4) {
            LOGGER.error("Could not parse RealmsNews: {}", var4.getMessage());
        }
        return $$1;
    }
}