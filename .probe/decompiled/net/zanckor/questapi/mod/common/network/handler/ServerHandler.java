package net.zanckor.questapi.mod.common.network.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.data.QuestDialogManager;
import net.zanckor.questapi.api.file.dialog.abstractdialog.AbstractDialogOption;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.screen.NpcType;
import net.zanckor.questapi.mod.common.util.MCUtil;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Timer;

public class ServerHandler {

    public static void addQuest(Player player, Enum optionType, int optionID) {
        String dialogGlobalID = (String) QuestDialogManager.currentGlobalDialog.get(player);
        Path path = QuestDialogManager.getConversationPathLocation(dialogGlobalID);
        File dialogFile = path.toFile();
        AbstractDialogOption dialogTemplate = QuestTemplateRegistry.getDialogTemplate(optionType);
        try {
            Conversation dialog = (Conversation) GsonManager.getJsonClass(dialogFile, Conversation.class);
            dialogTemplate.handler(player, dialog, optionID, (Entity) null);
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }
    }

    public static void questHandler(Enum questType, ServerPlayer player, LivingEntity entity) throws IOException {
        AbstractGoal quest = QuestTemplateRegistry.getQuestTemplate(questType);
        List<Path> questTypeLocation = QuestDialogManager.getQuestTypePathLocation(questType);
        if (quest != null && questTypeLocation != null && !questTypeLocation.isEmpty()) {
            for (Path path : new CopyOnWriteArrayList(QuestDialogManager.getQuestTypePathLocation(questType))) {
                File file = path.toAbsolutePath().toFile();
                UserQuest userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
                if (userQuest != null && !userQuest.isCompleted()) {
                    List<UserGoal> questGoals = new ArrayList(userQuest.getQuestGoals());
                    for (int indexGoals = 0; indexGoals < questGoals.size(); indexGoals++) {
                        userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
                        if (userQuest == null) {
                            return;
                        }
                        UserGoal questGoal = (UserGoal) questGoals.get(indexGoals);
                        if (questGoal.getType().equals(questType.toString())) {
                            quest.handler(player, entity, GsonManager.gson, file, userQuest, indexGoals, questType);
                        }
                    }
                }
            }
        }
    }

    public static void requestDialog(ServerPlayer player, int optionID, Enum optionType, UUID entityUUID, Item item, NpcType npcType) {
        String globalDialogID = (String) QuestDialogManager.currentGlobalDialog.get(player);
        Path path = QuestDialogManager.getConversationPathLocation(globalDialogID);
        File dialogFile = path.toFile();
        AbstractDialogOption dialogTemplate = QuestTemplateRegistry.getDialogTemplate(optionType);
        try {
            Conversation dialog = (Conversation) GsonManager.getJsonClass(dialogFile, Conversation.class);
            switch(npcType) {
                case ITEM:
                    dialogTemplate.handler(player, dialog, optionID, item);
                    break;
                case RESOURCE_LOCATION:
                case UUID:
                    dialogTemplate.handler(player, dialog, optionID, MCUtil.getEntityByUUID((ServerLevel) player.m_9236_(), entityUUID));
            }
        } catch (IOException var11) {
            throw new RuntimeException(var11);
        }
    }

    public static void questTimer(ServerLevel level) {
        for (Player player : level.players()) {
            Path userFolder = Paths.get(CommonMain.playerData.toString(), player.m_20148_().toString());
            for (File file : userFolder.toFile().listFiles()) {
                try {
                    UserQuest userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
                    if (userQuest.isCompleted() || !userQuest.hasTimeLimit() || !Timer.canUseWithCooldown(player.m_20148_(), userQuest.getId(), (float) userQuest.getTimeLimitInSeconds())) {
                        return;
                    }
                    userQuest.setCompleted(true);
                    GsonManager.writeJson(file, userQuest);
                } catch (IOException var9) {
                    CommonMain.Constants.LOG.error(var9.getMessage());
                }
            }
        }
    }
}