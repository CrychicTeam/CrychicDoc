package com.almostreliable.summoningrituals.compat.kubejs;

import com.almostreliable.summoningrituals.recipe.AltarRecipe;
import dev.latvian.mods.kubejs.level.LevelEventJS;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class SummoningEventJS extends LevelEventJS {

    private final ServerLevel serverLevel;

    private final BlockPos pos;

    private final AltarRecipe recipe;

    @Nullable
    private final ServerPlayer player;

    SummoningEventJS(ServerLevel serverLevel, BlockPos pos, AltarRecipe recipe, @Nullable ServerPlayer player) {
        this.serverLevel = serverLevel;
        this.pos = pos;
        this.recipe = recipe;
        this.player = player;
    }

    public ServerLevel getLevel() {
        return this.serverLevel;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public AltarRecipe getRecipe() {
        return this.recipe;
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return this.player;
    }
}