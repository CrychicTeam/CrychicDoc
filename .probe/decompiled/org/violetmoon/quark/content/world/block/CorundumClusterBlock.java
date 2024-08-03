package org.violetmoon.quark.content.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class CorundumClusterBlock extends ZetaBlock implements SimpleWaterloggedBlock {

    public final CorundumBlock base;

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static VoxelShape northAabb;

    protected static VoxelShape southAabb;

    protected static VoxelShape eastAabb;

    protected static VoxelShape westAabb;

    protected static VoxelShape upAabb;

    protected static VoxelShape downAabb;

    public CorundumClusterBlock(CorundumBlock base) {
        super(Quark.ZETA.registryUtil.inheritQuark(base, "%s_cluster"), base.getModule(), BlockBehaviour.Properties.copy(base).sound(SoundType.AMETHYST_CLUSTER));
        this.base = base;
        base.cluster = this;
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.DOWN)).m_61124_(WATERLOGGED, false));
        ZetaModule module = base.getModule();
        if (module != null) {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
            this.setCreativeTab(CreativeModeTabs.COLORED_BLOCKS);
        }
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = (Direction) state.m_61143_(FACING);
        return switch(direction) {
            case NORTH ->
                northAabb;
            case SOUTH ->
                southAabb;
            case EAST ->
                eastAabb;
            case WEST ->
                westAabb;
            case DOWN ->
                downAabb;
            default ->
                upAabb;
        };
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        if (!this.canSurvive(state, worldIn, pos)) {
            worldIn.m_46961_(pos, true);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        Direction dir = (Direction) state.m_61143_(FACING);
        BlockPos off = pos.relative(dir.getOpposite());
        BlockState offState = worldIn.m_8055_(off);
        return offState.m_60783_(worldIn, off, dir);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_43719_())).m_61124_(WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        return super.m_7417_(state, facing, facingState, level, pos, facingPos);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return type == PathComputationType.WATER && worldIn.getFluidState(pos).is(FluidTags.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    static {
        int yBound = 7;
        int xBound = 3;
        upAabb = Block.box((double) xBound, 0.0, (double) xBound, (double) (16 - xBound), (double) yBound, (double) (16 - xBound));
        downAabb = Block.box((double) xBound, (double) (16 - yBound), (double) xBound, (double) (16 - xBound), 16.0, (double) (16 - xBound));
        northAabb = Block.box((double) xBound, (double) xBound, (double) (16 - yBound), (double) (16 - xBound), (double) (16 - xBound), 16.0);
        southAabb = Block.box((double) xBound, (double) xBound, 0.0, (double) (16 - xBound), (double) (16 - xBound), (double) yBound);
        eastAabb = Block.box(0.0, (double) xBound, (double) xBound, (double) yBound, (double) (16 - xBound), (double) (16 - xBound));
        westAabb = Block.box((double) (16 - yBound), (double) xBound, (double) xBound, 16.0, (double) (16 - xBound), (double) (16 - xBound));
    }
}