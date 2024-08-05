package net.mehvahdjukaar.amendments.events.behaviors;

import net.mehvahdjukaar.amendments.common.item.DyeBottleItem;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.block.IRecolorable;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

class DyeBehavior implements ItemUseOnBlock {

    @Override
    public boolean altersWorld() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (Boolean) CommonConfigs.DYE_BLOCKS.get();
    }

    @Override
    public boolean appliesToItem(Item item) {
        return ForgeHelper.getColor(new ItemStack(item)) != null || item == ModRegistry.DYE_BOTTLE_ITEM.get();
    }

    @Override
    public InteractionResult tryPerformingAction(Level level, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);
        boolean isBottle = false;
        DyeColor color;
        if (stack.getItem() instanceof DyeBottleItem) {
            isBottle = true;
            color = DyeBottleItem.getClosestDye(stack);
        } else {
            color = ForgeHelper.getColor(stack);
        }
        if (this.recolor(level, pos, state, color)) {
            if (isBottle) {
                level.playSound(player, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            } else {
                level.playSound(player, pos, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            if (player instanceof ServerPlayer serverPlayer) {
                if (!player.isCreative()) {
                    if (isBottle) {
                        Utils.swapItem(player, hand, stack, Items.GLASS_BOTTLE.getDefaultInstance());
                    } else {
                        stack.shrink(1);
                    }
                }
                level.m_142346_(player, GameEvent.BLOCK_CHANGE, pos);
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                serverPlayer.m_36246_(Stats.ITEM_USED.get(stack.getItem()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    private boolean recolor(Level level, BlockPos pos, BlockState state, DyeColor color) {
        if (state.m_60734_() instanceof IRecolorable recolorable) {
            if (recolorable.isDefaultColor(level, pos, state)) {
                return recolorable.tryRecolor(level, pos, state, color);
            }
        } else {
            Block newBlock = BlocksColorAPI.changeColor(state.m_60734_(), color);
            if (newBlock != null && !state.m_60713_(newBlock) && BlocksColorAPI.isDefaultColor(state.m_60734_())) {
                BlockState newState = newBlock.withPropertiesOf(state);
                if (newState.m_245147_()) {
                    level.setBlockAndUpdate(pos, newState);
                }
                return true;
            }
        }
        return false;
    }
}