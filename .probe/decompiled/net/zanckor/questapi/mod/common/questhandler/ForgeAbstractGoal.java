package net.zanckor.questapi.mod.common.questhandler;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractGoal;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractReward;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerReward;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.registry.EnumRegistry;
import net.zanckor.questapi.mod.common.network.SendQuestPacket;
import net.zanckor.questapi.mod.common.network.packet.quest.ActiveQuestList;
import net.zanckor.questapi.mod.common.network.packet.quest.ToastPacket;
import net.zanckor.questapi.mod.common.network.packet.screen.UpdateQuestTracked;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Util;

public abstract class ForgeAbstractGoal extends AbstractGoal {

    @Override
    public void handler(ServerPlayer player, Entity entity, Gson gson, File file, UserQuest userQuest, int indexGoal, Enum<?> questType) throws IOException {
        userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
        SendQuestPacket.TO_CLIENT(player, new UpdateQuestTracked(userQuest));
        if (Util.isQuestCompleted(userQuest)) {
            this.completeQuest(player, userQuest, file);
        }
    }

    @Override
    protected void completeQuest(ServerPlayer player, UserQuest userQuest, File file) throws IOException {
        if (userQuest != null) {
            this.callUpdate(userQuest, player, file);
            userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
            if (Util.isQuestCompleted(userQuest)) {
                userQuest.setCompleted(true);
                GsonManager.writeJson(file, userQuest);
                this.callEnhancedReward(userQuest, player, file);
                this.giveReward(player, file, userQuest);
                SendQuestPacket.TO_CLIENT(player, new ToastPacket(userQuest.getTitle()));
            }
            SendQuestPacket.TO_CLIENT(player, new ActiveQuestList(player.m_20148_()));
        }
    }

    @Override
    public abstract void enhancedCompleteQuest(ServerPlayer var1, File var2, UserGoal var3) throws IOException;

    @Override
    protected void giveReward(ServerPlayer player, File file, UserQuest userQuest) throws IOException {
        if (userQuest.isCompleted()) {
            String questID = userQuest.getId() + ".json";
            for (File serverFile : CommonMain.serverQuests.toFile().listFiles()) {
                if (serverFile.getName().contains(questID)) {
                    ServerQuest serverQuest = (ServerQuest) GsonManager.getJsonClass(serverFile, ServerQuest.class);
                    if (serverQuest.getRewards() == null) {
                        return;
                    }
                    for (int rewardIndex = 0; rewardIndex < serverQuest.getRewards().size(); rewardIndex++) {
                        Enum rewardEnum = EnumRegistry.getEnum(((ServerReward) serverQuest.getRewards().get(rewardIndex)).getType(), EnumRegistry.getQuestReward());
                        AbstractReward reward = QuestTemplateRegistry.getQuestReward(rewardEnum);
                        reward.handler(player, serverQuest, rewardIndex);
                    }
                    Util.moveFileToCompletedFolder(userQuest, player, file);
                    return;
                }
            }
        }
    }

    protected void callUpdate(UserQuest userQuest, ServerPlayer player, File file) throws IOException {
        for (UserGoal goal : userQuest.getQuestGoals()) {
            for (AbstractGoal abstractGoal : QuestTemplateRegistry.getAllGoals().values()) {
                if (goal.getType().equals(abstractGoal.getGoalType().toString())) {
                    abstractGoal.updateData(player, file);
                }
            }
        }
    }

    protected void callEnhancedReward(UserQuest userQuest, ServerPlayer player, File file) throws IOException {
        for (UserGoal goal : userQuest.getQuestGoals()) {
            for (AbstractGoal abstractGoal : QuestTemplateRegistry.getAllGoals().values()) {
                if (goal.getType().equals(abstractGoal.getGoalType().toString())) {
                    abstractGoal.enhancedCompleteQuest(player, file, goal);
                }
            }
        }
    }
}