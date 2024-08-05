package net.mehvahdjukaar.amendments.client.colors;

import net.mehvahdjukaar.moonlight.api.block.ISoftFluidTankProvider;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SoftFluidColor implements BlockColor {

    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tint) {
        if (level != null && pos != null && level.m_7702_(pos) instanceof ISoftFluidTankProvider bh && tint == 1) {
            SoftFluidTank tank = bh.getSoftFluidTank();
            if (tank.getFluid().is(BuiltInSoftFluids.MUSHROOM_STEW.get())) {
                return -1;
            } else if (tank.getFluid().is(BuiltInSoftFluids.SUS_STEW.get())) {
                return -1;
            } else if (tank.getFluid().is(BuiltInSoftFluids.RABBIT_STEW.get())) {
                return -1;
            } else {
                return tank.getFluid().is(BuiltInSoftFluids.MUSHROOM_STEW.get()) ? -1 : tank.getCachedStillColor(level, pos);
            }
        } else {
            return -1;
        }
    }
}