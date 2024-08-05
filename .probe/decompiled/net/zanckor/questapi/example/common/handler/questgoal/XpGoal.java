package net.zanckor.questapi.example.common.handler.questgoal;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.zanckor.questapi.api.file.quest.codec.user.UserGoal;
import net.zanckor.questapi.api.file.quest.codec.user.UserQuest;
import net.zanckor.questapi.mod.common.questhandler.ForgeAbstractGoal;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumGoalType;
import net.zanckor.questapi.util.GsonManager;

public class XpGoal extends ForgeAbstractGoal {

    @Override
    public void handler(ServerPlayer player, Entity entity, Gson gson, File file, UserQuest userQuest, int indexGoal, Enum questType) throws IOException {
        userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
        UserGoal questGoal = (UserGoal) userQuest.getQuestGoals().get(indexGoal);
        if (questGoal.getCurrentAmount() != null) {
            int currentAmount = player.f_36078_ >= questGoal.getAmount() ? questGoal.getAmount() : player.f_36078_;
            questGoal.setCurrentAmount(currentAmount);
            GsonManager.writeJson(file, userQuest);
            userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
            super.handler(player, entity, gson, file, userQuest, indexGoal, questType);
        }
    }

    @Override
    public void enhancedCompleteQuest(ServerPlayer player, File file, UserGoal userGoal) {
        player.setExperienceLevels(player.f_36078_ - userGoal.getAmount());
    }

    @Override
    public void updateData(ServerPlayer player, File file) {
    }

    @Override
    public Enum getGoalType() {
        return EnumGoalType.XP;
    }
}