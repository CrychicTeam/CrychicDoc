package com.mojang.realmsclient.dto;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import javax.annotation.Nullable;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class RealmsText {

    private static final String TRANSLATION_KEY = "translationKey";

    private static final String ARGS = "args";

    private final String translationKey;

    @Nullable
    private final Object[] args;

    private RealmsText(String string0, @Nullable Object[] object1) {
        this.translationKey = string0;
        this.args = object1;
    }

    public Component createComponent(Component component0) {
        if (!I18n.exists(this.translationKey)) {
            return component0;
        } else {
            return this.args == null ? Component.translatable(this.translationKey) : Component.translatable(this.translationKey, this.args);
        }
    }

    public static RealmsText parse(JsonObject jsonObject0) {
        String $$1 = JsonUtils.getRequiredString("translationKey", jsonObject0);
        JsonElement $$2 = jsonObject0.get("args");
        String[] $$5;
        if ($$2 != null && !$$2.isJsonNull()) {
            JsonArray $$4 = $$2.getAsJsonArray();
            $$5 = new String[$$4.size()];
            for (int $$6 = 0; $$6 < $$4.size(); $$6++) {
                $$5[$$6] = $$4.get($$6).getAsString();
            }
        } else {
            $$5 = null;
        }
        return new RealmsText($$1, $$5);
    }
}