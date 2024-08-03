package com.github.alexmodguy.alexscaves.server.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MothBallBlock extends Block implements SimpleWaterloggedBlock {

    private static final VoxelShape ONE_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 4.0, 10.0);

    private static final VoxelShape MULTI_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 4.0, 15.0);

    public static final IntegerProperty BALLS = IntegerProperty.create("balls", 1, 5);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public MothBallBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).noCollission().strength(0.5F).sound(ACSoundTypes.MOTH_BALL));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(BALLS, 1));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowState = level.m_8055_(pos.below());
        return belowState.m_60659_(level, pos.below(), Direction.UP, SupportType.FULL);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return !state.m_60710_(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.m_61143_(BALLS) > 1 ? MULTI_SHAPE : ONE_SHAPE;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.m_43722_().getItem() == this.m_5456_() && (Integer) state.m_61143_(BALLS) < 5 || super.m_6864_(state, useContext);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = levelaccessor.m_8055_(blockpos);
        return blockstate.m_60734_() == this ? (BlockState) blockstate.m_61124_(BALLS, Math.min(5, (Integer) blockstate.m_61143_(BALLS) + 1)) : (BlockState) super.getStateForPlacement(context).m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER);
    }

    protected void removeOneBall(Level worldIn, BlockPos pos, BlockState state) {
        int i = (Integer) state.m_61143_(BALLS);
        if (i <= 1) {
            worldIn.m_46961_(pos, false);
        } else {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(BALLS, i - 1), 2);
            worldIn.m_220407_(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            worldIn.m_46796_(2001, pos, Block.getId(state));
        }
    }

    @Override
    public void playerDestroy(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack stack) {
        super.playerDestroy(worldIn, player, pos, state, te, stack);
        this.removeOneBall(worldIn, pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BALLS, WATERLOGGED);
    }
}