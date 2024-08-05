package net.zanckor.questapi.example.common.handler.questreward;

import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractReward;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerReward;
import net.zanckor.questapi.mod.core.filemanager.dialogquestregistry.enumquest.EnumQuestReward;

public class XpReward extends AbstractReward {

    @Override
    public void handler(ServerPlayer player, ServerQuest serverQuest, int rewardIndex) throws IOException {
        EnumQuestReward type = EnumQuestReward.valueOf(((ServerReward) serverQuest.getRewards().get(rewardIndex)).getTag());
        int quantity = ((ServerReward) serverQuest.getRewards().get(rewardIndex)).getAmount();
        switch(type) {
            case LEVEL:
                player.giveExperienceLevels(quantity);
                break;
            case POINTS:
                player.giveExperiencePoints(quantity);
        }
    }
}