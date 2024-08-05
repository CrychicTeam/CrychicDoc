package net.zanckor.questapi.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.api.file.dialog.codec.ReadConversation;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.registry.EnumRegistry;

public class Util {

    public static ConcurrentHashMap<String, Integer> lastDialogPerConversation = new ConcurrentHashMap();

    public static boolean isQuestCompleted(UserQuest userQuest) {
        int indexGoals = 0;
        for (UserGoal questGoal : userQuest.getQuestGoals()) {
            indexGoals++;
            if (questGoal.getCurrentAmount() < questGoal.getAmount()) {
                return false;
            }
            if (indexGoals >= userQuest.getQuestGoals().size()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConversationCompleted(String conversationID, Player player) throws IOException {
        Path userFolder = Paths.get(CommonMain.playerData.toString(), player.m_20148_().toString());
        Path readDialogPath = Paths.get(CommonMain.getReadDialogs(userFolder).toString(), File.separator, "dialog_read.json");
        File readDialogFile = readDialogPath.toFile();
        Optional<ReadConversation> readConversation = readDialogFile.exists() ? Optional.ofNullable((ReadConversation) GsonManager.getJsonClass(readDialogFile, ReadConversation.class)) : Optional.empty();
        List<Integer> dialogIdList = ((ReadConversation) readConversation.orElse(new ReadConversation())).getConversation(conversationID);
        Conversation conversation = QuestDialogManager.getConversationByID(conversationID);
        int highestDialogID = 0;
        for (Integer dialogID : dialogIdList) {
            highestDialogID = Math.max(highestDialogID, dialogID);
        }
        lastDialogPerConversation.put(conversationID, highestDialogID);
        return highestDialogID >= conversation.getDialog().size();
    }

    public static void moveFileToCompletedFolder(UserQuest userQuest, ServerPlayer player, File file) throws IOException {
        Path userFolder = Paths.get(CommonMain.playerData.toFile().toString(), player.m_20148_().toString());
        Path completedPath = Paths.get(CommonMain.getCompletedQuest(userFolder).toString(), file.getName());
        String id = file.getName().substring(0, file.getName().length() - 5);
        Files.deleteIfExists(Paths.get(CommonMain.getCompletedQuest(userFolder).toString(), file.getName()));
        Files.move(file.toPath(), completedPath);
        for (int indexGoals = 0; indexGoals < userQuest.getQuestGoals().size(); indexGoals++) {
            Enum<?> goalEnum = EnumRegistry.getEnum(((UserGoal) userQuest.getQuestGoals().get(indexGoals)).getType(), EnumRegistry.getQuestGoal());
            QuestDialogManager.movePathQuest(id, Paths.get(CommonMain.getCompletedQuest(userFolder).toString(), file.getName()), goalEnum);
        }
    }

    public static void moveFileToUncompletedFolder(Path uncompletedQuestFolder, File file, UserQuest userQuest, Enum<?> goalEnum) throws IOException {
        Path uncompletedPath = Paths.get(uncompletedQuestFolder.toString(), file.getName());
        String id = file.getName().substring(0, file.getName().length() - 5);
        if (file.exists()) {
            Files.move(file.toPath(), uncompletedPath);
        }
        QuestDialogManager.movePathQuest(id, uncompletedPath, goalEnum);
    }

    public static Entity getEntityByUUID(ServerLevel level, UUID uuid) {
        for (Entity entity : level.getAllEntities()) {
            if (entity.getUUID().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }

    public static List<Integer> findSlotMatchingItemStack(ItemStack itemStack, Inventory inventory) {
        List<Integer> slots = new ArrayList();
        for (int itemSlot = 0; itemSlot < inventory.items.size(); itemSlot++) {
            if (!inventory.items.get(itemSlot).isEmpty() && ItemStack.isSameItem(itemStack, inventory.items.get(itemSlot))) {
                slots.add(itemSlot);
            }
        }
        return slots;
    }

    public static void createQuest(ServerQuest serverQuest, Player player, Path path) throws IOException {
        UserQuest userQuest = UserQuest.createQuest(serverQuest, path);
        if (userQuest.hasTimeLimit()) {
            Timer.updateCooldown(player.m_20148_(), userQuest.getId(), (float) userQuest.getTimeLimitInSeconds());
        }
        GsonManager.writeJson(path.toFile(), userQuest);
    }

    public static int getFreeSlots(Player player) {
        Inventory inventory = player.getInventory();
        int freeSlotQuantity = 0;
        for (int slot = 0; slot < inventory.items.size(); slot++) {
            if (inventory.items.get(slot).isEmpty()) {
                freeSlotQuantity++;
            }
        }
        return freeSlotQuantity;
    }

    public static boolean hasQuest(String quest, Path userFolder) {
        return Files.exists(Paths.get(CommonMain.getCompletedQuest(userFolder).toString(), quest), new LinkOption[0]) || Files.exists(Paths.get(CommonMain.getActiveQuest(userFolder).toString(), quest), new LinkOption[0]) || Files.exists(Paths.get(CommonMain.getFailedQuest(userFolder).toString(), quest), new LinkOption[0]);
    }
}