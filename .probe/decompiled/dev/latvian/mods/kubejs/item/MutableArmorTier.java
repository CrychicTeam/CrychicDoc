package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class MutableArmorTier implements ArmorMaterial {

    private static final int[] HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };

    public final ArmorMaterial parent;

    private int durabilityMultiplier;

    private int[] slotProtections;

    private int enchantmentValue;

    private SoundEvent sound;

    private float toughness;

    private float knockbackResistance;

    private Ingredient repairIngredient;

    private String name;

    public MutableArmorTier(String id, ArmorMaterial p) {
        this.parent = p;
        this.enchantmentValue = p.getEnchantmentValue();
        this.sound = p.getEquipSound();
        this.repairIngredient = this.parent.getRepairIngredient();
        this.toughness = p.getToughness();
        this.knockbackResistance = p.getKnockbackResistance();
        this.name = id;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type equipmentSlot) {
        return this.durabilityMultiplier == 0 ? this.parent.getDurabilityForType(equipmentSlot) : HEALTH_PER_SLOT[equipmentSlot.getSlot().getIndex()] * this.durabilityMultiplier;
    }

    public void setDurabilityMultiplier(int m) {
        this.durabilityMultiplier = m;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type equipmentSlot) {
        return this.slotProtections == null ? this.parent.getDefenseForType(equipmentSlot) : this.slotProtections[equipmentSlot.getSlot().getIndex()];
    }

    public void setSlotProtections(int[] p) {
        this.slotProtections = p;
    }

    @RemapForJS("getEnchantmentValue")
    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public void setEnchantmentValue(int i) {
        this.enchantmentValue = i;
    }

    @RemapForJS("getEquipSound")
    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public void setEquipSound(SoundEvent e) {
        this.sound = e;
    }

    @RemapForJS("getVanillaRepairIngredient")
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }

    public void setRepairIngredient(Ingredient in) {
        this.repairIngredient = in;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @RemapForJS("getToughness")
    @Override
    public float getToughness() {
        return this.toughness;
    }

    public void setToughness(float f) {
        this.toughness = f;
    }

    @RemapForJS("getKnockbackResistance")
    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public void setKnockbackResistance(float f) {
        this.knockbackResistance = f;
    }
}