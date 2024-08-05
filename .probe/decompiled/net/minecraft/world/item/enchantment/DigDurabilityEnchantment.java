package net.minecraft.world.item.enchantment;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class DigDurabilityEnchantment extends Enchantment {

    protected DigDurabilityEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.BREAKABLE, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 5 + (int0 - 1) * 8;
    }

    @Override
    public int getMaxCost(int int0) {
        return super.getMinCost(int0) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack0) {
        return itemStack0.isDamageableItem() ? true : super.canEnchant(itemStack0);
    }

    public static boolean shouldIgnoreDurabilityDrop(ItemStack itemStack0, int int1, RandomSource randomSource2) {
        return itemStack0.getItem() instanceof ArmorItem && randomSource2.nextFloat() < 0.6F ? false : randomSource2.nextInt(int1 + 1) > 0;
    }
}