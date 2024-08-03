package org.violetmoon.quark.content.building.block;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class WoodPostBlock extends ZetaBlock implements SimpleWaterloggedBlock {

    private static final float START = 0.0F;

    private static final float END = 16.0F;

    private static final float LEFT_EDGE = 6.0F;

    private static final float RIGHT_EDGE = 10.0F;

    private static final VoxelShape CENTER_SHAPE = Block.box(6.0, 6.0, 6.0, 10.0, 10.0, 10.0);

    private static final VoxelShape DOWN_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 10.0);

    private static final VoxelShape UP_SHAPE = Block.box(6.0, 6.0, 6.0, 10.0, 16.0, 10.0);

    private static final VoxelShape NORTH_SHAPE = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 10.0);

    private static final VoxelShape SOUTH_SHAPE = Block.box(6.0, 6.0, 6.0, 10.0, 10.0, 16.0);

    private static final VoxelShape WEST_SHAPE = Block.box(0.0, 6.0, 6.0, 10.0, 10.0, 10.0);

    private static final VoxelShape EAST_SHAPE = Block.box(6.0, 6.0, 6.0, 16.0, 10.0, 10.0);

    private static final VoxelShape[] SIDE_BOXES = new VoxelShape[] { DOWN_SHAPE, UP_SHAPE, NORTH_SHAPE, SOUTH_SHAPE, WEST_SHAPE, EAST_SHAPE };

    private static final VoxelShape[] SHAPE_CACHE = new VoxelShape[192];

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public static final EnumProperty<WoodPostBlock.PostSideType>[] SIDES = new EnumProperty[] { EnumProperty.create("connect_down", WoodPostBlock.PostSideType.class), EnumProperty.create("connect_up", WoodPostBlock.PostSideType.class), EnumProperty.create("connect_north", WoodPostBlock.PostSideType.class), EnumProperty.create("connect_south", WoodPostBlock.PostSideType.class), EnumProperty.create("connect_west", WoodPostBlock.PostSideType.class), EnumProperty.create("connect_east", WoodPostBlock.PostSideType.class) };

    public WoodPostBlock(@Nullable ZetaModule module, Block parent, String prefix, SoundType sound) {
        super(Quark.ZETA.registryUtil.inherit(parent, (Function<String, String>) (s -> prefix + s.replace("_fence", "_post"))), module, BlockBehaviour.Properties.copy(parent).sound(sound));
        BlockState state = (BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(AXIS, Direction.Axis.Y);
        for (EnumProperty<WoodPostBlock.PostSideType> prop : SIDES) {
            state = (BlockState) state.m_61124_(prop, WoodPostBlock.PostSideType.NONE);
        }
        this.m_49959_(state);
        if (module != null) {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
            this.setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, parent, true);
        }
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        int index = 0;
        for (Direction dir : Direction.values()) {
            if (((WoodPostBlock.PostSideType) state.m_61143_(SIDES[dir.ordinal()])).isSolid()) {
                index += 1 << dir.ordinal();
            }
        }
        index += 64 * ((Direction.Axis) state.m_61143_(AXIS)).ordinal();
        VoxelShape cached = SHAPE_CACHE[index];
        if (cached == null) {
            VoxelShape currShape = CENTER_SHAPE;
            for (Direction dirx : Direction.values()) {
                boolean connected = this.isConnected(state, dirx);
                if (connected) {
                    currShape = Shapes.or(currShape, SIDE_BOXES[dirx.ordinal()]);
                }
            }
            SHAPE_CACHE[index] = currShape;
            cached = currShape;
        }
        return cached;
    }

    private boolean isConnected(BlockState state, Direction dir) {
        return state.m_61143_(AXIS) == dir.getAxis() ? true : ((WoodPostBlock.PostSideType) state.m_61143_(SIDES[dir.ordinal()])).isSolid();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return !(Boolean) state.m_61143_(WATERLOGGED);
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis axis = context.m_43719_().getAxis();
        BlockPos pos = context.getClickedPos();
        Level level = context.m_43725_();
        BlockState state = (BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER)).m_61124_(AXIS, axis);
        for (Direction d : Direction.values()) {
            if (axis != d.getAxis()) {
                state = (BlockState) state.m_61124_(SIDES[d.ordinal()], WoodPostBlock.PostSideType.get(level, pos, d));
            }
        }
        return state;
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        state = (BlockState) state.m_61124_(SIDES[facing.ordinal()], WoodPostBlock.PostSideType.get(level, pos, facing));
        return super.m_7417_(state, facing, facingState, level, pos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, AXIS);
        for (EnumProperty<WoodPostBlock.PostSideType> prop : SIDES) {
            builder.add(prop);
        }
    }

    public static enum PostSideType implements StringRepresentable {

        NONE("none"), CHAIN("chain"), OTHER_POST("other_post");

        private final String name;

        private PostSideType(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public boolean isSolid() {
            return this != NONE;
        }

        private static WoodPostBlock.PostSideType get(LevelAccessor world, BlockPos pos, Direction d) {
            BlockState sideState = world.m_8055_(pos.relative(d));
            if ((!(sideState.m_60734_() instanceof ChainBlock) || sideState.m_61143_(BlockStateProperties.AXIS) != d.getAxis()) && (d != Direction.DOWN || !(sideState.m_60734_() instanceof LanternBlock) || !(Boolean) sideState.m_61143_(LanternBlock.HANGING)) && (d != Direction.DOWN || !(sideState.m_60734_() instanceof CeilingHangingSignBlock))) {
                return sideState.m_60734_() instanceof WoodPostBlock && sideState.m_61143_(WoodPostBlock.AXIS) == d.getAxis() ? OTHER_POST : NONE;
            } else {
                return CHAIN;
            }
        }
    }
}