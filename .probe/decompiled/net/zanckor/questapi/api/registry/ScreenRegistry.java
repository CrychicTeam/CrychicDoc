package net.zanckor.questapi.api.registry;

import java.util.HashMap;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.screen.AbstractDialog;
import net.zanckor.questapi.api.screen.AbstractQuestLog;
import net.zanckor.questapi.api.screen.AbstractQuestTracked;

public class ScreenRegistry {

    private static final HashMap<String, AbstractDialog> dialog = new HashMap();

    private static final HashMap<String, AbstractQuestTracked> tracked_screen = new HashMap();

    private static final HashMap<String, AbstractQuestLog> quest_log_screen = new HashMap();

    public static void registerDialogScreen(String identifier, AbstractDialog screen) {
        dialog.put(identifier, screen);
    }

    public static AbstractDialog getDialogScreen(String identifier) {
        AbstractDialog abstractDialog = (AbstractDialog) dialog.get(identifier);
        if (abstractDialog != null) {
            return abstractDialog;
        } else {
            errorMessage("on DialogScreen " + identifier);
            return (AbstractDialog) dialog.get("questapi");
        }
    }

    public static void registerQuestTrackedScreen(String identifier, AbstractQuestTracked screen) {
        tracked_screen.put(identifier, screen);
    }

    public static AbstractQuestTracked getQuestTrackedScreen(String identifier) {
        AbstractQuestTracked trackedScreen = (AbstractQuestTracked) tracked_screen.get(identifier);
        if (trackedScreen != null) {
            return trackedScreen;
        } else {
            errorMessage("on QuestTracked " + identifier);
            return (AbstractQuestTracked) tracked_screen.get("questapi");
        }
    }

    public static void registerQuestLogScreen(String identifier, AbstractQuestLog screen) {
        quest_log_screen.put(identifier, screen);
    }

    public static AbstractQuestLog getQuestLogScreen(String identifier) {
        AbstractQuestLog questLogScreen = (AbstractQuestLog) quest_log_screen.get(identifier);
        if (questLogScreen != null) {
            return questLogScreen;
        } else {
            errorMessage("on QuestLog " + identifier);
            return (AbstractQuestLog) quest_log_screen.get("questapi");
        }
    }

    private static void errorMessage(String identifier) {
        CommonMain.Constants.LOG.error("Your identifier " + identifier + " is incorrect or you have no screen registered");
    }
}