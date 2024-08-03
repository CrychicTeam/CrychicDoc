package net.minecraftforge.common.extensions;

import java.util.Set;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public interface IForgeEnchantment {

    private Enchantment self() {
        return (Enchantment) this;
    }

    default float getDamageBonus(int level, MobType mobType, ItemStack enchantedItem) {
        return this.self().getDamageBonus(level, mobType);
    }

    default boolean allowedInCreativeTab(Item book, Set<EnchantmentCategory> allowedCategories) {
        return this.self().isAllowedOnBooks() && allowedCategories.contains(this.self().category);
    }
}