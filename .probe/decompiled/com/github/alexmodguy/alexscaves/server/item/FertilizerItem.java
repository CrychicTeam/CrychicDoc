package com.github.alexmodguy.alexscaves.server.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

public class FertilizerItem extends Item {

    public FertilizerItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(context.getClickedFace());
        ItemStack itemStack = context.getItemInHand();
        if (applyFertilizer(itemStack, level, blockpos, context.getPlayer())) {
            if (!level.isClientSide) {
                level.m_46796_(1505, blockpos, 0);
            }
            if (!context.getPlayer().isCreative()) {
                itemStack.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            BlockState blockstate = level.getBlockState(blockpos);
            boolean flag = blockstate.m_60783_(level, blockpos, context.getClickedFace());
            if (flag && BoneMealItem.growWaterPlant(itemStack, level, blockpos1, context.getClickedFace())) {
                if (!level.isClientSide) {
                    level.m_46796_(1505, blockpos1, 0);
                }
                if (!context.getPlayer().isCreative()) {
                    itemStack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    private static boolean applyFertilizer(ItemStack itemStack, Level level, BlockPos blockPos, Player player) {
        BlockState blockstate = level.getBlockState(blockPos);
        int hook = ForgeEventFactory.onApplyBonemeal(player, level, blockPos, blockstate, itemStack);
        if (hook != 0) {
            return hook > 0;
        } else {
            if (blockstate.m_60734_() instanceof BonemealableBlock) {
                BonemealableBlock bonemealableblock = (BonemealableBlock) blockstate.m_60734_();
                if (bonemealableblock.isValidBonemealTarget(level, blockPos, blockstate, level.isClientSide)) {
                    if (level instanceof ServerLevel) {
                        for (int boneMealAttempts = 0; boneMealAttempts < 4; boneMealAttempts++) {
                            bonemealableblock.performBonemeal((ServerLevel) level, level.random, blockPos, blockstate);
                            blockstate = level.getBlockState(blockPos);
                            if (!(blockstate.m_60734_() instanceof BonemealableBlock)) {
                                return true;
                            }
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }
}