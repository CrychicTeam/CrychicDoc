package net.mehvahdjukaar.supplementaries.common.block.faucet;

import net.mehvahdjukaar.moonlight.api.block.ISoftFluidConsumer;
import net.mehvahdjukaar.moonlight.api.block.ISoftFluidProvider;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

class SoftFluidProviderInteraction implements FaucetTarget.BlState, FaucetSource.BlState {

    public FluidOffer getProvidedFluid(Level level, BlockPos pos, Direction dir, BlockState state) {
        return state.m_60734_() instanceof ISoftFluidProvider p ? FluidOffer.of(p.getProvidedFluid(level, state, pos)) : null;
    }

    public void drain(Level level, BlockPos pos, Direction dir, BlockState state, int amount) {
        if (state.m_60734_() instanceof ISoftFluidProvider p) {
            p.consumeProvidedFluid(level, state, pos);
        }
    }

    public Integer fill(Level level, BlockPos pos, BlockState state, SoftFluidStack fluid, int minAmount) {
        return state.m_60734_() instanceof ISoftFluidConsumer p ? p.tryAcceptingFluid(level, state, pos, fluid.copyWithCount(minAmount)) ? minAmount : 0 : null;
    }
}