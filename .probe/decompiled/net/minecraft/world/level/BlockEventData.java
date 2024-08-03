package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public record BlockEventData(BlockPos f_45529_, Block f_45530_, int f_45531_, int f_45532_) {

    private final BlockPos pos;

    private final Block block;

    private final int paramA;

    private final int paramB;

    public BlockEventData(BlockPos f_45529_, Block f_45530_, int f_45531_, int f_45532_) {
        this.pos = f_45529_;
        this.block = f_45530_;
        this.paramA = f_45531_;
        this.paramB = f_45532_;
    }
}