package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CocoaBlock extends HorizontalDirectionalBlock implements BonemealableBlock {

    public static final int MAX_AGE = 2;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;

    protected static final int AGE_0_WIDTH = 4;

    protected static final int AGE_0_HEIGHT = 5;

    protected static final int AGE_0_HALFWIDTH = 2;

    protected static final int AGE_1_WIDTH = 6;

    protected static final int AGE_1_HEIGHT = 7;

    protected static final int AGE_1_HALFWIDTH = 3;

    protected static final int AGE_2_WIDTH = 8;

    protected static final int AGE_2_HEIGHT = 9;

    protected static final int AGE_2_HALFWIDTH = 4;

    protected static final VoxelShape[] EAST_AABB = new VoxelShape[] { Block.box(11.0, 7.0, 6.0, 15.0, 12.0, 10.0), Block.box(9.0, 5.0, 5.0, 15.0, 12.0, 11.0), Block.box(7.0, 3.0, 4.0, 15.0, 12.0, 12.0) };

    protected static final VoxelShape[] WEST_AABB = new VoxelShape[] { Block.box(1.0, 7.0, 6.0, 5.0, 12.0, 10.0), Block.box(1.0, 5.0, 5.0, 7.0, 12.0, 11.0), Block.box(1.0, 3.0, 4.0, 9.0, 12.0, 12.0) };

    protected static final VoxelShape[] NORTH_AABB = new VoxelShape[] { Block.box(6.0, 7.0, 1.0, 10.0, 12.0, 5.0), Block.box(5.0, 5.0, 1.0, 11.0, 12.0, 7.0), Block.box(4.0, 3.0, 1.0, 12.0, 12.0, 9.0) };

    protected static final VoxelShape[] SOUTH_AABB = new VoxelShape[] { Block.box(6.0, 7.0, 11.0, 10.0, 12.0, 15.0), Block.box(5.0, 5.0, 9.0, 11.0, 12.0, 15.0), Block.box(4.0, 3.0, 7.0, 12.0, 12.0, 15.0) };

    public CocoaBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54117_, Direction.NORTH)).m_61124_(AGE, 0));
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(AGE) < 2;
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.f_46441_.nextInt(5) == 0) {
            int $$4 = (Integer) blockState0.m_61143_(AGE);
            if ($$4 < 2) {
                serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(AGE, $$4 + 1), 2);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockState $$3 = levelReader1.m_8055_(blockPos2.relative((Direction) blockState0.m_61143_(f_54117_)));
        return $$3.m_204336_(BlockTags.JUNGLE_LOGS);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        int $$4 = (Integer) blockState0.m_61143_(AGE);
        switch((Direction) blockState0.m_61143_(f_54117_)) {
            case SOUTH:
                return SOUTH_AABB[$$4];
            case NORTH:
            default:
                return NORTH_AABB[$$4];
            case WEST:
                return WEST_AABB[$$4];
            case EAST:
                return EAST_AABB[$$4];
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = this.m_49966_();
        LevelReader $$2 = blockPlaceContext0.m_43725_();
        BlockPos $$3 = blockPlaceContext0.getClickedPos();
        for (Direction $$4 : blockPlaceContext0.getNearestLookingDirections()) {
            if ($$4.getAxis().isHorizontal()) {
                $$1 = (BlockState) $$1.m_61124_(f_54117_, $$4);
                if ($$1.m_60710_($$2, $$3)) {
                    return $$1;
                }
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1 == blockState0.m_61143_(f_54117_) && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return (Integer) blockState2.m_61143_(AGE) < 2;
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        serverLevel0.m_7731_(blockPos2, (BlockState) blockState3.m_61124_(AGE, (Integer) blockState3.m_61143_(AGE) + 1), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_54117_, AGE);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}