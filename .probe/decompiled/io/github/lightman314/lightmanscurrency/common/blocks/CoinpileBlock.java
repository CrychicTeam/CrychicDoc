package io.github.lightman314.lightmanscurrency.common.blocks;

import io.github.lightman314.lightmanscurrency.api.misc.blocks.IRotatableBlock;
import io.github.lightman314.lightmanscurrency.api.misc.blocks.LazyShapes;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoinpileBlock extends CoinBlock implements IRotatableBlock, SimpleWaterloggedBlock {

    private final VoxelShape shape;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public CoinpileBlock(BlockBehaviour.Properties properties, Supplier<Item> coinItem) {
        this(properties, coinItem, LazyShapes.SHORT_BOX);
    }

    public CoinpileBlock(BlockBehaviour.Properties properties, Supplier<Item> coinItem, VoxelShape shape) {
        super(properties, coinItem);
        this.shape = shape != null ? shape : LazyShapes.SHORT_BOX;
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    @Override
    protected boolean isFullBlock() {
        return false;
    }

    @Override
    protected int getCoinCount() {
        return 9;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        FluidState fluidstate = context.m_43725_().getFluidState(blockpos);
        return (BlockState) ((BlockState) super.m_5573_(context).m_61124_(FACING, context.m_8125_())).m_61124_(WATERLOGGED, fluidstate.is(Fluids.WATER));
    }

    @Nonnull
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(FACING);
        builder.add(WATERLOGGED);
    }

    @Nonnull
    @Override
    public BlockState updateShape(BlockState blockState0, @Nonnull Direction direction1, @Nonnull BlockState blockState2, @Nonnull LevelAccessor levelAccessor3, @Nonnull BlockPos blockPos4, @Nonnull BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext contect) {
        return this.shape;
    }

    @Nonnull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public boolean isPathfindable(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull PathComputationType type) {
        return type == PathComputationType.WATER ? level.getFluidState(pos).is(FluidTags.WATER) : false;
    }
}