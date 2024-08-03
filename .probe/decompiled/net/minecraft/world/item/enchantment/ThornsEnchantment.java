package net.minecraft.world.item.enchantment;

import java.util.Map.Entry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class ThornsEnchantment extends Enchantment {

    private static final float CHANCE_PER_LEVEL = 0.15F;

    public ThornsEnchantment(Enchantment.Rarity enchantmentRarity0, EquipmentSlot... equipmentSlot1) {
        super(enchantmentRarity0, EnchantmentCategory.ARMOR_CHEST, equipmentSlot1);
    }

    @Override
    public int getMinCost(int int0) {
        return 10 + 20 * (int0 - 1);
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
        return itemStack0.getItem() instanceof ArmorItem ? true : super.canEnchant(itemStack0);
    }

    @Override
    public void doPostHurt(LivingEntity livingEntity0, Entity entity1, int int2) {
        RandomSource $$3 = livingEntity0.getRandom();
        Entry<EquipmentSlot, ItemStack> $$4 = EnchantmentHelper.getRandomItemWith(Enchantments.THORNS, livingEntity0);
        if (shouldHit(int2, $$3)) {
            if (entity1 != null) {
                entity1.hurt(livingEntity0.m_269291_().thorns(livingEntity0), (float) getDamage(int2, $$3));
            }
            if ($$4 != null) {
                ((ItemStack) $$4.getValue()).hurtAndBreak(2, livingEntity0, p_45208_ -> p_45208_.broadcastBreakEvent((EquipmentSlot) $$4.getKey()));
            }
        }
    }

    public static boolean shouldHit(int int0, RandomSource randomSource1) {
        return int0 <= 0 ? false : randomSource1.nextFloat() < 0.15F * (float) int0;
    }

    public static int getDamage(int int0, RandomSource randomSource1) {
        return int0 > 10 ? int0 - 10 : 1 + randomSource1.nextInt(4);
    }
}