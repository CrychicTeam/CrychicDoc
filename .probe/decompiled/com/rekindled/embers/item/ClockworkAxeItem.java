package com.rekindled.embers.item;

import com.rekindled.embers.util.EmbersTiers;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ClockworkAxeItem extends ClockworkToolItem {

    public ClockworkAxeItem(Item.Properties properties) {
        super(2.0F, -3.0F, EmbersTiers.CLOCKWORK_AXE, BlockTags.MINEABLE_WITH_AXE, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        ItemStack itemstack = pContext.getItemInHand();
        if (!this.hasEmber(itemstack)) {
            return InteractionResult.PASS;
        } else {
            Level level = pContext.getLevel();
            BlockPos blockpos = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            BlockState blockstate = level.getBlockState(blockpos);
            Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(pContext, ToolActions.AXE_STRIP, false));
            Optional<BlockState> optional1 = optional.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(pContext, ToolActions.AXE_SCRAPE, false));
            Optional<BlockState> optional2 = !optional.isPresent() && !optional1.isPresent() ? Optional.ofNullable(blockstate.getToolModifiedState(pContext, ToolActions.AXE_WAX_OFF, false)) : Optional.empty();
            Optional<BlockState> optional3 = Optional.empty();
            if (optional.isPresent()) {
                level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                optional3 = optional;
            } else if (optional1.isPresent()) {
                level.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.m_5898_(player, 3005, blockpos, 0);
                optional3 = optional1;
            } else if (optional2.isPresent()) {
                level.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.m_5898_(player, 3004, blockpos, 0);
                optional3 = optional2;
            }
            if (optional3.isPresent()) {
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
                }
                level.setBlock(blockpos, (BlockState) optional3.get(), 11);
                level.m_220407_(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, (BlockState) optional3.get()));
                if (player != null) {
                    itemstack.hurtAndBreak(1, player, p_150686_ -> p_150686_.m_21190_(pContext.getHand()));
                }
                itemstack.getOrCreateTag().putBoolean("didUse", true);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return this.hasEmber(stack) && ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchant) {
        return super.canApplyAtEnchantingTable(stack, enchant) && (enchant.category == EnchantmentCategory.WEAPON || enchant.category == EnchantmentCategory.DIGGER);
    }
}