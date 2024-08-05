package net.mehvahdjukaar.supplementaries.common.block.faucet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public interface FaucetSource<T> {

    @Nullable
    FluidOffer getProvidedFluid(Level var1, BlockPos var2, Direction var3, T var4);

    void drain(Level var1, BlockPos var2, Direction var3, T var4, int var5);

    public interface BlState extends FaucetSource<BlockState> {
    }

    public interface Fluid extends FaucetSource<FluidState> {
    }

    public interface Tile extends FaucetSource<BlockEntity> {
    }
}