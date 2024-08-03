package net.minecraft.world.item.enchantment;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;

public abstract class Enchantment {

    private final EquipmentSlot[] slots;

    private final Enchantment.Rarity rarity;

    public final EnchantmentCategory category;

    @Nullable
    protected String descriptionId;

    @Nullable
    public static Enchantment byId(int int0) {
        return (Enchantment) BuiltInRegistries.ENCHANTMENT.m_7942_(int0);
    }

    protected Enchantment(Enchantment.Rarity enchantmentRarity0, EnchantmentCategory enchantmentCategory1, EquipmentSlot[] equipmentSlot2) {
        this.rarity = enchantmentRarity0;
        this.category = enchantmentCategory1;
        this.slots = equipmentSlot2;
    }

    public Map<EquipmentSlot, ItemStack> getSlotItems(LivingEntity livingEntity0) {
        Map<EquipmentSlot, ItemStack> $$1 = Maps.newEnumMap(EquipmentSlot.class);
        for (EquipmentSlot $$2 : this.slots) {
            ItemStack $$3 = livingEntity0.getItemBySlot($$2);
            if (!$$3.isEmpty()) {
                $$1.put($$2, $$3);
            }
        }
        return $$1;
    }

    public Enchantment.Rarity getRarity() {
        return this.rarity;
    }

    public int getMinLevel() {
        return 1;
    }

    public int getMaxLevel() {
        return 1;
    }

    public int getMinCost(int int0) {
        return 1 + int0 * 10;
    }

    public int getMaxCost(int int0) {
        return this.getMinCost(int0) + 5;
    }

    public int getDamageProtection(int int0, DamageSource damageSource1) {
        return 0;
    }

    public float getDamageBonus(int int0, MobType mobType1) {
        return 0.0F;
    }

    public final boolean isCompatibleWith(Enchantment enchantment0) {
        return this.checkCompatibility(enchantment0) && enchantment0.checkCompatibility(this);
    }

    protected boolean checkCompatibility(Enchantment enchantment0) {
        return this != enchantment0;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("enchantment", BuiltInRegistries.ENCHANTMENT.getKey(this));
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getFullname(int int0) {
        MutableComponent $$1 = Component.translatable(this.getDescriptionId());
        if (this.isCurse()) {
            $$1.withStyle(ChatFormatting.RED);
        } else {
            $$1.withStyle(ChatFormatting.GRAY);
        }
        if (int0 != 1 || this.getMaxLevel() != 1) {
            $$1.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + int0));
        }
        return $$1;
    }

    public boolean canEnchant(ItemStack itemStack0) {
        return this.category.canEnchant(itemStack0.getItem());
    }

    public void doPostAttack(LivingEntity livingEntity0, Entity entity1, int int2) {
    }

    public void doPostHurt(LivingEntity livingEntity0, Entity entity1, int int2) {
    }

    public boolean isTreasureOnly() {
        return false;
    }

    public boolean isCurse() {
        return false;
    }

    public boolean isTradeable() {
        return true;
    }

    public boolean isDiscoverable() {
        return true;
    }

    public static enum Rarity {

        COMMON(10), UNCOMMON(5), RARE(2), VERY_RARE(1);

        private final int weight;

        private Rarity(int p_44715_) {
            this.weight = p_44715_;
        }

        public int getWeight() {
            return this.weight;
        }
    }
}