package net.zanckor.questapi;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import net.zanckor.questapi.multiloader.platform.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonMain {

    public static Path serverDirectory;

    public static Path questApi;

    public static Path playerData;

    public static Path serverQuests;

    public static Path serverDialogs;

    public static Path serverNPC;

    public static Path entity_type_list;

    public static Path compoundTag_List;

    public static void init() {
        CommonMain.Constants.LOG.info("Common init on {}! Currently in a {} environment!", Services.PLATFORM.getPlatform(), Services.PLATFORM.getEnvironmentName());
    }

    public static Path getUserFolder(UUID playerUUID) {
        return Paths.get(playerData.toString(), playerUUID.toString());
    }

    public static Path getActiveQuest(Path userFolder) {
        return Paths.get(userFolder.toString(), "active-quests");
    }

    public static Path getCompletedQuest(Path userFolder) {
        return Paths.get(userFolder.toString(), "completed-quests");
    }

    public static Path getFailedQuest(Path userFolder) {
        return Paths.get(userFolder.toString(), "uncompleted-quests");
    }

    public static Path getReadDialogs(Path userFolder) {
        return Paths.get(userFolder.toString(), "read-dialogs");
    }

    public static class Constants {

        public static final String MOD_ID = "questapi";

        public static final String MOD_NAME = "QuestAPI Multiloader";

        public static final Logger LOG = LoggerFactory.getLogger("QuestAPI Multiloader");
    }
}