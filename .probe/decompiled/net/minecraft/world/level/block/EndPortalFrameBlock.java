package net.minecraft.world.level.block;

import com.google.common.base.Predicates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EndPortalFrameBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty HAS_EYE = BlockStateProperties.EYE;

    protected static final VoxelShape BASE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0);

    protected static final VoxelShape EYE_SHAPE = Block.box(4.0, 13.0, 4.0, 12.0, 16.0, 12.0);

    protected static final VoxelShape FULL_SHAPE = Shapes.or(BASE_SHAPE, EYE_SHAPE);

    private static BlockPattern portalShape;

    public EndPortalFrameBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(HAS_EYE, false));
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return blockState0.m_61143_(HAS_EYE) ? FULL_SHAPE : BASE_SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, blockPlaceContext0.m_8125_().getOpposite())).m_61124_(HAS_EYE, false);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return blockState0.m_61143_(HAS_EYE) ? 15 : 0;
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, HAS_EYE);
    }

    public static BlockPattern getOrCreatePortalShape() {
        if (portalShape == null) {
            portalShape = BlockPatternBuilder.start().aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?").where('?', BlockInWorld.hasState(BlockStatePredicate.ANY)).where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where(HAS_EYE, Predicates.equalTo(true)).where(FACING, Predicates.equalTo(Direction.SOUTH)))).where('>', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where(HAS_EYE, Predicates.equalTo(true)).where(FACING, Predicates.equalTo(Direction.WEST)))).where('v', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where(HAS_EYE, Predicates.equalTo(true)).where(FACING, Predicates.equalTo(Direction.NORTH)))).where('<', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME).where(HAS_EYE, Predicates.equalTo(true)).where(FACING, Predicates.equalTo(Direction.EAST)))).build();
        }
        return portalShape;
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}