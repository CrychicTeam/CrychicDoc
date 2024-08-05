package net.zanckor.questapi.example.common.handler.questreward;

import java.io.IOException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractReward;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;
import net.zanckor.questapi.api.file.quest.codec.server.ServerReward;

public class CommandReward extends AbstractReward {

    @Override
    public void handler(ServerPlayer player, ServerQuest serverQuest, int rewardIndex) throws IOException {
        String command = ((ServerReward) serverQuest.getRewards().get(rewardIndex)).getTag();
        if (command.contains("@p")) {
            command.replace("@p", player.m_6302_());
        }
        int quantity = ((ServerReward) serverQuest.getRewards().get(rewardIndex)).getAmount();
        CommandSourceStack sourceStack = player.m_20194_().createCommandSourceStack();
        for (int timesExecuted = 0; timesExecuted < quantity; timesExecuted++) {
            sourceStack.getServer().getCommands().performPrefixedCommand(sourceStack, command);
        }
    }
}