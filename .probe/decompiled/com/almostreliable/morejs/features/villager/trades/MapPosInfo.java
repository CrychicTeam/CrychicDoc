package com.almostreliable.morejs.features.villager.trades;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public record MapPosInfo(BlockPos pos, Component name) {

    @FunctionalInterface
    public interface Provider {

        @Nullable
        MapPosInfo apply(ServerLevel var1, Entity var2);
    }
}