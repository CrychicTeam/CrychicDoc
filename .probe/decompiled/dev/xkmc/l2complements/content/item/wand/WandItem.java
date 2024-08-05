package dev.xkmc.l2complements.content.item.wand;

import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.data.TagGen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class WandItem extends Item {

    public WandItem(Item.Properties prop) {
        super(prop);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return LCConfig.COMMON.enableWandEnchantments.get();
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (!LCConfig.COMMON.enableWandEnchantments.get()) {
            return false;
        } else {
            if (LCConfig.COMMON.useTagsForWandEnchantmentWhiteList.get()) {
                ITagManager<Enchantment> manager = ForgeRegistries.ENCHANTMENTS.tags();
                if (manager != null) {
                    return manager.getTag(TagGen.WAND_ENCH).contains(enchantment);
                }
            }
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }
}