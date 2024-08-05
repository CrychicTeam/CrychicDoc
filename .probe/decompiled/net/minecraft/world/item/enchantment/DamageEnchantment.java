package net.minecraft.world.item.enchantment;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;

public class DamageEnchantment extends Enchantment {

    public static final int ALL = 0;

    public static final int UNDEAD = 1;

    public static final int ARTHROPODS = 2;

    private static final String[] NAMES = new String[] { "all", "undead", "arthropods" };

    private static final int[] MIN_COST = new int[] { 1, 5, 5 };

    private static final int[] LEVEL_COST = new int[] { 11, 8, 8 };

    private static final int[] LEVEL_COST_SPAN = new int[] { 20, 20, 20 };

    public final int type;

    public DamageEnchantment(Enchantment.Rarity enchantmentRarity0, int int1, EquipmentSlot... equipmentSlot2) {
        super(enchantmentRarity0, EnchantmentCategory.WEAPON, equipmentSlot2);
        this.type = int1;
    }

    @Override
    public int getMinCost(int int0) {
        return MIN_COST[this.type] + (int0 - 1) * LEVEL_COST[this.type];
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + LEVEL_COST_SPAN[this.type];
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public float getDamageBonus(int int0, MobType mobType1) {
        if (this.type == 0) {
            return 1.0F + (float) Math.max(0, int0 - 1) * 0.5F;
        } else if (this.type == 1 && mobType1 == MobType.UNDEAD) {
            return (float) int0 * 2.5F;
        } else {
            return this.type == 2 && mobType1 == MobType.ARTHROPOD ? (float) int0 * 2.5F : 0.0F;
        }
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment0) {
        return !(enchantment0 instanceof DamageEnchantment);
    }

    @Override
    public boolean canEnchant(ItemStack itemStack0) {
        return itemStack0.getItem() instanceof AxeItem ? true : super.canEnchant(itemStack0);
    }

    @Override
    public void doPostAttack(LivingEntity livingEntity0, Entity entity1, int int2) {
        if (entity1 instanceof LivingEntity $$3 && this.type == 2 && int2 > 0 && $$3.getMobType() == MobType.ARTHROPOD) {
            int $$4 = 20 + livingEntity0.getRandom().nextInt(10 * int2);
            $$3.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, $$4, 3));
        }
    }
}