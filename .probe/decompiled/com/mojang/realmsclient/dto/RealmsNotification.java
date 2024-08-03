package com.mojang.realmsclient.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class RealmsNotification {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String NOTIFICATION_UUID = "notificationUuid";

    private static final String DISMISSABLE = "dismissable";

    private static final String SEEN = "seen";

    private static final String TYPE = "type";

    private static final String VISIT_URL = "visitUrl";

    final UUID uuid;

    final boolean dismissable;

    final boolean seen;

    final String type;

    RealmsNotification(UUID uUID0, boolean boolean1, boolean boolean2, String string3) {
        this.uuid = uUID0;
        this.dismissable = boolean1;
        this.seen = boolean2;
        this.type = string3;
    }

    public boolean seen() {
        return this.seen;
    }

    public boolean dismissable() {
        return this.dismissable;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public static List<RealmsNotification> parseList(String string0) {
        List<RealmsNotification> $$1 = new ArrayList();
        try {
            for (JsonElement $$3 : JsonParser.parseString(string0).getAsJsonObject().get("notifications").getAsJsonArray()) {
                $$1.add(parse($$3.getAsJsonObject()));
            }
        } catch (Exception var5) {
            LOGGER.error("Could not parse list of RealmsNotifications", var5);
        }
        return $$1;
    }

    private static RealmsNotification parse(JsonObject jsonObject0) {
        UUID $$1 = JsonUtils.getUuidOr("notificationUuid", jsonObject0, null);
        if ($$1 == null) {
            throw new IllegalStateException("Missing required property notificationUuid");
        } else {
            boolean $$2 = JsonUtils.getBooleanOr("dismissable", jsonObject0, true);
            boolean $$3 = JsonUtils.getBooleanOr("seen", jsonObject0, false);
            String $$4 = JsonUtils.getRequiredString("type", jsonObject0);
            RealmsNotification $$5 = new RealmsNotification($$1, $$2, $$3, $$4);
            return (RealmsNotification) ("visitUrl".equals($$4) ? RealmsNotification.VisitUrl.parse($$5, jsonObject0) : $$5);
        }
    }

    public static class VisitUrl extends RealmsNotification {

        private static final String URL = "url";

        private static final String BUTTON_TEXT = "buttonText";

        private static final String MESSAGE = "message";

        private final String url;

        private final RealmsText buttonText;

        private final RealmsText message;

        private VisitUrl(RealmsNotification realmsNotification0, String string1, RealmsText realmsText2, RealmsText realmsText3) {
            super(realmsNotification0.uuid, realmsNotification0.dismissable, realmsNotification0.seen, realmsNotification0.type);
            this.url = string1;
            this.buttonText = realmsText2;
            this.message = realmsText3;
        }

        public static RealmsNotification.VisitUrl parse(RealmsNotification realmsNotification0, JsonObject jsonObject1) {
            String $$2 = JsonUtils.getRequiredString("url", jsonObject1);
            RealmsText $$3 = JsonUtils.getRequired("buttonText", jsonObject1, RealmsText::m_274486_);
            RealmsText $$4 = JsonUtils.getRequired("message", jsonObject1, RealmsText::m_274486_);
            return new RealmsNotification.VisitUrl(realmsNotification0, $$2, $$3, $$4);
        }

        public Component getMessage() {
            return this.message.createComponent(Component.translatable("mco.notification.visitUrl.message.default"));
        }

        public Button buildOpenLinkButton(Screen screen0) {
            Component $$1 = this.buttonText.createComponent(Component.translatable("mco.notification.visitUrl.buttonText.default"));
            return Button.builder($$1, ConfirmLinkScreen.confirmLink(this.url, screen0, true)).build();
        }
    }
}