package net.minecraft.world.level.block;

import com.google.common.collect.UnmodifiableIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrossCollisionBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty NORTH = PipeBlock.NORTH;

    public static final BooleanProperty EAST = PipeBlock.EAST;

    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;

    public static final BooleanProperty WEST = PipeBlock.WEST;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>) PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(p_52346_ -> ((Direction) p_52346_.getKey()).getAxis().isHorizontal()).collect(Util.toMap());

    protected final VoxelShape[] collisionShapeByIndex;

    protected final VoxelShape[] shapeByIndex;

    private final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap();

    protected CrossCollisionBlock(float float0, float float1, float float2, float float3, float float4, BlockBehaviour.Properties blockBehaviourProperties5) {
        super(blockBehaviourProperties5);
        this.collisionShapeByIndex = this.makeShapes(float0, float1, float4, 0.0F, float4);
        this.shapeByIndex = this.makeShapes(float0, float1, float2, 0.0F, float3);
        UnmodifiableIterator var7 = this.f_49792_.getPossibleStates().iterator();
        while (var7.hasNext()) {
            BlockState $$6 = (BlockState) var7.next();
            this.getAABBIndex($$6);
        }
    }

    protected VoxelShape[] makeShapes(float float0, float float1, float float2, float float3, float float4) {
        float $$5 = 8.0F - float0;
        float $$6 = 8.0F + float0;
        float $$7 = 8.0F - float1;
        float $$8 = 8.0F + float1;
        VoxelShape $$9 = Block.box((double) $$5, 0.0, (double) $$5, (double) $$6, (double) float2, (double) $$6);
        VoxelShape $$10 = Block.box((double) $$7, (double) float3, 0.0, (double) $$8, (double) float4, (double) $$8);
        VoxelShape $$11 = Block.box((double) $$7, (double) float3, (double) $$7, (double) $$8, (double) float4, 16.0);
        VoxelShape $$12 = Block.box(0.0, (double) float3, (double) $$7, (double) $$8, (double) float4, (double) $$8);
        VoxelShape $$13 = Block.box((double) $$7, (double) float3, (double) $$7, 16.0, (double) float4, (double) $$8);
        VoxelShape $$14 = Shapes.or($$10, $$13);
        VoxelShape $$15 = Shapes.or($$11, $$12);
        VoxelShape[] $$16 = new VoxelShape[] { Shapes.empty(), $$11, $$12, $$15, $$10, Shapes.or($$11, $$10), Shapes.or($$12, $$10), Shapes.or($$15, $$10), $$13, Shapes.or($$11, $$13), Shapes.or($$12, $$13), Shapes.or($$15, $$13), $$14, Shapes.or($$11, $$14), Shapes.or($$12, $$14), Shapes.or($$15, $$14) };
        for (int $$17 = 0; $$17 < 16; $$17++) {
            $$16[$$17] = Shapes.or($$9, $$16[$$17]);
        }
        return $$16;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return !(Boolean) blockState0.m_61143_(WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.shapeByIndex[this.getAABBIndex(blockState0)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.collisionShapeByIndex[this.getAABBIndex(blockState0)];
    }

    private static int indexFor(Direction direction0) {
        return 1 << direction0.get2DDataValue();
    }

    protected int getAABBIndex(BlockState blockState0) {
        return this.stateToIndex.computeIntIfAbsent(blockState0, p_52366_ -> {
            int $$1 = 0;
            if ((Boolean) p_52366_.m_61143_(NORTH)) {
                $$1 |= indexFor(Direction.NORTH);
            }
            if ((Boolean) p_52366_.m_61143_(EAST)) {
                $$1 |= indexFor(Direction.EAST);
            }
            if ((Boolean) p_52366_.m_61143_(SOUTH)) {
                $$1 |= indexFor(Direction.SOUTH);
            }
            if ((Boolean) p_52366_.m_61143_(WEST)) {
                $$1 |= indexFor(Direction.WEST);
            }
            return $$1;
        });
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        switch(rotation1) {
            case CLOCKWISE_180:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH, (Boolean) blockState0.m_61143_(SOUTH))).m_61124_(EAST, (Boolean) blockState0.m_61143_(WEST))).m_61124_(SOUTH, (Boolean) blockState0.m_61143_(NORTH))).m_61124_(WEST, (Boolean) blockState0.m_61143_(EAST));
            case COUNTERCLOCKWISE_90:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH, (Boolean) blockState0.m_61143_(EAST))).m_61124_(EAST, (Boolean) blockState0.m_61143_(SOUTH))).m_61124_(SOUTH, (Boolean) blockState0.m_61143_(WEST))).m_61124_(WEST, (Boolean) blockState0.m_61143_(NORTH));
            case CLOCKWISE_90:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH, (Boolean) blockState0.m_61143_(WEST))).m_61124_(EAST, (Boolean) blockState0.m_61143_(NORTH))).m_61124_(SOUTH, (Boolean) blockState0.m_61143_(EAST))).m_61124_(WEST, (Boolean) blockState0.m_61143_(SOUTH));
            default:
                return blockState0;
        }
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        switch(mirror1) {
            case LEFT_RIGHT:
                return (BlockState) ((BlockState) blockState0.m_61124_(NORTH, (Boolean) blockState0.m_61143_(SOUTH))).m_61124_(SOUTH, (Boolean) blockState0.m_61143_(NORTH));
            case FRONT_BACK:
                return (BlockState) ((BlockState) blockState0.m_61124_(EAST, (Boolean) blockState0.m_61143_(WEST))).m_61124_(WEST, (Boolean) blockState0.m_61143_(EAST));
            default:
                return super.m_6943_(blockState0, mirror1);
        }
    }
}