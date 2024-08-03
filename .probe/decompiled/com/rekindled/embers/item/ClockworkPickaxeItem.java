package com.rekindled.embers.item;

import com.rekindled.embers.datagen.EmbersBlockTags;
import com.rekindled.embers.util.EmbersTiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ClockworkPickaxeItem extends ClockworkToolItem {

    public ClockworkPickaxeItem(Item.Properties properties) {
        super(1.0F, -2.8F, EmbersTiers.CLOCKWORK_PICK, EmbersBlockTags.MINABLE_WITH_PICKAXE_SHOVEL, properties);
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return this.hasEmber(stack) && (toolAction == ToolActions.PICKAXE_DIG || toolAction == ToolActions.SHOVEL_DIG);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchant) {
        return super.canApplyAtEnchantingTable(stack, enchant) && (enchant.category == EnchantmentCategory.WEAPON || enchant.category == EnchantmentCategory.DIGGER);
    }
}