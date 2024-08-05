package net.mehvahdjukaar.supplementaries.common.block.faucet;

import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface FaucetTarget<T> {

    Integer fill(Level var1, BlockPos var2, T var3, SoftFluidStack var4, int var5);

    public interface BlState extends FaucetTarget<BlockState> {
    }

    public interface Fluid extends FaucetTarget<FluidState> {
    }

    public interface Tile extends FaucetTarget<BlockEntity> {
    }
}