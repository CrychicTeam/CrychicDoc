package net.mehvahdjukaar.moonlight.api.block;

import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ISoftFluidProvider {

    SoftFluidStack getProvidedFluid(Level var1, BlockState var2, BlockPos var3);

    void consumeProvidedFluid(Level var1, BlockState var2, BlockPos var3);
}