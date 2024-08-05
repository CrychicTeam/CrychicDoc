package org.violetmoon.quark.content.world.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class GlowShroomRingBlock extends ZetaBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(0.0, 4.0, 5.0, 16.0, 12.0, 16.0), Direction.SOUTH, Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 11.0), Direction.WEST, Block.box(5.0, 4.0, 0.0, 16.0, 12.0, 16.0), Direction.EAST, Block.box(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)));

    public GlowShroomRingBlock(@Nullable ZetaModule module) {
        super("glow_shroom_ring", module, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).noCollission().instabreak().sound(SoundType.FUNGUS).pushReaction(PushReaction.DESTROY));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false));
        if (module != null) {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
            CreativeTabManager.addToCreativeTab(CreativeModeTabs.NATURAL_BLOCKS, this);
            this.setCreativeTab(CreativeModeTabs.NATURAL_BLOCKS);
        }
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return (VoxelShape) SHAPES.get(state.m_61143_(FACING));
    }

    @NotNull
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @NotNull
    @Override
    public BlockState mirror(@NotNull BlockState state, Mirror mirror) {
        return this.rotate(state, mirror.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        return facing.getOpposite() == state.m_61143_(FACING) && !state.m_60710_(world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        BlockState blockstate = world.m_8055_(blockpos);
        return blockstate.m_60783_(world, blockpos, direction);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState blockstate = super.m_5573_(context);
        LevelReader levelreader = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        Direction[] adirection = context.getNearestLookingDirections();
        for (Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                blockstate = (BlockState) blockstate.m_61124_(FACING, direction.getOpposite());
                if (blockstate.m_60710_(levelreader, blockpos)) {
                    return blockstate;
                }
            }
        }
        return null;
    }
}