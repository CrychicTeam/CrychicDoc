package com.github.alexmodguy.alexscaves.server.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class StrippableLogBlock extends RotatedPillarBlock {

    public StrippableLogBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        ItemStack itemStack = context.getItemInHand();
        if (!itemStack.canPerformAction(toolAction)) {
            return null;
        } else {
            if (ToolActions.AXE_STRIP == toolAction) {
                if (this == ACBlockRegistry.PEWEN_LOG.get()) {
                    return (BlockState) ACBlockRegistry.STRIPPED_PEWEN_LOG.get().defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS));
                }
                if (this == ACBlockRegistry.PEWEN_WOOD.get()) {
                    return (BlockState) ACBlockRegistry.STRIPPED_PEWEN_WOOD.get().defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS));
                }
                if (this == ACBlockRegistry.THORNWOOD_LOG.get()) {
                    return (BlockState) ACBlockRegistry.STRIPPED_THORNWOOD_LOG.get().defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS));
                }
                if (this == ACBlockRegistry.THORNWOOD_WOOD.get()) {
                    return (BlockState) ACBlockRegistry.STRIPPED_THORNWOOD_WOOD.get().defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS));
                }
            }
            return super.getToolModifiedState(state, context, toolAction, simulate);
        }
    }
}