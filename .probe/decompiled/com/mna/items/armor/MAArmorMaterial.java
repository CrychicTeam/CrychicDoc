package com.mna.items.armor;

import com.mna.items.ItemInit;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum MAArmorMaterial implements ArmorMaterial {

    BROKEN("broken", 5, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(ItemInit.INFUSED_SILK.get())),
    INFUSED_SILK("infused_silk", 5, new int[] { 1, 2, 3, 1 }, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(ItemInit.INFUSED_SILK.get())),
    BONE("bone", 33, new int[] { 3, 6, 8, 3 }, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> Ingredient.of(Items.WITHER_SKELETON_SKULL)),
    DEMON("demon", 33, new int[] { 3, 6, 8, 3 }, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> Ingredient.of(Items.NETHERITE_INGOT)),
    FEY("fey", 33, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(Blocks.MYCELIUM)),
    COUNCIL("council", 33, new int[] { 1, 4, 5, 2 }, 12, SoundEvents.ARMOR_EQUIP_DIAMOND, 0.0F, 0.0F, () -> Ingredient.of(ItemInit.RUNIC_SILK.get()));

    private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };

    private final String name;

    private final int maxDamageFactor;

    private final int[] damageReductionAmountArray;

    private final int enchantability;

    private final SoundEvent soundEvent;

    private final float toughness;

    private final float knockbackResistance;

    private final Supplier<Ingredient> repairMaterial;

    private MAArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type pType) {
        return MAX_DAMAGE_ARRAY[pType.getSlot().getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type pType) {
        return this.damageReductionAmountArray[pType.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return (Ingredient) this.repairMaterial.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getName() {
        return "mna:" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}