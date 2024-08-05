package com.mna.items.ritual;

import com.mna.api.items.TieredItem;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemWizardChalk extends TieredItem {

    public ItemWizardChalk() {
        super(new Item.Properties().durability(150));
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide()) {
            if (context.getLevel().getBlockState(context.getClickedPos()).m_60783_(context.getLevel(), context.getClickedPos(), Direction.UP) && context.getLevel().m_46859_(context.getClickedPos().above())) {
                context.getLevel().setBlockAndUpdate(context.getClickedPos().above(), (BlockState) ((BlockState) ((BlockState) BlockInit.CHALK_RUNE.get().defaultBlockState().m_61124_(ChalkRuneBlock.RUNEINDEX, (int) Math.floor(Math.random() * (double) (ChalkRuneBlock.RUNEINDEX.getPossibleValues().size() - 1)))).m_61124_(ChalkRuneBlock.METAL, false)).m_61124_(ChalkRuneBlock.ACTIVATED, false));
                context.getItemInHand().hurtAndBreak(1, context.getPlayer(), player -> player.m_21190_(context.getHand()));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        int usesRemaining = stack.getMaxDamage() - stack.getDamageValue();
        tooltip.add(Component.translatable("item.mna.wizard_chalk_uses", usesRemaining));
    }
}