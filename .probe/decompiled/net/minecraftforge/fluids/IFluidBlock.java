package net.minecraftforge.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface IFluidBlock {

    Fluid getFluid();

    int place(Level var1, BlockPos var2, @NotNull FluidStack var3, IFluidHandler.FluidAction var4);

    @NotNull
    FluidStack drain(Level var1, BlockPos var2, IFluidHandler.FluidAction var3);

    boolean canDrain(Level var1, BlockPos var2);

    float getFilledPercentage(Level var1, BlockPos var2);
}