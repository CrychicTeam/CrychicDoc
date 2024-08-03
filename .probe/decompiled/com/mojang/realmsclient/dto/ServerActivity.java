package com.mojang.realmsclient.dto;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;

public class ServerActivity extends ValueObject {

    public String profileUuid;

    public long joinTime;

    public long leaveTime;

    public static ServerActivity parse(JsonObject jsonObject0) {
        ServerActivity $$1 = new ServerActivity();
        try {
            $$1.profileUuid = JsonUtils.getStringOr("profileUuid", jsonObject0, null);
            $$1.joinTime = JsonUtils.getLongOr("joinTime", jsonObject0, Long.MIN_VALUE);
            $$1.leaveTime = JsonUtils.getLongOr("leaveTime", jsonObject0, Long.MIN_VALUE);
        } catch (Exception var3) {
        }
        return $$1;
    }
}