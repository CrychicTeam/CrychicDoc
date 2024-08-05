package com.rekindled.embers.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class MechEdgeBlockBase extends Block implements SimpleWaterloggedBlock {

    public static final EnumProperty<MechEdgeBlockBase.MechEdge> EDGE = EnumProperty.create("edge", MechEdgeBlockBase.MechEdge.class);

    public static final VoxelShape TOP_AABB = Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);

    public static final VoxelShape BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

    public static final VoxelShape NORTH_AABB = Shapes.or(Block.box(0.0, 4.0, 2.0, 16.0, 12.0, 16.0), TOP_AABB, BOTTOM_AABB);

    public static final VoxelShape NORTHEAST_AABB = Shapes.or(Block.box(0.0, 4.0, 2.0, 14.0, 12.0, 16.0), TOP_AABB, BOTTOM_AABB);

    public static final VoxelShape EAST_AABB = Shapes.or(Block.box(0.0, 4.0, 0.0, 14.0, 12.0, 16.0), TOP_AABB, BOTTOM_AABB);

    public static final VoxelShape SOUTHEAST_AABB = Shapes.or(Block.box(0.0, 4.0, 0.0, 14.0, 12.0, 14.0), TOP_AABB, BOTTOM_AABB);

    public static final VoxelShape SOUTH_AABB = Shapes.or(Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 14.0), TOP_AABB, BOTTOM_AABB);

    public static final VoxelShape SOUTHWEST_AABB = Shapes.or(Block.box(2.0, 4.0, 0.0, 16.0, 12.0, 14.0), TOP_AABB, BOTTOM_AABB);

    public static final VoxelShape WEST_AABB = Shapes.or(Block.box(2.0, 4.0, 0.0, 16.0, 12.0, 16.0), TOP_AABB, BOTTOM_AABB);

    public static final VoxelShape NORTHWEST_AABB = Shapes.or(Block.box(2.0, 4.0, 2.0, 16.0, 12.0, 16.0), TOP_AABB, BOTTOM_AABB);

    public static final VoxelShape[] SHAPES = new VoxelShape[] { NORTH_AABB, NORTHEAST_AABB, EAST_AABB, SOUTHEAST_AABB, SOUTH_AABB, SOUTHWEST_AABB, WEST_AABB, NORTHWEST_AABB };

    public MechEdgeBlockBase(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(EDGE, MechEdgeBlockBase.MechEdge.NORTH));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            BlockPos centerPos = pos.offset(((MechEdgeBlockBase.MechEdge) state.m_61143_(EDGE)).centerPos);
            if (level.getBlockState(centerPos).m_60734_() == this.getCenterBlock()) {
                level.m_46961_(centerPos, false);
            }
            super.m_6810_(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[((MechEdgeBlockBase.MechEdge) state.m_61143_(EDGE)).index];
    }

    public abstract Block getCenterBlock();

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(this.getCenterBlock());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState) super.getStateForPlacement(pContext).m_61124_(BlockStateProperties.WATERLOGGED, pContext.m_43725_().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        return super.m_7417_(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(EDGE).add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }

    public static enum MechEdge implements StringRepresentable {

        NORTH("north", 0, new Vec3i(0, 0, 1), false, 0),
        NORTHEAST("northeast", 1, new Vec3i(-1, 0, 1), true, 0),
        EAST("east", 2, new Vec3i(-1, 0, 0), false, 90),
        SOUTHEAST("southeast", 3, new Vec3i(-1, 0, -1), true, 90),
        SOUTH("south", 4, new Vec3i(0, 0, -1), false, 180),
        SOUTHWEST("southwest", 5, new Vec3i(1, 0, -1), true, 180),
        WEST("west", 6, new Vec3i(1, 0, 0), false, 270),
        NORTHWEST("northwest", 7, new Vec3i(1, 0, 1), true, 270);

        private final String name;

        public final int index;

        public final Vec3i centerPos;

        public final boolean corner;

        public final int rotation;

        private MechEdge(String name, int index, Vec3i center, boolean corner, int rotation) {
            this.name = name;
            this.index = index;
            this.centerPos = center;
            this.corner = corner;
            this.rotation = rotation;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}