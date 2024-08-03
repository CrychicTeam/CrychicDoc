package com.github.alexmodguy.alexscaves.server.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;

public class AcidSwimNodeEvaluator extends SwimNodeEvaluator {

    public AcidSwimNodeEvaluator(boolean breach) {
        super(breach);
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter getter, int x, int y, int z, Mob mob0) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i = x; i < x + this.f_77315_; i++) {
            for (int j = y; j < y + this.f_77316_; j++) {
                for (int k = z; k < z + this.f_77317_; k++) {
                    FluidState fluidstate = getter.getFluidState(blockpos$mutableblockpos.set(i, j, k));
                    BlockState blockstate = getter.getBlockState(blockpos$mutableblockpos.set(i, j, k));
                    if (fluidstate.isEmpty() && blockstate.m_60647_(getter, blockpos$mutableblockpos.m_7495_(), PathComputationType.WATER) && blockstate.m_60795_()) {
                        return BlockPathTypes.BREACH;
                    }
                    if (fluidstate.is(FluidTags.LAVA)) {
                        return BlockPathTypes.BLOCKED;
                    }
                }
            }
        }
        BlockState blockstate1 = getter.getBlockState(blockpos$mutableblockpos);
        return blockstate1.m_60647_(getter, blockpos$mutableblockpos, PathComputationType.WATER) ? BlockPathTypes.WATER : BlockPathTypes.BLOCKED;
    }
}