package com.mojang.realmsclient.util;

import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.dto.GuardedSerializer;
import com.mojang.realmsclient.dto.ReflectionBasedSerialization;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;

public class RealmsPersistence {

    private static final String FILE_NAME = "realms_persistence.json";

    private static final GuardedSerializer GSON = new GuardedSerializer();

    private static final Logger LOGGER = LogUtils.getLogger();

    public RealmsPersistence.RealmsPersistenceData read() {
        return readFile();
    }

    public void save(RealmsPersistence.RealmsPersistenceData realmsPersistenceRealmsPersistenceData0) {
        writeFile(realmsPersistenceRealmsPersistenceData0);
    }

    public static RealmsPersistence.RealmsPersistenceData readFile() {
        Path $$0 = getPathToData();
        try {
            String $$1 = Files.readString($$0, StandardCharsets.UTF_8);
            RealmsPersistence.RealmsPersistenceData $$2 = GSON.fromJson($$1, RealmsPersistence.RealmsPersistenceData.class);
            if ($$2 != null) {
                return $$2;
            }
        } catch (NoSuchFileException var3) {
        } catch (Exception var4) {
            LOGGER.warn("Failed to read Realms storage {}", $$0, var4);
        }
        return new RealmsPersistence.RealmsPersistenceData();
    }

    public static void writeFile(RealmsPersistence.RealmsPersistenceData realmsPersistenceRealmsPersistenceData0) {
        Path $$1 = getPathToData();
        try {
            Files.writeString($$1, GSON.toJson(realmsPersistenceRealmsPersistenceData0), StandardCharsets.UTF_8);
        } catch (Exception var3) {
        }
    }

    private static Path getPathToData() {
        return Minecraft.getInstance().gameDirectory.toPath().resolve("realms_persistence.json");
    }

    public static class RealmsPersistenceData implements ReflectionBasedSerialization {

        @SerializedName("newsLink")
        public String newsLink;

        @SerializedName("hasUnreadNews")
        public boolean hasUnreadNews;
    }
}