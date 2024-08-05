package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;

public class DoublePlantBlock extends BushBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public DoublePlantBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        DoubleBlockHalf $$6 = (DoubleBlockHalf) blockState0.m_61143_(HALF);
        if (direction1.getAxis() != Direction.Axis.Y || $$6 == DoubleBlockHalf.LOWER != (direction1 == Direction.UP) || blockState2.m_60713_(this) && blockState2.m_61143_(HALF) != $$6) {
            return $$6 == DoubleBlockHalf.LOWER && direction1 == Direction.DOWN && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockPos $$1 = blockPlaceContext0.getClickedPos();
        Level $$2 = blockPlaceContext0.m_43725_();
        return $$1.m_123342_() < $$2.m_151558_() - 1 && $$2.getBlockState($$1.above()).m_60629_(blockPlaceContext0) ? super.m_5573_(blockPlaceContext0) : null;
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        BlockPos $$5 = blockPos1.above();
        level0.setBlock($$5, copyWaterloggedFrom(level0, $$5, (BlockState) this.m_49966_().m_61124_(HALF, DoubleBlockHalf.UPPER)), 3);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        if (blockState0.m_61143_(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(blockState0, levelReader1, blockPos2);
        } else {
            BlockState $$3 = levelReader1.m_8055_(blockPos2.below());
            return $$3.m_60713_(this) && $$3.m_61143_(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    public static void placeAt(LevelAccessor levelAccessor0, BlockState blockState1, BlockPos blockPos2, int int3) {
        BlockPos $$4 = blockPos2.above();
        levelAccessor0.m_7731_(blockPos2, copyWaterloggedFrom(levelAccessor0, blockPos2, (BlockState) blockState1.m_61124_(HALF, DoubleBlockHalf.LOWER)), int3);
        levelAccessor0.m_7731_($$4, copyWaterloggedFrom(levelAccessor0, $$4, (BlockState) blockState1.m_61124_(HALF, DoubleBlockHalf.UPPER)), int3);
    }

    public static BlockState copyWaterloggedFrom(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2) {
        return blockState2.m_61138_(BlockStateProperties.WATERLOGGED) ? (BlockState) blockState2.m_61124_(BlockStateProperties.WATERLOGGED, levelReader0.isWaterAt(blockPos1)) : blockState2;
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        if (!level0.isClientSide) {
            if (player3.isCreative()) {
                preventCreativeDropFromBottomPart(level0, blockPos1, blockState2, player3);
            } else {
                m_49881_(blockState2, level0, blockPos1, null, player3, player3.m_21205_());
            }
        }
        super.m_5707_(level0, blockPos1, blockState2, player3);
    }

    @Override
    public void playerDestroy(Level level0, Player player1, BlockPos blockPos2, BlockState blockState3, @Nullable BlockEntity blockEntity4, ItemStack itemStack5) {
        super.m_6240_(level0, player1, blockPos2, Blocks.AIR.defaultBlockState(), blockEntity4, itemStack5);
    }

    protected static void preventCreativeDropFromBottomPart(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        DoubleBlockHalf $$4 = (DoubleBlockHalf) blockState2.m_61143_(HALF);
        if ($$4 == DoubleBlockHalf.UPPER) {
            BlockPos $$5 = blockPos1.below();
            BlockState $$6 = level0.getBlockState($$5);
            if ($$6.m_60713_(blockState2.m_60734_()) && $$6.m_61143_(HALF) == DoubleBlockHalf.LOWER) {
                BlockState $$7 = $$6.m_60819_().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                level0.setBlock($$5, $$7, 35);
                level0.m_5898_(player3, 2001, $$5, Block.getId($$6));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(HALF);
    }

    @Override
    public long getSeed(BlockState blockState0, BlockPos blockPos1) {
        return Mth.getSeed(blockPos1.m_123341_(), blockPos1.below(blockState0.m_61143_(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).m_123342_(), blockPos1.m_123343_());
    }
}