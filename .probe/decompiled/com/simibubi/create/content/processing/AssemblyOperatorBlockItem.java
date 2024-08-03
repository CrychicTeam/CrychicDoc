package com.simibubi.create.content.processing;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class AssemblyOperatorBlockItem extends BlockItem {

    public AssemblyOperatorBlockItem(Block block, Item.Properties builder) {
        super(block, builder);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        BlockPos placedOnPos = context.getClickedPos().relative(context.m_43719_().getOpposite());
        BlockState placedOnState = context.m_43725_().getBlockState(placedOnPos);
        if (this.operatesOn(placedOnState) && context.m_43719_() == Direction.UP) {
            if (!context.m_43725_().getBlockState(placedOnPos.above(2)).m_247087_()) {
                return InteractionResult.FAIL;
            }
            context = this.adjustContext(context, placedOnPos);
        }
        return super.place(context);
    }

    protected BlockPlaceContext adjustContext(BlockPlaceContext context, BlockPos placedOnPos) {
        BlockPos up = placedOnPos.above(2);
        return new AssemblyOperatorUseContext(context.m_43725_(), context.m_43723_(), context.m_43724_(), context.m_43722_(), new BlockHitResult(new Vec3((double) up.m_123341_() + 0.5 + (double) Direction.UP.getStepX() * 0.5, (double) up.m_123342_() + 0.5 + (double) Direction.UP.getStepY() * 0.5, (double) up.m_123343_() + 0.5 + (double) Direction.UP.getStepZ() * 0.5), Direction.UP, up, false));
    }

    protected boolean operatesOn(BlockState placedOnState) {
        return AllBlocks.BELT.has(placedOnState) ? placedOnState.m_61143_(BeltBlock.SLOPE) == BeltSlope.HORIZONTAL : AllBlocks.BASIN.has(placedOnState) || AllBlocks.DEPOT.has(placedOnState) || AllBlocks.WEIGHTED_EJECTOR.has(placedOnState);
    }
}