package dev.xkmc.l2archery.content.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public interface BowEnchantmentSupplier<T extends BaseBowEnchantment> {

    T get(Enchantment.Rarity var1, EnchantmentCategory var2, EquipmentSlot[] var3, int var4);
}