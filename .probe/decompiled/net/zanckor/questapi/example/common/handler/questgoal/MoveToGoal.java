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

public class MoveToGoal extends ForgeAbstractGoal {

    @Override
    public void handler(ServerPlayer player, Entity entity, Gson gson, File file, UserQuest userQuest, int indexGoal, Enum questType) throws IOException {
        UserGoal questGoal = (UserGoal) userQuest.getQuestGoals().get(indexGoal);
        if (questGoal.getType().equals(EnumGoalType.MOVE_TO.toString())) {
            questGoal.setCurrentAmount(1);
            GsonManager.writeJson(file, userQuest);
            userQuest = (UserQuest) GsonManager.getJsonClass(file, UserQuest.class);
            super.handler(player, entity, gson, file, userQuest, indexGoal, questType);
        }
    }

    @Override
    public void enhancedCompleteQuest(ServerPlayer player, File file, UserGoal userGoal) {
    }

    @Override
    public void updateData(ServerPlayer player, File file) {
    }

    @Override
    public Enum getGoalType() {
        return EnumGoalType.MOVE_TO;
    }
}