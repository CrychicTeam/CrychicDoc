package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.Date;
import org.slf4j.Logger;

public class PendingInvite extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    public String invitationId;

    public String worldName;

    public String worldOwnerName;

    public String worldOwnerUuid;

    public Date date;

    public static PendingInvite parse(JsonObject jsonObject0) {
        PendingInvite $$1 = new PendingInvite();
        try {
            $$1.invitationId = JsonUtils.getStringOr("invitationId", jsonObject0, "");
            $$1.worldName = JsonUtils.getStringOr("worldName", jsonObject0, "");
            $$1.worldOwnerName = JsonUtils.getStringOr("worldOwnerName", jsonObject0, "");
            $$1.worldOwnerUuid = JsonUtils.getStringOr("worldOwnerUuid", jsonObject0, "");
            $$1.date = JsonUtils.getDateOr("date", jsonObject0);
        } catch (Exception var3) {
            LOGGER.error("Could not parse PendingInvite: {}", var3.getMessage());
        }
        return $$1;
    }
}