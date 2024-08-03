package dev.ftb.mods.ftblibrary.integration.stages;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface StageProvider {

    boolean has(Player var1, String var2);

    void add(ServerPlayer var1, String var2);

    void remove(ServerPlayer var1, String var2);

    default void sync(ServerPlayer player) {
    }

    String getName();
}