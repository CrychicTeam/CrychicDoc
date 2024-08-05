package net.minecraft.world.level.block;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class HugeMushroomBlock extends Block {

    public static final BooleanProperty NORTH = PipeBlock.NORTH;

    public static final BooleanProperty EAST = PipeBlock.EAST;

    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;

    public static final BooleanProperty WEST = PipeBlock.WEST;

    public static final BooleanProperty UP = PipeBlock.UP;

    public static final BooleanProperty DOWN = PipeBlock.DOWN;

    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;

    public HugeMushroomBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(NORTH, true)).m_61124_(EAST, true)).m_61124_(SOUTH, true)).m_61124_(WEST, true)).m_61124_(UP, true)).m_61124_(DOWN, true));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockGetter $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(DOWN, !$$1.getBlockState($$2.below()).m_60713_(this))).m_61124_(UP, !$$1.getBlockState($$2.above()).m_60713_(this))).m_61124_(NORTH, !$$1.getBlockState($$2.north()).m_60713_(this))).m_61124_(EAST, !$$1.getBlockState($$2.east()).m_60713_(this))).m_61124_(SOUTH, !$$1.getBlockState($$2.south()).m_60713_(this))).m_61124_(WEST, !$$1.getBlockState($$2.west()).m_60713_(this));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return blockState2.m_60713_(this) ? (BlockState) blockState0.m_61124_((Property) PROPERTY_BY_DIRECTION.get(direction1), false) : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_((Property) PROPERTY_BY_DIRECTION.get(rotation1.rotate(Direction.NORTH)), (Boolean) blockState0.m_61143_(NORTH))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(rotation1.rotate(Direction.SOUTH)), (Boolean) blockState0.m_61143_(SOUTH))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(rotation1.rotate(Direction.EAST)), (Boolean) blockState0.m_61143_(EAST))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(rotation1.rotate(Direction.WEST)), (Boolean) blockState0.m_61143_(WEST))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(rotation1.rotate(Direction.UP)), (Boolean) blockState0.m_61143_(UP))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(rotation1.rotate(Direction.DOWN)), (Boolean) blockState0.m_61143_(DOWN));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_((Property) PROPERTY_BY_DIRECTION.get(mirror1.mirror(Direction.NORTH)), (Boolean) blockState0.m_61143_(NORTH))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(mirror1.mirror(Direction.SOUTH)), (Boolean) blockState0.m_61143_(SOUTH))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(mirror1.mirror(Direction.EAST)), (Boolean) blockState0.m_61143_(EAST))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(mirror1.mirror(Direction.WEST)), (Boolean) blockState0.m_61143_(WEST))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(mirror1.mirror(Direction.UP)), (Boolean) blockState0.m_61143_(UP))).m_61124_((Property) PROPERTY_BY_DIRECTION.get(mirror1.mirror(Direction.DOWN)), (Boolean) blockState0.m_61143_(DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }
}