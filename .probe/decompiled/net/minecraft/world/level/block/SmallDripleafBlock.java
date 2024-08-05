package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SmallDripleafBlock extends DoublePlantBlock implements BonemealableBlock, SimpleWaterloggedBlock {

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final float AABB_OFFSET = 6.0F;

    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);

    public SmallDripleafBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52858_, DoubleBlockHalf.LOWER)).m_61124_(WATERLOGGED, false)).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_204336_(BlockTags.SMALL_DRIPLEAF_PLACEABLE) || blockGetter1.getFluidState(blockPos2.above()).isSourceOfType(Fluids.WATER) && super.m_6266_(blockState0, blockGetter1, blockPos2);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = super.getStateForPlacement(blockPlaceContext0);
        return $$1 != null ? m_182453_(blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos(), (BlockState) $$1.m_61124_(FACING, blockPlaceContext0.m_8125_().getOpposite())) : null;
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        if (!level0.isClientSide()) {
            BlockPos $$5 = blockPos1.above();
            BlockState $$6 = DoublePlantBlock.copyWaterloggedFrom(level0, $$5, (BlockState) ((BlockState) this.m_49966_().m_61124_(f_52858_, DoubleBlockHalf.UPPER)).m_61124_(FACING, (Direction) blockState2.m_61143_(FACING)));
            level0.setBlock($$5, $$6, 3);
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        if (blockState0.m_61143_(f_52858_) == DoubleBlockHalf.UPPER) {
            return super.canSurvive(blockState0, levelReader1, blockPos2);
        } else {
            BlockPos $$3 = blockPos2.below();
            BlockState $$4 = levelReader1.m_8055_($$3);
            return this.mayPlaceOn($$4, levelReader1, $$3);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_52858_, WATERLOGGED, FACING);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        if (blockState3.m_61143_(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
            BlockPos $$4 = blockPos2.above();
            serverLevel0.m_7731_($$4, serverLevel0.m_6425_($$4).createLegacyBlock(), 18);
            BigDripleafBlock.placeWithRandomHeight(serverLevel0, randomSource1, blockPos2, (Direction) blockState3.m_61143_(FACING));
        } else {
            BlockPos $$5 = blockPos2.below();
            this.performBonemeal(serverLevel0, randomSource1, $$5, serverLevel0.m_8055_($$5));
        }
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public float getMaxVerticalOffset() {
        return 0.1F;
    }
}