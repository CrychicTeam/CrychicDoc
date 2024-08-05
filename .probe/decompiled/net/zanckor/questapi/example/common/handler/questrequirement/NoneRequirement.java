package net.zanckor.questapi.example.common.handler.questrequirement;

import java.io.IOException;
import net.minecraft.world.entity.player.Player;
import net.zanckor.questapi.api.file.quest.abstracquest.AbstractQuestRequirement;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;

public class NoneRequirement extends AbstractQuestRequirement {

    @Override
    public boolean handler(Player player, ServerQuest serverQuest, int requirementIndex) throws IOException {
        return true;
    }
}