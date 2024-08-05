package net.mehvahdjukaar.supplementaries.common.block.faucet;

import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

class BeehiveInteraction implements FaucetTarget.BlState, FaucetSource.BlState {

    public FluidOffer getProvidedFluid(Level level, BlockPos pos, Direction dir, BlockState state) {
        return state.m_61138_(BlockStateProperties.LEVEL_HONEY) && state.m_61143_(BlockStateProperties.LEVEL_HONEY) == 5 ? FluidOffer.of(BuiltInSoftFluids.HONEY.getHolder()) : null;
    }

    public void drain(Level level, BlockPos pos, Direction dir, BlockState source, int amount) {
        level.setBlock(pos, (BlockState) source.m_61124_(BlockStateProperties.LEVEL_HONEY, 0), 3);
    }

    public Integer fill(Level level, BlockPos pos, BlockState state, SoftFluidStack fluid, int minAmount) {
        if (state.m_61138_(BlockStateProperties.LEVEL_HONEY)) {
            if (fluid.is(BuiltInSoftFluids.HONEY.get()) && fluid.getCount() == 1 && (Integer) state.m_61143_(BlockStateProperties.LEVEL_HONEY) == 0) {
                level.setBlock(pos, (BlockState) state.m_61124_(BlockStateProperties.LEVEL_HONEY, 5), 3);
                return minAmount;
            } else {
                return 0;
            }
        } else {
            return null;
        }
    }
}