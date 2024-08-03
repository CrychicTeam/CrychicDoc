package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public interface SimpleWaterloggedBlock extends BucketPickup, LiquidBlockContainer {

    @Override
    default boolean canPlaceLiquid(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Fluid fluid3) {
        return fluid3 == Fluids.WATER;
    }

    @Override
    default boolean placeLiquid(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, FluidState fluidState3) {
        if (!(Boolean) blockState2.m_61143_(BlockStateProperties.WATERLOGGED) && fluidState3.getType() == Fluids.WATER) {
            if (!levelAccessor0.m_5776_()) {
                levelAccessor0.m_7731_(blockPos1, (BlockState) blockState2.m_61124_(BlockStateProperties.WATERLOGGED, true), 3);
                levelAccessor0.scheduleTick(blockPos1, fluidState3.getType(), fluidState3.getType().getTickDelay(levelAccessor0));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    default ItemStack pickupBlock(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        if ((Boolean) blockState2.m_61143_(BlockStateProperties.WATERLOGGED)) {
            levelAccessor0.m_7731_(blockPos1, (BlockState) blockState2.m_61124_(BlockStateProperties.WATERLOGGED, false), 3);
            if (!blockState2.m_60710_(levelAccessor0, blockPos1)) {
                levelAccessor0.m_46961_(blockPos1, true);
            }
            return new ItemStack(Items.WATER_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    default Optional<SoundEvent> getPickupSound() {
        return Fluids.WATER.m_142520_();
    }
}