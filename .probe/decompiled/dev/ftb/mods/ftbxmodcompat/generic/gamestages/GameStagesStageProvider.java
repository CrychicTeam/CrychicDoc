package dev.ftb.mods.ftbxmodcompat.generic.gamestages;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.ftb.mods.ftblibrary.integration.stages.StageProvider;
import dev.ftb.mods.ftbxmodcompat.generic.gamestages.forge.GameStagesStageProviderImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class GameStagesStageProvider implements StageProvider {

    @Override
    public boolean has(Player player, String stage) {
        return hasStage(player, stage);
    }

    @Override
    public void add(ServerPlayer player, String stage) {
        addStage(player, stage);
    }

    @Override
    public void remove(ServerPlayer player, String stage) {
        removeStage(player, stage);
    }

    @Override
    public void sync(ServerPlayer player) {
        syncStages(player);
    }

    @Override
    public String getName() {
        return "Game Stages";
    }

    @ExpectPlatform
    @Transformed
    public static boolean hasStage(Player player, String stage) {
        return GameStagesStageProviderImpl.hasStage(player, stage);
    }

    @ExpectPlatform
    @Transformed
    public static void addStage(Player player, String stage) {
        GameStagesStageProviderImpl.addStage(player, stage);
    }

    @ExpectPlatform
    @Transformed
    public static void removeStage(Player player, String stage) {
        GameStagesStageProviderImpl.removeStage(player, stage);
    }

    @ExpectPlatform
    @Transformed
    public static void syncStages(Player player) {
        GameStagesStageProviderImpl.syncStages(player);
    }
}