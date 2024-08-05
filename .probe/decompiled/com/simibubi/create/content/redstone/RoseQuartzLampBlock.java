package com.simibubi.create.content.redstone;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.redstone.diodes.BrassDiodeBlock;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class RoseQuartzLampBlock extends Block implements IWrenchable {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty POWERING = BrassDiodeBlock.POWERING;

    public static final BooleanProperty ACTIVATE = BooleanProperty.create("activate");

    public RoseQuartzLampBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(POWERED, false)).m_61124_(POWERING, false)).m_61124_(ACTIVATE, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState stateForPlacement = super.getStateForPlacement(pContext);
        return (BlockState) stateForPlacement.m_61124_(POWERED, pContext.m_43725_().m_276867_(pContext.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(POWERED, POWERING, ACTIVATE));
    }

    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            boolean isPowered = (Boolean) pState.m_61143_(POWERED);
            if (isPowered != pLevel.m_276867_(pPos)) {
                if (isPowered) {
                    pLevel.setBlock(pPos, (BlockState) pState.m_61122_(POWERED), 2);
                } else {
                    this.forEachInCluster(pLevel, pPos, (currentPos, currentState) -> {
                        pLevel.setBlock(currentPos, (BlockState) currentState.m_61124_(POWERING, false), 2);
                        this.scheduleActivation(pLevel, currentPos);
                    });
                    pLevel.setBlock(pPos, (BlockState) ((BlockState) ((BlockState) pState.m_61124_(POWERED, true)).m_61124_(POWERING, true)).m_61124_(ACTIVATE, true), 2);
                    pLevel.updateNeighborsAt(pPos, this);
                    this.scheduleActivation(pLevel, pPos);
                }
            }
        }
    }

    private void scheduleActivation(Level pLevel, BlockPos pPos) {
        if (!pLevel.m_183326_().m_183582_(pPos, this)) {
            pLevel.m_186460_(pPos, this, 1);
        }
    }

    private void forEachInCluster(Level pLevel, BlockPos pPos, BiConsumer<BlockPos, BlockState> callback) {
        List<BlockPos> frontier = new LinkedList();
        Set<BlockPos> visited = new HashSet();
        frontier.add(pPos);
        visited.add(pPos);
        while (!frontier.isEmpty()) {
            BlockPos pos = (BlockPos) frontier.remove(0);
            for (Direction d : Iterate.directions) {
                BlockPos currentPos = pos.relative(d);
                if (currentPos.m_123333_(pPos) <= 16 && visited.add(currentPos)) {
                    BlockState currentState = pLevel.getBlockState(currentPos);
                    if (currentState.m_60713_(this)) {
                        callback.accept(currentPos, currentState);
                        frontier.add(currentPos);
                    }
                }
            }
        }
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        if (pDirection == null) {
            return 0;
        } else {
            BlockState toState = pLevel.getBlockState(pPos.relative(pDirection.getOpposite()));
            if (toState.m_60713_(this)) {
                return 0;
            } else if (toState.m_60713_(Blocks.COMPARATOR)) {
                return this.getDistanceToPowered(pLevel, pPos, pDirection);
            } else {
                return pState.m_61143_(POWERING) ? 15 : 0;
            }
        }
    }

    private int getDistanceToPowered(BlockGetter level, BlockPos pos, Direction column) {
        BlockPos.MutableBlockPos currentPos = pos.mutable();
        for (int power = 15; power > 0; power--) {
            BlockState blockState = level.getBlockState(currentPos);
            if (!blockState.m_60713_(this)) {
                return 0;
            }
            if ((Boolean) blockState.m_61143_(POWERING)) {
                return power;
            }
            currentPos.move(column);
        }
        return 0;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRand) {
        boolean wasPowering = (Boolean) pState.m_61143_(POWERING);
        boolean shouldBePowering = (Boolean) pState.m_61143_(ACTIVATE);
        if (wasPowering || shouldBePowering) {
            pLevel.m_7731_(pPos, (BlockState) ((BlockState) pState.m_61124_(ACTIVATE, false)).m_61124_(POWERING, shouldBePowering), 2);
        }
        pLevel.updateNeighborsAt(pPos, this);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return InteractionResult.PASS;
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return (BlockState) originalState.m_61122_(POWERING);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult onWrenched = IWrenchable.super.onWrenched(state, context);
        if (!onWrenched.consumesAction()) {
            return onWrenched;
        } else {
            this.forEachInCluster(context.getLevel(), context.getClickedPos(), (currentPos, currentState) -> context.getLevel().updateNeighborsAt(currentPos, this));
            return onWrenched;
        }
    }
}