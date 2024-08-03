package dev.latvian.mods.kubejs.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2BooleanFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentBuilder extends BuilderBase<Enchantment> {

    public transient Enchantment.Rarity rarity = Enchantment.Rarity.COMMON;

    public transient EnchantmentCategory category = EnchantmentCategory.DIGGER;

    public transient EquipmentSlot[] slots = new EquipmentSlot[] { EquipmentSlot.MAINHAND };

    public transient int minLevel = 1;

    public transient int maxLevel = 1;

    public transient Int2IntFunction minCost = null;

    public transient Int2IntFunction maxCost = null;

    public transient EnchantmentBuilder.DamageProtectionFunction damageProtection = null;

    public transient EnchantmentBuilder.DamageBonusFunction damageBonus = null;

    public transient Object2BooleanFunction<ResourceLocation> checkCompatibility = null;

    public transient Object2BooleanFunction<ItemStack> canEnchant = null;

    public transient EnchantmentBuilder.PostFunction postAttack = null;

    public transient EnchantmentBuilder.PostFunction postHurt = null;

    public transient boolean treasureOnly = false;

    public transient boolean curse = false;

    public transient boolean tradeable = true;

    public transient boolean discoverable = true;

    public EnchantmentBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public final RegistryInfo getRegistryType() {
        return RegistryInfo.ENCHANTMENT;
    }

    public Enchantment createObject() {
        return new BasicEnchantment(this);
    }

    public EnchantmentBuilder rarity(Enchantment.Rarity r) {
        this.rarity = r;
        return this;
    }

    public EnchantmentBuilder uncommon() {
        return this.rarity(Enchantment.Rarity.UNCOMMON);
    }

    public EnchantmentBuilder rare() {
        return this.rarity(Enchantment.Rarity.RARE);
    }

    public EnchantmentBuilder veryRare() {
        return this.rarity(Enchantment.Rarity.VERY_RARE);
    }

    public EnchantmentBuilder category(EnchantmentCategory c) {
        this.category = c;
        return this;
    }

    public EnchantmentBuilder slots(EquipmentSlot[] s) {
        this.slots = s;
        return this;
    }

    public EnchantmentBuilder armor() {
        return this.category(EnchantmentCategory.ARMOR).slots(new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET });
    }

    public EnchantmentBuilder armorHead() {
        return this.category(EnchantmentCategory.ARMOR_HEAD).slots(new EquipmentSlot[] { EquipmentSlot.HEAD });
    }

    public EnchantmentBuilder armorChest() {
        return this.category(EnchantmentCategory.ARMOR_CHEST).slots(new EquipmentSlot[] { EquipmentSlot.CHEST });
    }

    public EnchantmentBuilder armorLegs() {
        return this.category(EnchantmentCategory.ARMOR_LEGS).slots(new EquipmentSlot[] { EquipmentSlot.LEGS });
    }

    public EnchantmentBuilder armorFeet() {
        return this.category(EnchantmentCategory.ARMOR_FEET).slots(new EquipmentSlot[] { EquipmentSlot.FEET });
    }

    public EnchantmentBuilder weapon() {
        return this.category(EnchantmentCategory.WEAPON);
    }

    public EnchantmentBuilder fishingRod() {
        return this.category(EnchantmentCategory.FISHING_ROD);
    }

    public EnchantmentBuilder trident() {
        return this.category(EnchantmentCategory.TRIDENT);
    }

    public EnchantmentBuilder breakable() {
        return this.category(EnchantmentCategory.BREAKABLE).slots(EquipmentSlot.values());
    }

    public EnchantmentBuilder bow() {
        return this.category(EnchantmentCategory.BOW);
    }

    public EnchantmentBuilder wearable() {
        return this.category(EnchantmentCategory.WEARABLE);
    }

    public EnchantmentBuilder crossbow() {
        return this.category(EnchantmentCategory.CROSSBOW);
    }

    public EnchantmentBuilder vanishable() {
        return this.category(EnchantmentCategory.VANISHABLE).slots(EquipmentSlot.values());
    }

    public EnchantmentBuilder minLevel(int i) {
        this.minLevel = i;
        return this;
    }

    public EnchantmentBuilder maxLevel(int i) {
        this.maxLevel = i;
        return this;
    }

    public EnchantmentBuilder minCost(Int2IntFunction i) {
        this.minCost = i;
        return this;
    }

    public EnchantmentBuilder maxCost(Int2IntFunction i) {
        this.maxCost = i;
        return this;
    }

    public EnchantmentBuilder damageProtection(EnchantmentBuilder.DamageProtectionFunction i) {
        this.damageProtection = i;
        return this;
    }

    public EnchantmentBuilder damageBonus(EnchantmentBuilder.DamageBonusFunction i) {
        this.damageBonus = i;
        return this;
    }

    public EnchantmentBuilder checkCompatibility(Object2BooleanFunction<ResourceLocation> i) {
        this.checkCompatibility = i;
        return this;
    }

    public EnchantmentBuilder canEnchant(Object2BooleanFunction<ItemStack> i) {
        this.canEnchant = i;
        return this;
    }

    public EnchantmentBuilder postAttack(EnchantmentBuilder.PostFunction i) {
        this.postAttack = i;
        return this;
    }

    public EnchantmentBuilder postHurt(EnchantmentBuilder.PostFunction i) {
        this.postHurt = i;
        return this;
    }

    public EnchantmentBuilder treasureOnly() {
        this.treasureOnly = true;
        return this;
    }

    public EnchantmentBuilder curse() {
        this.curse = true;
        return this;
    }

    public EnchantmentBuilder untradeable() {
        this.tradeable = false;
        return this;
    }

    public EnchantmentBuilder undiscoverable() {
        this.discoverable = false;
        return this;
    }

    @FunctionalInterface
    public interface DamageBonusFunction {

        float getDamageBonus(int var1, String var2);
    }

    @FunctionalInterface
    public interface DamageProtectionFunction {

        int getDamageProtection(int var1, DamageSource var2);
    }

    @FunctionalInterface
    public interface PostFunction {

        void apply(LivingEntity var1, Entity var2, int var3);
    }
}