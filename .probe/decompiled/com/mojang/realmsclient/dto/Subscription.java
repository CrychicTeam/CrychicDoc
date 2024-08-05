package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import org.slf4j.Logger;

public class Subscription extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    public long startDate;

    public int daysLeft;

    public Subscription.SubscriptionType type = Subscription.SubscriptionType.NORMAL;

    public static Subscription parse(String string0) {
        Subscription $$1 = new Subscription();
        try {
            JsonParser $$2 = new JsonParser();
            JsonObject $$3 = $$2.parse(string0).getAsJsonObject();
            $$1.startDate = JsonUtils.getLongOr("startDate", $$3, 0L);
            $$1.daysLeft = JsonUtils.getIntOr("daysLeft", $$3, 0);
            $$1.type = typeFrom(JsonUtils.getStringOr("subscriptionType", $$3, Subscription.SubscriptionType.NORMAL.name()));
        } catch (Exception var4) {
            LOGGER.error("Could not parse Subscription: {}", var4.getMessage());
        }
        return $$1;
    }

    private static Subscription.SubscriptionType typeFrom(String string0) {
        try {
            return Subscription.SubscriptionType.valueOf(string0);
        } catch (Exception var2) {
            return Subscription.SubscriptionType.NORMAL;
        }
    }

    public static enum SubscriptionType {

        NORMAL, RECURRING
    }
}