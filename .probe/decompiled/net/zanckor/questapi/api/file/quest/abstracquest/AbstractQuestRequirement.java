package net.zanckor.questapi.api.file.quest.abstracquest;

import java.io.IOException;
import net.minecraft.world.entity.player.Player;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;

public abstract class AbstractQuestRequirement {

    public abstract boolean handler(Player var1, ServerQuest var2, int var3) throws IOException;
}