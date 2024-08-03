package net.zanckor.questapi.api.file.quest.abstracquest;

import java.io.IOException;
import net.minecraft.server.level.ServerPlayer;
import net.zanckor.questapi.api.file.quest.codec.server.ServerQuest;

public abstract class AbstractReward {

    public abstract void handler(ServerPlayer var1, ServerQuest var2, int var3) throws IOException;
}