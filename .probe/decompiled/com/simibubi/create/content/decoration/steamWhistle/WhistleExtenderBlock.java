package com.simibubi.create.content.decoration.steamWhistle;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WhistleExtenderBlock extends Block implements IWrenchable {

    public static final EnumProperty<WhistleExtenderBlock.WhistleExtenderShape> SHAPE = EnumProperty.create("shape", WhistleExtenderBlock.WhistleExtenderShape.class);

    public static final EnumProperty<WhistleBlock.WhistleSize> SIZE = WhistleBlock.SIZE;

    public WhistleExtenderBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(SHAPE, WhistleExtenderBlock.WhistleExtenderShape.SINGLE)).m_61124_(SIZE, WhistleBlock.WhistleSize.MEDIUM));
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (context.getClickLocation().y < (double) ((float) context.getClickedPos().m_123342_() + 0.5F) || state.m_61143_(SHAPE) == WhistleExtenderBlock.WhistleExtenderShape.SINGLE) {
            return IWrenchable.super.onSneakWrenched(state, context);
        } else if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            world.setBlock(pos, (BlockState) state.m_61124_(SHAPE, WhistleExtenderBlock.WhistleExtenderShape.SINGLE), 3);
            this.playRemoveSound(world, pos);
            return InteractionResult.SUCCESS;
        }
    }

    protected UseOnContext relocateContext(UseOnContext context, BlockPos target) {
        return new UseOnContext(context.getPlayer(), context.getHand(), new BlockHitResult(context.getClickLocation(), context.getClickedFace(), target, context.isInside()));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer != null && AllBlocks.STEAM_WHISTLE.isIn(pPlayer.m_21120_(pHand))) {
            BlockPos findRoot = findRoot(pLevel, pPos);
            BlockState blockState = pLevel.getBlockState(findRoot);
            return blockState.m_60734_() instanceof WhistleBlock whistle ? whistle.use(blockState, pLevel, findRoot, pPlayer, pHand, new BlockHitResult(pHit.m_82450_(), pHit.getDirection(), findRoot, pHit.isInside())) : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos findRoot = findRoot(level, context.getClickedPos());
        BlockState blockState = level.getBlockState(findRoot);
        return blockState.m_60734_() instanceof WhistleBlock whistle ? whistle.onWrenched(blockState, this.relocateContext(context, findRoot)) : IWrenchable.super.onWrenched(state, context);
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return AllBlocks.STEAM_WHISTLE.asStack();
    }

    public static BlockPos findRoot(LevelAccessor pLevel, BlockPos pPos) {
        BlockPos currentPos = pPos.below();
        while (true) {
            BlockState blockState = pLevel.m_8055_(currentPos);
            if (!AllBlocks.STEAM_WHISTLE_EXTENSION.has(blockState)) {
                return currentPos;
            }
            currentPos = currentPos.below();
        }
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState below = pLevel.m_8055_(pPos.below());
        return below.m_60713_(this) && below.m_61143_(SHAPE) != WhistleExtenderBlock.WhistleExtenderShape.SINGLE || AllBlocks.STEAM_WHISTLE.has(below);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pFacing.getAxis() != Direction.Axis.Y) {
            return pState;
        } else if (pFacing == Direction.UP) {
            boolean connected = pState.m_61143_(SHAPE) == WhistleExtenderBlock.WhistleExtenderShape.DOUBLE_CONNECTED;
            boolean shouldConnect = pLevel.m_8055_(pCurrentPos.above()).m_60713_(this);
            if (!connected && shouldConnect) {
                return (BlockState) pState.m_61124_(SHAPE, WhistleExtenderBlock.WhistleExtenderShape.DOUBLE_CONNECTED);
            } else {
                return connected && !shouldConnect ? (BlockState) pState.m_61124_(SHAPE, WhistleExtenderBlock.WhistleExtenderShape.DOUBLE) : pState;
            }
        } else {
            return !pState.m_60710_(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : (BlockState) pState.m_61124_(SIZE, (WhistleBlock.WhistleSize) pLevel.m_8055_(pCurrentPos.below()).m_61143_(SIZE));
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pOldState.m_60734_() != this || pOldState.m_61143_(SHAPE) != pState.m_61143_(SHAPE)) {
            WhistleBlock.queuePitchUpdate(pLevel, findRoot(pLevel, pPos));
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pNewState.m_60734_() != this) {
            WhistleBlock.queuePitchUpdate(pLevel, findRoot(pLevel, pPos));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(SHAPE, SIZE));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        WhistleBlock.WhistleSize size = (WhistleBlock.WhistleSize) pState.m_61143_(SIZE);
        switch((WhistleExtenderBlock.WhistleExtenderShape) pState.m_61143_(SHAPE)) {
            case DOUBLE:
                return size == WhistleBlock.WhistleSize.LARGE ? AllShapes.WHISTLE_EXTENDER_LARGE_DOUBLE : (size == WhistleBlock.WhistleSize.MEDIUM ? AllShapes.WHISTLE_EXTENDER_MEDIUM_DOUBLE : AllShapes.WHISTLE_EXTENDER_SMALL_DOUBLE);
            case DOUBLE_CONNECTED:
                return size == WhistleBlock.WhistleSize.LARGE ? AllShapes.WHISTLE_EXTENDER_LARGE_DOUBLE_CONNECTED : (size == WhistleBlock.WhistleSize.MEDIUM ? AllShapes.WHISTLE_EXTENDER_MEDIUM_DOUBLE_CONNECTED : AllShapes.WHISTLE_EXTENDER_SMALL_DOUBLE_CONNECTED);
            case SINGLE:
            default:
                return size == WhistleBlock.WhistleSize.LARGE ? AllShapes.WHISTLE_EXTENDER_LARGE : (size == WhistleBlock.WhistleSize.MEDIUM ? AllShapes.WHISTLE_EXTENDER_MEDIUM : AllShapes.WHISTLE_EXTENDER_SMALL);
        }
    }

    public boolean hidesNeighborFace(BlockGetter level, BlockPos pos, BlockState state, BlockState neighborState, Direction dir) {
        return AllBlocks.STEAM_WHISTLE.has(neighborState) && dir == Direction.DOWN;
    }

    public static enum WhistleExtenderShape implements StringRepresentable {

        SINGLE, DOUBLE, DOUBLE_CONNECTED;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}