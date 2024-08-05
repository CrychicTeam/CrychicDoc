package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SnowLayerBlock extends Block {

    public static final int MAX_HEIGHT = 8;

    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[] { Shapes.empty(), Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0) };

    public static final int HEIGHT_IMPASSABLE = 5;

    protected SnowLayerBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LAYERS, 1));
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        switch(pathComputationType3) {
            case LAND:
                return (Integer) blockState0.m_61143_(LAYERS) < 5;
            case WATER:
                return false;
            case AIR:
                return false;
            default:
                return false;
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE_BY_LAYER[blockState0.m_61143_(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE_BY_LAYER[blockState0.m_61143_(LAYERS) - 1];
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return SHAPE_BY_LAYER[blockState0.m_61143_(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE_BY_LAYER[blockState0.m_61143_(LAYERS)];
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_61143_(LAYERS) == 8 ? 0.2F : 1.0F;
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockState $$3 = levelReader1.m_8055_(blockPos2.below());
        if ($$3.m_204336_(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)) {
            return false;
        } else {
            return $$3.m_204336_(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON) ? true : Block.isFaceFull($$3.m_60812_(levelReader1, blockPos2.below()), Direction.UP) || $$3.m_60713_(this) && (Integer) $$3.m_61143_(LAYERS) == 8;
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.m_45517_(LightLayer.BLOCK, blockPos2) > 11) {
            m_49950_(blockState0, serverLevel1, blockPos2);
            serverLevel1.m_7471_(blockPos2, false);
        }
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        int $$2 = (Integer) blockState0.m_61143_(LAYERS);
        if (!blockPlaceContext1.m_43722_().is(this.m_5456_()) || $$2 >= 8) {
            return $$2 == 1;
        } else {
            return blockPlaceContext1.replacingClickedOnBlock() ? blockPlaceContext1.m_43719_() == Direction.UP : true;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = blockPlaceContext0.m_43725_().getBlockState(blockPlaceContext0.getClickedPos());
        if ($$1.m_60713_(this)) {
            int $$2 = (Integer) $$1.m_61143_(LAYERS);
            return (BlockState) $$1.m_61124_(LAYERS, Math.min(8, $$2 + 1));
        } else {
            return super.getStateForPlacement(blockPlaceContext0);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LAYERS);
    }
}