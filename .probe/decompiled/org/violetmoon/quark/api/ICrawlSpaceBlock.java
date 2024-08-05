package org.violetmoon.quark.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ICrawlSpaceBlock {

    boolean canCrawl(Level var1, BlockState var2, BlockPos var3, Direction var4);

    default double crawlHeight(Level level, BlockState state, BlockPos pos, Direction direction) {
        return 0.13;
    }

    default boolean isLog(ServerPlayer sp, BlockState state, BlockPos pos, Direction direction) {
        return true;
    }
}