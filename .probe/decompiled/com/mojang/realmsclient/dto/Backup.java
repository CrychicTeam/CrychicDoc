package com.mojang.realmsclient.dto;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;

public class Backup extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    public String backupId;

    public Date lastModifiedDate;

    public long size;

    private boolean uploadedVersion;

    public Map<String, String> metadata = Maps.newHashMap();

    public Map<String, String> changeList = Maps.newHashMap();

    public static Backup parse(JsonElement jsonElement0) {
        JsonObject $$1 = jsonElement0.getAsJsonObject();
        Backup $$2 = new Backup();
        try {
            $$2.backupId = JsonUtils.getStringOr("backupId", $$1, "");
            $$2.lastModifiedDate = JsonUtils.getDateOr("lastModifiedDate", $$1);
            $$2.size = JsonUtils.getLongOr("size", $$1, 0L);
            if ($$1.has("metadata")) {
                JsonObject $$3 = $$1.getAsJsonObject("metadata");
                for (Entry<String, JsonElement> $$5 : $$3.entrySet()) {
                    if (!((JsonElement) $$5.getValue()).isJsonNull()) {
                        $$2.metadata.put((String) $$5.getKey(), ((JsonElement) $$5.getValue()).getAsString());
                    }
                }
            }
        } catch (Exception var7) {
            LOGGER.error("Could not parse Backup: {}", var7.getMessage());
        }
        return $$2;
    }

    public boolean isUploadedVersion() {
        return this.uploadedVersion;
    }

    public void setUploadedVersion(boolean boolean0) {
        this.uploadedVersion = boolean0;
    }
}