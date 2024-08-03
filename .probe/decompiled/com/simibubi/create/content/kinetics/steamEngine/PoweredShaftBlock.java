package com.simibubi.create.content.kinetics.steamEngine;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PoweredShaftBlock extends AbstractShaftBlock {

    public PoweredShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.EIGHT_VOXEL_POLE.get((Direction.Axis) pState.m_61143_(AXIS));
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends KineticBlockEntity>) AllBlockEntityTypes.POWERED_SHAFT.get();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pPlayer.m_6144_() && pPlayer.mayBuild()) {
            ItemStack heldItem = pPlayer.m_21120_(pHand);
            IPlacementHelper helper = PlacementHelpers.get(ShaftBlock.placementHelperId);
            return helper.matchesItem(heldItem) ? helper.getOffset(pPlayer, pLevel, pState, pPos, pHit).placeInWorld(pLevel, (BlockItem) heldItem.getItem(), pPlayer, pHand, pHit) : InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!stillValid(pState, pLevel, pPos)) {
            pLevel.m_7731_(pPos, (BlockState) ((BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(ShaftBlock.AXIS, (Direction.Axis) pState.m_61143_(AXIS))).m_61124_(WATERLOGGED, (Boolean) pState.m_61143_(WATERLOGGED)), 3);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return AllBlocks.SHAFT.asStack();
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return stillValid(pState, pLevel, pPos);
    }

    public static boolean stillValid(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        for (Direction d : Iterate.directions) {
            if (d.getAxis() != pState.m_61143_(AXIS)) {
                BlockPos enginePos = pPos.relative(d, 2);
                BlockState engineState = pLevel.m_8055_(enginePos);
                if (engineState.m_60734_() instanceof SteamEngineBlock engine && SteamEngineBlock.getShaftPos(engineState, enginePos).equals(pPos) && SteamEngineBlock.isShaftValid(engineState, pState)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static BlockState getEquivalent(BlockState stateForPlacement) {
        return (BlockState) ((BlockState) AllBlocks.POWERED_SHAFT.getDefaultState().m_61124_(AXIS, (Direction.Axis) stateForPlacement.m_61143_(ShaftBlock.AXIS))).m_61124_(WATERLOGGED, (Boolean) stateForPlacement.m_61143_(WATERLOGGED));
    }
}