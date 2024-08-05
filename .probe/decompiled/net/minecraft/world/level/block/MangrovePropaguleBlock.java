package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.grower.MangroveTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MangrovePropaguleBlock extends SaplingBlock implements SimpleWaterloggedBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;

    public static final int MAX_AGE = 4;

    private static final VoxelShape[] SHAPE_PER_AGE = new VoxelShape[] { Block.box(7.0, 13.0, 7.0, 9.0, 16.0, 9.0), Block.box(7.0, 10.0, 7.0, 9.0, 16.0, 9.0), Block.box(7.0, 7.0, 7.0, 9.0, 16.0, 9.0), Block.box(7.0, 3.0, 7.0, 9.0, 16.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0) };

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    private static final float GROW_TALL_MANGROVE_PROBABILITY = 0.85F;

    public MangrovePropaguleBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(new MangroveTreeGrower(0.85F), blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_55973_, 0)).m_61124_(AGE, 0)).m_61124_(WATERLOGGED, false)).m_61124_(HANGING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_55973_).add(AGE).add(WATERLOGGED).add(HANGING);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return super.m_6266_(blockState0, blockGetter1, blockPos2) || blockState0.m_60713_(Blocks.CLAY);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        boolean $$2 = $$1.getType() == Fluids.WATER;
        return (BlockState) ((BlockState) super.m_5573_(blockPlaceContext0).m_61124_(WATERLOGGED, $$2)).m_61124_(AGE, 4);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Vec3 $$4 = blockState0.m_60824_(blockGetter1, blockPos2);
        VoxelShape $$5;
        if (!(Boolean) blockState0.m_61143_(HANGING)) {
            $$5 = SHAPE_PER_AGE[4];
        } else {
            $$5 = SHAPE_PER_AGE[blockState0.m_61143_(AGE)];
        }
        return $$5.move($$4.x, $$4.y, $$4.z);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return isHanging(blockState0) ? levelReader1.m_8055_(blockPos2.above()).m_60713_(Blocks.MANGROVE_LEAVES) : super.m_7898_(blockState0, levelReader1, blockPos2);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return direction1 == Direction.UP && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!isHanging(blockState0)) {
            if (randomSource3.nextInt(7) == 0) {
                this.m_222000_(serverLevel1, blockPos2, blockState0, randomSource3);
            }
        } else {
            if (!isFullyGrown(blockState0)) {
                serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61122_(AGE), 2);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return !isHanging(blockState2) || !isFullyGrown(blockState2);
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return isHanging(blockState3) ? !isFullyGrown(blockState3) : super.isBonemealSuccess(level0, randomSource1, blockPos2, blockState3);
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        if (isHanging(blockState3) && !isFullyGrown(blockState3)) {
            serverLevel0.m_7731_(blockPos2, (BlockState) blockState3.m_61122_(AGE), 2);
        } else {
            super.performBonemeal(serverLevel0, randomSource1, blockPos2, blockState3);
        }
    }

    private static boolean isHanging(BlockState blockState0) {
        return (Boolean) blockState0.m_61143_(HANGING);
    }

    private static boolean isFullyGrown(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(AGE) == 4;
    }

    public static BlockState createNewHangingPropagule() {
        return createNewHangingPropagule(0);
    }

    public static BlockState createNewHangingPropagule(int int0) {
        return (BlockState) ((BlockState) Blocks.MANGROVE_PROPAGULE.defaultBlockState().m_61124_(HANGING, true)).m_61124_(AGE, int0);
    }
}