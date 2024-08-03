package com.mojang.realmsclient.dto;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;

public class BackupList extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    public List<Backup> backups;

    public static BackupList parse(String string0) {
        JsonParser $$1 = new JsonParser();
        BackupList $$2 = new BackupList();
        $$2.backups = Lists.newArrayList();
        try {
            JsonElement $$3 = $$1.parse(string0).getAsJsonObject().get("backups");
            if ($$3.isJsonArray()) {
                Iterator<JsonElement> $$4 = $$3.getAsJsonArray().iterator();
                while ($$4.hasNext()) {
                    $$2.backups.add(Backup.parse((JsonElement) $$4.next()));
                }
            }
        } catch (Exception var5) {
            LOGGER.error("Could not parse BackupList: {}", var5.getMessage());
        }
        return $$2;
    }
}