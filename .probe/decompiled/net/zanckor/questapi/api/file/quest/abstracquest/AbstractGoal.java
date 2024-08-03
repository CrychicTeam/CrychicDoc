package net.zanckor.questapi.api.file.quest.abstracquest;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.zanckor.questapi.CommonMain;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerReward;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.api.file.quest.register.QuestTemplateRegistry;
import net.zanckor.questapi.api.registry.EnumRegistry;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Util;

public abstract class AbstractGoal {

    public abstract void handler(ServerPlayer var1, Entity var2, Gson var3, File var4, UserQuest var5, int var6, Enum<?> var7) throws IOException;

    protected abstract void completeQuest(ServerPlayer var1, UserQuest var2, File var3) throws IOException;

    public abstract void enhancedCompleteQuest(ServerPlayer var1, File var2, UserGoal var3) throws IOException;

    protected void giveReward(ServerPlayer player, File file, UserQuest userQuest) throws IOException {
        if (userQuest.isCompleted()) {
            String questID = userQuest.getId() + ".json";
            for (File serverFile : CommonMain.serverQuests.toFile().listFiles()) {
                if (serverFile.getName().equals(questID)) {
                    ServerQuest serverQuest = (ServerQuest) GsonManager.getJsonClass(serverFile, ServerQuest.class);
                    if (serverQuest.getRewards() == null) {
                        return;
                    }
                    for (int rewardIndex = 0; rewardIndex < serverQuest.getRewards().size(); rewardIndex++) {
                        Enum<?> rewardEnum = EnumRegistry.getEnum(((ServerReward) serverQuest.getRewards().get(rewardIndex)).getType(), EnumRegistry.getQuestReward());
                        AbstractReward reward = QuestTemplateRegistry.getQuestReward(rewardEnum);
                        reward.handler(player, serverQuest, rewardIndex);
                    }
                    Util.moveFileToCompletedFolder(userQuest, player, file);
                    return;
                }
            }
        }
    }

    public abstract void updateData(ServerPlayer var1, File var2) throws IOException;

    public abstract Enum<?> getGoalType();
}