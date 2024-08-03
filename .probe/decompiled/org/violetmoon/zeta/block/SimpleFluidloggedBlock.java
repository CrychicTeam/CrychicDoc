package org.violetmoon.zeta.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public interface SimpleFluidloggedBlock extends BucketPickup, LiquidBlockContainer {

    @Override
    default boolean canPlaceLiquid(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Fluid fluid) {
        return this.fluidContained(state) == Fluids.EMPTY && this.acceptsFluid(fluid);
    }

    @Override
    default boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidState fluid) {
        if (this.canPlaceLiquid(level, pos, state, fluid.getType())) {
            if (!level.m_5776_()) {
                level.m_7731_(pos, this.withFluid(state, fluid.getType()), 3);
                level.scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(level));
            }
            return true;
        } else {
            return false;
        }
    }

    @NotNull
    @Override
    default ItemStack pickupBlock(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
        Fluid fluid = this.fluidContained(state);
        if (fluid != Fluids.EMPTY && fluid.getBucket() != Items.AIR) {
            level.m_7731_(pos, this.withFluid(state, Fluids.EMPTY), 3);
            if (!state.m_60710_(level, pos)) {
                level.m_46961_(pos, true);
            }
            return new ItemStack(fluid.getBucket());
        } else {
            return ItemStack.EMPTY;
        }
    }

    @NotNull
    @Override
    default Optional<SoundEvent> getPickupSound() {
        return Optional.empty();
    }

    default Optional<SoundEvent> getPickupSound(BlockState state) {
        return this.fluidContained(state).getPickupSound();
    }

    boolean acceptsFluid(@NotNull Fluid var1);

    @NotNull
    BlockState withFluid(@NotNull BlockState var1, @NotNull Fluid var2);

    @NotNull
    Fluid fluidContained(@NotNull BlockState var1);
}