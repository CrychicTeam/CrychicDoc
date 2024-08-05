package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty UP = BlockStateProperties.UP;

    public static final EnumProperty<WallSide> EAST_WALL = BlockStateProperties.EAST_WALL;

    public static final EnumProperty<WallSide> NORTH_WALL = BlockStateProperties.NORTH_WALL;

    public static final EnumProperty<WallSide> SOUTH_WALL = BlockStateProperties.SOUTH_WALL;

    public static final EnumProperty<WallSide> WEST_WALL = BlockStateProperties.WEST_WALL;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final Map<BlockState, VoxelShape> shapeByIndex;

    private final Map<BlockState, VoxelShape> collisionShapeByIndex;

    private static final int WALL_WIDTH = 3;

    private static final int WALL_HEIGHT = 14;

    private static final int POST_WIDTH = 4;

    private static final int POST_COVER_WIDTH = 1;

    private static final int WALL_COVER_START = 7;

    private static final int WALL_COVER_END = 9;

    private static final VoxelShape POST_TEST = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);

    private static final VoxelShape NORTH_TEST = Block.box(7.0, 0.0, 0.0, 9.0, 16.0, 9.0);

    private static final VoxelShape SOUTH_TEST = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 16.0);

    private static final VoxelShape WEST_TEST = Block.box(0.0, 0.0, 7.0, 9.0, 16.0, 9.0);

    private static final VoxelShape EAST_TEST = Block.box(7.0, 0.0, 7.0, 16.0, 16.0, 9.0);

    public WallBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(UP, true)).m_61124_(NORTH_WALL, WallSide.NONE)).m_61124_(EAST_WALL, WallSide.NONE)).m_61124_(SOUTH_WALL, WallSide.NONE)).m_61124_(WEST_WALL, WallSide.NONE)).m_61124_(WATERLOGGED, false));
        this.shapeByIndex = this.makeShapes(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
        this.collisionShapeByIndex = this.makeShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
    }

    private static VoxelShape applyWallShape(VoxelShape voxelShape0, WallSide wallSide1, VoxelShape voxelShape2, VoxelShape voxelShape3) {
        if (wallSide1 == WallSide.TALL) {
            return Shapes.or(voxelShape0, voxelShape3);
        } else {
            return wallSide1 == WallSide.LOW ? Shapes.or(voxelShape0, voxelShape2) : voxelShape0;
        }
    }

    private Map<BlockState, VoxelShape> makeShapes(float float0, float float1, float float2, float float3, float float4, float float5) {
        float $$6 = 8.0F - float0;
        float $$7 = 8.0F + float0;
        float $$8 = 8.0F - float1;
        float $$9 = 8.0F + float1;
        VoxelShape $$10 = Block.box((double) $$6, 0.0, (double) $$6, (double) $$7, (double) float2, (double) $$7);
        VoxelShape $$11 = Block.box((double) $$8, (double) float3, 0.0, (double) $$9, (double) float4, (double) $$9);
        VoxelShape $$12 = Block.box((double) $$8, (double) float3, (double) $$8, (double) $$9, (double) float4, 16.0);
        VoxelShape $$13 = Block.box(0.0, (double) float3, (double) $$8, (double) $$9, (double) float4, (double) $$9);
        VoxelShape $$14 = Block.box((double) $$8, (double) float3, (double) $$8, 16.0, (double) float4, (double) $$9);
        VoxelShape $$15 = Block.box((double) $$8, (double) float3, 0.0, (double) $$9, (double) float5, (double) $$9);
        VoxelShape $$16 = Block.box((double) $$8, (double) float3, (double) $$8, (double) $$9, (double) float5, 16.0);
        VoxelShape $$17 = Block.box(0.0, (double) float3, (double) $$8, (double) $$9, (double) float5, (double) $$9);
        VoxelShape $$18 = Block.box((double) $$8, (double) float3, (double) $$8, 16.0, (double) float5, (double) $$9);
        Builder<BlockState, VoxelShape> $$19 = ImmutableMap.builder();
        for (Boolean $$20 : UP.getPossibleValues()) {
            for (WallSide $$21 : EAST_WALL.getPossibleValues()) {
                for (WallSide $$22 : NORTH_WALL.getPossibleValues()) {
                    for (WallSide $$23 : WEST_WALL.getPossibleValues()) {
                        for (WallSide $$24 : SOUTH_WALL.getPossibleValues()) {
                            VoxelShape $$25 = Shapes.empty();
                            $$25 = applyWallShape($$25, $$21, $$14, $$18);
                            $$25 = applyWallShape($$25, $$23, $$13, $$17);
                            $$25 = applyWallShape($$25, $$22, $$11, $$15);
                            $$25 = applyWallShape($$25, $$24, $$12, $$16);
                            if ($$20) {
                                $$25 = Shapes.or($$25, $$10);
                            }
                            BlockState $$26 = (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(UP, $$20)).m_61124_(EAST_WALL, $$21)).m_61124_(WEST_WALL, $$23)).m_61124_(NORTH_WALL, $$22)).m_61124_(SOUTH_WALL, $$24);
                            $$19.put((BlockState) $$26.m_61124_(WATERLOGGED, false), $$25);
                            $$19.put((BlockState) $$26.m_61124_(WATERLOGGED, true), $$25);
                        }
                    }
                }
            }
        }
        return $$19.build();
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) this.shapeByIndex.get(blockState0);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) this.collisionShapeByIndex.get(blockState0);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    private boolean connectsTo(BlockState blockState0, boolean boolean1, Direction direction2) {
        Block $$3 = blockState0.m_60734_();
        boolean $$4 = $$3 instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(blockState0, direction2);
        return blockState0.m_204336_(BlockTags.WALLS) || !m_152463_(blockState0) && boolean1 || $$3 instanceof IronBarsBlock || $$4;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        LevelReader $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        FluidState $$3 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        BlockPos $$4 = $$2.north();
        BlockPos $$5 = $$2.east();
        BlockPos $$6 = $$2.south();
        BlockPos $$7 = $$2.west();
        BlockPos $$8 = $$2.above();
        BlockState $$9 = $$1.m_8055_($$4);
        BlockState $$10 = $$1.m_8055_($$5);
        BlockState $$11 = $$1.m_8055_($$6);
        BlockState $$12 = $$1.m_8055_($$7);
        BlockState $$13 = $$1.m_8055_($$8);
        boolean $$14 = this.connectsTo($$9, $$9.m_60783_($$1, $$4, Direction.SOUTH), Direction.SOUTH);
        boolean $$15 = this.connectsTo($$10, $$10.m_60783_($$1, $$5, Direction.WEST), Direction.WEST);
        boolean $$16 = this.connectsTo($$11, $$11.m_60783_($$1, $$6, Direction.NORTH), Direction.NORTH);
        boolean $$17 = this.connectsTo($$12, $$12.m_60783_($$1, $$7, Direction.EAST), Direction.EAST);
        BlockState $$18 = (BlockState) this.m_49966_().m_61124_(WATERLOGGED, $$3.getType() == Fluids.WATER);
        return this.updateShape($$1, $$18, $$8, $$13, $$14, $$15, $$16, $$17);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        if (direction1 == Direction.DOWN) {
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        } else {
            return direction1 == Direction.UP ? this.topUpdate(levelAccessor3, blockState0, blockPos5, blockState2) : this.sideUpdate(levelAccessor3, blockPos4, blockState0, blockPos5, blockState2, direction1);
        }
    }

    private static boolean isConnected(BlockState blockState0, Property<WallSide> propertyWallSide1) {
        return blockState0.m_61143_(propertyWallSide1) != WallSide.NONE;
    }

    private static boolean isCovered(VoxelShape voxelShape0, VoxelShape voxelShape1) {
        return !Shapes.joinIsNotEmpty(voxelShape1, voxelShape0, BooleanOp.ONLY_FIRST);
    }

    private BlockState topUpdate(LevelReader levelReader0, BlockState blockState1, BlockPos blockPos2, BlockState blockState3) {
        boolean $$4 = isConnected(blockState1, NORTH_WALL);
        boolean $$5 = isConnected(blockState1, EAST_WALL);
        boolean $$6 = isConnected(blockState1, SOUTH_WALL);
        boolean $$7 = isConnected(blockState1, WEST_WALL);
        return this.updateShape(levelReader0, blockState1, blockPos2, blockState3, $$4, $$5, $$6, $$7);
    }

    private BlockState sideUpdate(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, BlockPos blockPos3, BlockState blockState4, Direction direction5) {
        Direction $$6 = direction5.getOpposite();
        boolean $$7 = direction5 == Direction.NORTH ? this.connectsTo(blockState4, blockState4.m_60783_(levelReader0, blockPos3, $$6), $$6) : isConnected(blockState2, NORTH_WALL);
        boolean $$8 = direction5 == Direction.EAST ? this.connectsTo(blockState4, blockState4.m_60783_(levelReader0, blockPos3, $$6), $$6) : isConnected(blockState2, EAST_WALL);
        boolean $$9 = direction5 == Direction.SOUTH ? this.connectsTo(blockState4, blockState4.m_60783_(levelReader0, blockPos3, $$6), $$6) : isConnected(blockState2, SOUTH_WALL);
        boolean $$10 = direction5 == Direction.WEST ? this.connectsTo(blockState4, blockState4.m_60783_(levelReader0, blockPos3, $$6), $$6) : isConnected(blockState2, WEST_WALL);
        BlockPos $$11 = blockPos1.above();
        BlockState $$12 = levelReader0.m_8055_($$11);
        return this.updateShape(levelReader0, blockState2, $$11, $$12, $$7, $$8, $$9, $$10);
    }

    private BlockState updateShape(LevelReader levelReader0, BlockState blockState1, BlockPos blockPos2, BlockState blockState3, boolean boolean4, boolean boolean5, boolean boolean6, boolean boolean7) {
        VoxelShape $$8 = blockState3.m_60812_(levelReader0, blockPos2).getFaceShape(Direction.DOWN);
        BlockState $$9 = this.updateSides(blockState1, boolean4, boolean5, boolean6, boolean7, $$8);
        return (BlockState) $$9.m_61124_(UP, this.shouldRaisePost($$9, blockState3, $$8));
    }

    private boolean shouldRaisePost(BlockState blockState0, BlockState blockState1, VoxelShape voxelShape2) {
        boolean $$3 = blockState1.m_60734_() instanceof WallBlock && (Boolean) blockState1.m_61143_(UP);
        if ($$3) {
            return true;
        } else {
            WallSide $$4 = (WallSide) blockState0.m_61143_(NORTH_WALL);
            WallSide $$5 = (WallSide) blockState0.m_61143_(SOUTH_WALL);
            WallSide $$6 = (WallSide) blockState0.m_61143_(EAST_WALL);
            WallSide $$7 = (WallSide) blockState0.m_61143_(WEST_WALL);
            boolean $$8 = $$5 == WallSide.NONE;
            boolean $$9 = $$7 == WallSide.NONE;
            boolean $$10 = $$6 == WallSide.NONE;
            boolean $$11 = $$4 == WallSide.NONE;
            boolean $$12 = $$11 && $$8 && $$9 && $$10 || $$11 != $$8 || $$9 != $$10;
            if ($$12) {
                return true;
            } else {
                boolean $$13 = $$4 == WallSide.TALL && $$5 == WallSide.TALL || $$6 == WallSide.TALL && $$7 == WallSide.TALL;
                return $$13 ? false : blockState1.m_204336_(BlockTags.WALL_POST_OVERRIDE) || isCovered(voxelShape2, POST_TEST);
            }
        }
    }

    private BlockState updateSides(BlockState blockState0, boolean boolean1, boolean boolean2, boolean boolean3, boolean boolean4, VoxelShape voxelShape5) {
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH_WALL, this.makeWallState(boolean1, voxelShape5, NORTH_TEST))).m_61124_(EAST_WALL, this.makeWallState(boolean2, voxelShape5, EAST_TEST))).m_61124_(SOUTH_WALL, this.makeWallState(boolean3, voxelShape5, SOUTH_TEST))).m_61124_(WEST_WALL, this.makeWallState(boolean4, voxelShape5, WEST_TEST));
    }

    private WallSide makeWallState(boolean boolean0, VoxelShape voxelShape1, VoxelShape voxelShape2) {
        if (boolean0) {
            return isCovered(voxelShape1, voxelShape2) ? WallSide.TALL : WallSide.LOW;
        } else {
            return WallSide.NONE;
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return !(Boolean) blockState0.m_61143_(WATERLOGGED);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(UP, NORTH_WALL, EAST_WALL, WEST_WALL, SOUTH_WALL, WATERLOGGED);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        switch(rotation1) {
            case CLOCKWISE_180:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH_WALL, (WallSide) blockState0.m_61143_(SOUTH_WALL))).m_61124_(EAST_WALL, (WallSide) blockState0.m_61143_(WEST_WALL))).m_61124_(SOUTH_WALL, (WallSide) blockState0.m_61143_(NORTH_WALL))).m_61124_(WEST_WALL, (WallSide) blockState0.m_61143_(EAST_WALL));
            case COUNTERCLOCKWISE_90:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH_WALL, (WallSide) blockState0.m_61143_(EAST_WALL))).m_61124_(EAST_WALL, (WallSide) blockState0.m_61143_(SOUTH_WALL))).m_61124_(SOUTH_WALL, (WallSide) blockState0.m_61143_(WEST_WALL))).m_61124_(WEST_WALL, (WallSide) blockState0.m_61143_(NORTH_WALL));
            case CLOCKWISE_90:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH_WALL, (WallSide) blockState0.m_61143_(WEST_WALL))).m_61124_(EAST_WALL, (WallSide) blockState0.m_61143_(NORTH_WALL))).m_61124_(SOUTH_WALL, (WallSide) blockState0.m_61143_(EAST_WALL))).m_61124_(WEST_WALL, (WallSide) blockState0.m_61143_(SOUTH_WALL));
            default:
                return blockState0;
        }
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        switch(mirror1) {
            case LEFT_RIGHT:
                return (BlockState) ((BlockState) blockState0.m_61124_(NORTH_WALL, (WallSide) blockState0.m_61143_(SOUTH_WALL))).m_61124_(SOUTH_WALL, (WallSide) blockState0.m_61143_(NORTH_WALL));
            case FRONT_BACK:
                return (BlockState) ((BlockState) blockState0.m_61124_(EAST_WALL, (WallSide) blockState0.m_61143_(WEST_WALL))).m_61124_(WEST_WALL, (WallSide) blockState0.m_61143_(EAST_WALL));
            default:
                return super.m_6943_(blockState0, mirror1);
        }
    }
}