package dev.ftb.mods.ftblibrary.integration.stages;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class EntityTagStageProvider implements StageProvider {

    @Override
    public boolean has(Player player, String stage) {
        return player.m_19880_().contains(stage);
    }

    @Override
    public void add(ServerPlayer player, String stage) {
        player.m_20049_(stage);
    }

    @Override
    public void remove(ServerPlayer player, String stage) {
        player.m_20137_(stage);
    }

    @Override
    public String getName() {
        return "Entity Tags";
    }
}