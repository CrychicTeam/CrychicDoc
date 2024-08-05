package dev.latvian.mods.kubejs.misc;

import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class BasicEnchantment extends Enchantment {

    public final EnchantmentBuilder enchantmentBuilder;

    public BasicEnchantment(EnchantmentBuilder b) {
        super(b.rarity, b.category, b.slots);
        this.enchantmentBuilder = b;
    }

    @Override
    public int getMinLevel() {
        return this.enchantmentBuilder.minLevel;
    }

    @Override
    public int getMaxLevel() {
        return this.enchantmentBuilder.maxLevel;
    }

    @Override
    public int getMinCost(int i) {
        return this.enchantmentBuilder.minCost != null ? this.enchantmentBuilder.minCost.get(i) : super.getMinCost(i);
    }

    @Override
    public int getMaxCost(int i) {
        return this.enchantmentBuilder.maxCost != null ? this.enchantmentBuilder.maxCost.get(i) : super.getMaxCost(i);
    }

    @Override
    public int getDamageProtection(int i, DamageSource damageSource) {
        return this.enchantmentBuilder.damageProtection != null ? this.enchantmentBuilder.damageProtection.getDamageProtection(i, damageSource) : super.getDamageProtection(i, damageSource);
    }

    @Override
    public float getDamageBonus(int i, MobType mobType) {
        return this.enchantmentBuilder.damageBonus != null ? this.enchantmentBuilder.damageBonus.getDamageBonus(i, UtilsJS.getMobTypeId(mobType)) : super.getDamageBonus(i, mobType);
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) {
        if (enchantment == this) {
            return false;
        } else {
            return this.enchantmentBuilder.checkCompatibility != null ? (Boolean) this.enchantmentBuilder.checkCompatibility.apply(RegistryInfo.ENCHANTMENT.getId(enchantment)) : true;
        }
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        if (super.canEnchant(itemStack)) {
            return true;
        } else {
            return this.enchantmentBuilder.canEnchant != null ? (Boolean) this.enchantmentBuilder.canEnchant.apply(itemStack) : false;
        }
    }

    @Override
    public void doPostAttack(LivingEntity entity, Entity target, int level) {
        if (this.enchantmentBuilder.postAttack != null) {
            this.enchantmentBuilder.postAttack.apply(entity, target, level);
        }
    }

    @Override
    public void doPostHurt(LivingEntity entity, Entity target, int level) {
        if (this.enchantmentBuilder.postHurt != null) {
            this.enchantmentBuilder.postHurt.apply(entity, target, level);
        }
    }

    @Override
    public boolean isTreasureOnly() {
        return this.enchantmentBuilder.treasureOnly;
    }

    @Override
    public boolean isCurse() {
        return this.enchantmentBuilder.curse;
    }

    @Override
    public boolean isTradeable() {
        return this.enchantmentBuilder.tradeable;
    }

    @Override
    public boolean isDiscoverable() {
        return this.enchantmentBuilder.discoverable;
    }
}