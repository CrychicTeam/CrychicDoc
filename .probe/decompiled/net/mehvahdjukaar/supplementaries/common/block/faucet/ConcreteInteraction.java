package net.mehvahdjukaar.supplementaries.common.block.faucet;

import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SugarBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.state.BlockState;

class ConcreteInteraction implements FaucetTarget.BlState {

    public Integer fill(Level level, BlockPos pos, BlockState state, SoftFluidStack fluid, int minAmount) {
        if (fluid.is(BuiltInSoftFluids.WATER.get())) {
            if (state.m_60734_() instanceof SugarBlock) {
                level.blockEvent(pos, state.m_60734_(), 1, 0);
                return 1;
            }
            if (state.m_60734_() instanceof ConcretePowderBlock cp) {
                level.setBlock(pos, cp.concrete, 3);
                return minAmount;
            }
        }
        return null;
    }
}