package net.mehvahdjukaar.supplementaries.common.block.faucet;

import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

class PowderSnowCauldronInteraction implements FaucetTarget.BlState, FaucetSource.BlState {

    public FluidOffer getProvidedFluid(Level level, BlockPos pos, Direction dir, BlockState source) {
        return source.m_60713_(Blocks.POWDER_SNOW_CAULDRON) ? FluidOffer.of(BuiltInSoftFluids.POWDERED_SNOW.getHolder(), (Integer) source.m_61143_(LayeredCauldronBlock.LEVEL)) : null;
    }

    public void drain(Level level, BlockPos pos, Direction dir, BlockState source, int amount) {
        int am = (Integer) source.m_61143_(LayeredCauldronBlock.LEVEL) - amount;
        if (am <= 0) {
            level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
        } else {
            level.setBlockAndUpdate(pos, (BlockState) source.m_61124_(LayeredCauldronBlock.LEVEL, am));
        }
    }

    public Integer fill(Level level, BlockPos pos, BlockState state, SoftFluidStack fluid, int minAmount) {
        int amount = fluid.getCount();
        if (state.m_60713_(Blocks.CAULDRON) && fluid.is(BuiltInSoftFluids.POWDERED_SNOW.get())) {
            int am = Math.min(minAmount, 3);
            level.setBlockAndUpdate(pos, (BlockState) Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, am));
            return minAmount;
        } else if (state.m_60713_(Blocks.POWDER_SNOW_CAULDRON)) {
            if (fluid.is(BuiltInSoftFluids.POWDERED_SNOW.get()) && (Integer) state.m_61143_(LayeredCauldronBlock.LEVEL) < 3) {
                int space = 3 - (Integer) state.m_61143_(LayeredCauldronBlock.LEVEL);
                int am = Math.min(amount, space);
                level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(LayeredCauldronBlock.LEVEL, (Integer) state.m_61143_(LayeredCauldronBlock.LEVEL) + am));
                return Math.max(minAmount, am);
            } else if (fluid.is(BuiltInSoftFluids.WATER.get())) {
                level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.withPropertiesOf(state));
                return minAmount;
            } else {
                return 0;
            }
        } else {
            return null;
        }
    }
}