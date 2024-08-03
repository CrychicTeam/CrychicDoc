package net.zanckor.questapi.mod.common.network.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.registry.ScreenRegistry;
import net.zanckor.questapi.api.screen.AbstractDialog;
import net.zanckor.questapi.api.screen.NpcType;
import net.zanckor.questapi.mod.common.util.MCUtilClient;
import net.zanckor.questapi.util.Timer;

@EventBusSubscriber(modid = "questapi", value = { Dist.CLIENT })
public class ClientHandler {

    public static List<UserQuest> trackedQuestList = new ArrayList();

    public static List<UserQuest> activeQuestList;

    public static List<EntityType> availableEntityTypeForQuest = new ArrayList();

    public static Map<String, String> availableEntityTagForQuest = new HashMap();

    public static void toastQuestCompleted(String questName) {
        String title = I18n.get(questName);
        SystemToast.add(Minecraft.getInstance().getToasts(), SystemToast.SystemToastIds.PERIODIC_NOTIFICATION, Component.literal("Quest Completed"), Component.literal(title));
        MCUtilClient.playSound((SoundEvent) SoundEvents.NOTE_BLOCK_PLING.get(), 1.0F, 2.0F);
    }

    public static void displayDialog(String dialogIdentifier, int dialogID, String text, int optionSize, HashMap<Integer, List<Integer>> optionIntegers, HashMap<Integer, List<String>> optionStrings, UUID entity, String resourceLocation, Item item, NpcType npcType) {
        AbstractDialog dialogScreen = ScreenRegistry.getDialogScreen(dialogIdentifier);
        Minecraft.getInstance().setScreen(dialogScreen.modifyScreen(dialogID, text, optionSize, optionIntegers, optionStrings, entity, resourceLocation, item, npcType));
    }

    public static void closeDialog() {
        Minecraft.getInstance().setScreen(null);
    }

    public static void modifyTrackedQuests(Boolean addQuest, UserQuest userQuest) {
        if (addQuest) {
            AtomicInteger totalGoals = new AtomicInteger();
            trackedQuestList.forEach(quest -> totalGoals.addAndGet(quest.getQuestGoals().size()));
            if (totalGoals.get() < 5) {
                trackedQuestList.add(userQuest);
            }
        } else {
            trackedQuestList.removeIf(quest -> quest.getId().equals(userQuest.getId()));
        }
    }

    public static void updateQuestTracked(UserQuest userQuest) {
        trackedQuestList.forEach(quest -> {
            if (userQuest.getId().equals(quest.getId())) {
                quest.setQuestGoals(userQuest.getQuestGoals());
            }
        });
    }

    public static void removeQuest(String id) {
        Timer.clearTimer(Minecraft.getInstance().player.m_20148_(), "TIMER_QUEST" + id);
    }

    public static void setActiveQuestList(List<UserQuest> activeQuestList) {
        ClientHandler.activeQuestList = activeQuestList;
    }

    public static void setAvailableEntityTypeForQuest(List<String> entityTypeForQuest, Map<String, String> entityTagMap) {
        for (String entityTypeString : entityTypeForQuest) {
            if (entityTypeString.isBlank()) {
                return;
            }
            ResourceLocation entityResourceLocation = new ResourceLocation(entityTypeString.strip());
            EntityType entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityResourceLocation);
            availableEntityTypeForQuest.add(entityType);
        }
        availableEntityTagForQuest = entityTagMap;
    }
}