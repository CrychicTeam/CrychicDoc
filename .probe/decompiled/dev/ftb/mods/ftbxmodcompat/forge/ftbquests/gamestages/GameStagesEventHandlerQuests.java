package dev.ftb.mods.ftbxmodcompat.forge.ftbquests.gamestages;

import dev.ftb.mods.ftbquests.quest.task.StageTask;
import net.darkhax.gamestages.event.GameStageEvent.Added;
import net.darkhax.gamestages.event.GameStageEvent.Cleared;
import net.darkhax.gamestages.event.GameStageEvent.Removed;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;

public class GameStagesEventHandlerQuests {

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(GameStagesEventHandlerQuests::onStageAdded);
        MinecraftForge.EVENT_BUS.addListener(GameStagesEventHandlerQuests::onStageRemoved);
        MinecraftForge.EVENT_BUS.addListener(GameStagesEventHandlerQuests::onStagesCleared);
    }

    private static void onStageAdded(Added event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            StageTask.checkStages(sp);
        }
    }

    private static void onStageRemoved(Removed event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            StageTask.checkStages(sp);
        }
    }

    private static void onStagesCleared(Cleared event) {
        if (event.getEntity() instanceof ServerPlayer sp) {
            StageTask.checkStages(sp);
        }
    }
}