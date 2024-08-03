package net.minecraft.world.item.enchantment;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class ProtectionEnchantment extends Enchantment {

    public final ProtectionEnchantment.Type type;

    public ProtectionEnchantment(Enchantment.Rarity enchantmentRarity0, ProtectionEnchantment.Type protectionEnchantmentType1, EquipmentSlot... equipmentSlot2) {
        super(enchantmentRarity0, protectionEnchantmentType1 == ProtectionEnchantment.Type.FALL ? EnchantmentCategory.ARMOR_FEET : EnchantmentCategory.ARMOR, equipmentSlot2);
        this.type = protectionEnchantmentType1;
    }

    @Override
    public int getMinCost(int int0) {
        return this.type.getMinCost() + (int0 - 1) * this.type.getLevelCost();
    }

    @Override
    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + this.type.getLevelCost();
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getDamageProtection(int int0, DamageSource damageSource1) {
        if (damageSource1.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return 0;
        } else if (this.type == ProtectionEnchantment.Type.ALL) {
            return int0;
        } else if (this.type == ProtectionEnchantment.Type.FIRE && damageSource1.is(DamageTypeTags.IS_FIRE)) {
            return int0 * 2;
        } else if (this.type == ProtectionEnchantment.Type.FALL && damageSource1.is(DamageTypeTags.IS_FALL)) {
            return int0 * 3;
        } else if (this.type == ProtectionEnchantment.Type.EXPLOSION && damageSource1.is(DamageTypeTags.IS_EXPLOSION)) {
            return int0 * 2;
        } else {
            return this.type == ProtectionEnchantment.Type.PROJECTILE && damageSource1.is(DamageTypeTags.IS_PROJECTILE) ? int0 * 2 : 0;
        }
    }

    @Override
    public boolean checkCompatibility(Enchantment enchantment0) {
        if (enchantment0 instanceof ProtectionEnchantment $$1) {
            return this.type == $$1.type ? false : this.type == ProtectionEnchantment.Type.FALL || $$1.type == ProtectionEnchantment.Type.FALL;
        } else {
            return super.checkCompatibility(enchantment0);
        }
    }

    public static int getFireAfterDampener(LivingEntity livingEntity0, int int1) {
        int $$2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, livingEntity0);
        if ($$2 > 0) {
            int1 -= Mth.floor((float) int1 * (float) $$2 * 0.15F);
        }
        return int1;
    }

    public static double getExplosionKnockbackAfterDampener(LivingEntity livingEntity0, double double1) {
        int $$2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, livingEntity0);
        if ($$2 > 0) {
            double1 *= Mth.clamp(1.0 - (double) $$2 * 0.15, 0.0, 1.0);
        }
        return double1;
    }

    public static enum Type {

        ALL(1, 11), FIRE(10, 8), FALL(5, 6), EXPLOSION(5, 8), PROJECTILE(3, 6);

        private final int minCost;

        private final int levelCost;

        private Type(int p_151299_, int p_151300_) {
            this.minCost = p_151299_;
            this.levelCost = p_151300_;
        }

        public int getMinCost() {
            return this.minCost;
        }

        public int getLevelCost() {
            return this.levelCost;
        }
    }
}