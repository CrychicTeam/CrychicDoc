package net.mehvahdjukaar.supplementaries.common.block.faucet;

import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.supplementaries.integration.FarmersRespriteCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

class KettleInteraction implements FaucetTarget.BlState, FaucetSource.BlState {

    public FluidOffer getProvidedFluid(Level level, BlockPos pos, Direction dir, BlockState state) {
        if (FarmersRespriteCompat.isKettle(state)) {
            IntegerProperty p = FarmersRespriteCompat.getWaterLevel();
            int waterLevel = (Integer) state.m_61143_(p);
            if (waterLevel > 0) {
                return FluidOffer.of(BuiltInSoftFluids.WATER.getHolder(), waterLevel);
            }
        }
        return null;
    }

    public void drain(Level level, BlockPos pos, Direction dir, BlockState state, int amount) {
        if (FarmersRespriteCompat.isKettle(state)) {
            IntegerProperty p = FarmersRespriteCompat.getWaterLevel();
            int waterLevel = (Integer) state.m_61143_(p);
            amount = Math.min(amount, waterLevel);
            level.setBlock(pos, (BlockState) state.m_61124_(p, amount), 3);
        }
    }

    public Integer fill(Level level, BlockPos pos, BlockState state, SoftFluidStack fluid, int minAmount) {
        if (FarmersRespriteCompat.isKettle(state)) {
            IntegerProperty p = FarmersRespriteCompat.getWaterLevel();
            int waterLevel = (Integer) state.m_61143_(p);
            if (waterLevel == 3) {
                return 0;
            } else {
                int newWater = Math.max(waterLevel + minAmount, 3);
                level.setBlock(pos, (BlockState) state.m_61124_(p, newWater), 3);
                return minAmount;
            }
        } else {
            return null;
        }
    }
}