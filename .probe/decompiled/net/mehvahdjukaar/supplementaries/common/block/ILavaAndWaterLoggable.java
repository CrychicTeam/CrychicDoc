package net.mehvahdjukaar.supplementaries.common.block;

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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public interface ILavaAndWaterLoggable extends BucketPickup, LiquidBlockContainer {

    @Override
    default boolean canPlaceLiquid(BlockGetter reader, BlockPos pos, BlockState state, Fluid fluid) {
        return !(Boolean) state.m_61143_(ModBlockProperties.LAVALOGGED) && fluid == Fluids.LAVA || !(Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
    }

    @Override
    default boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!(Boolean) state.m_61143_(ModBlockProperties.LAVALOGGED) && fluidState.getType() == Fluids.LAVA) {
            if (!world.m_5776_()) {
                world.m_7731_(pos, (BlockState) state.m_61124_(ModBlockProperties.LAVALOGGED, Boolean.TRUE), 3);
                world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
            }
            return true;
        } else if (!(Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            if (!world.m_5776_()) {
                world.m_7731_(pos, (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, Boolean.TRUE), 3);
                world.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
            }
            return true;
        } else {
            return false;
        }
    }

    default Fluid takeLiquid(LevelAccessor world, BlockPos pos, BlockState state) {
        if ((Boolean) state.m_61143_(ModBlockProperties.LAVALOGGED)) {
            world.m_7731_(pos, (BlockState) state.m_61124_(ModBlockProperties.LAVALOGGED, Boolean.FALSE), 3);
            return Fluids.LAVA;
        } else if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.m_7731_(pos, (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, Boolean.FALSE), 3);
            return Fluids.WATER;
        } else {
            return Fluids.EMPTY;
        }
    }

    @Override
    default ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.m_7731_(pPos, (BlockState) pState.m_61124_(BlockStateProperties.WATERLOGGED, Boolean.FALSE), 3);
            if (!pState.m_60710_(pLevel, pPos)) {
                pLevel.m_46961_(pPos, true);
            }
            return new ItemStack(Items.WATER_BUCKET);
        } else if ((Boolean) pState.m_61143_(ModBlockProperties.LAVALOGGED)) {
            pLevel.m_7731_(pPos, (BlockState) pState.m_61124_(ModBlockProperties.LAVALOGGED, Boolean.FALSE), 3);
            if (!pState.m_60710_(pLevel, pPos)) {
                pLevel.m_46961_(pPos, true);
            }
            return new ItemStack(Items.LAVA_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    default Optional<SoundEvent> getPickupSound() {
        return Fluids.WATER.m_142520_();
    }
}