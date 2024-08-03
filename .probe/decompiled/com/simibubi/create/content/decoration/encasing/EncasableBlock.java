package com.simibubi.create.content.decoration.encasing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface EncasableBlock {

    default InteractionResult tryEncase(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand, BlockHitResult ray) {
        for (Block block : EncasingRegistry.getVariants(state.m_60734_())) {
            if (block instanceof EncasedBlock encased && encased.getCasing().asItem() == heldItem.getItem()) {
                if (level.isClientSide) {
                    return InteractionResult.SUCCESS;
                }
                encased.handleEncasing(state, level, pos, heldItem, player, hand, ray);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}