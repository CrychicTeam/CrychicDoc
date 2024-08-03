package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class MutableToolTier implements Tier {

    public final Tier parent;

    private int uses;

    private float speed;

    private float attackDamageBonus;

    private int level;

    private int enchantmentValue;

    private Ingredient repairIngredient;

    public MutableToolTier(Tier p) {
        this.parent = p;
        this.uses = this.parent.getUses();
        this.speed = this.parent.getSpeed();
        this.attackDamageBonus = this.parent.getAttackDamageBonus();
        this.level = this.parent.getLevel();
        this.enchantmentValue = this.parent.getEnchantmentValue();
        this.repairIngredient = this.parent.getRepairIngredient();
    }

    @RemapForJS("getUses")
    @Override
    public int getUses() {
        return this.uses;
    }

    public void setUses(int i) {
        this.uses = i;
    }

    @RemapForJS("getSpeed")
    @Override
    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float f) {
        this.speed = f;
    }

    @RemapForJS("getAttackDamageBonus")
    @Override
    public float getAttackDamageBonus() {
        return this.attackDamageBonus;
    }

    public void setAttackDamageBonus(float f) {
        this.attackDamageBonus = f;
    }

    @RemapForJS("getLevel")
    @Override
    public int getLevel() {
        return this.level;
    }

    public void setLevel(int i) {
        this.level = i;
    }

    @RemapForJS("getEnchantmentValue")
    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public void setEnchantmentValue(int i) {
        this.enchantmentValue = i;
    }

    @RemapForJS("getVanillaRepairIngredient")
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }

    public void setRepairIngredient(Ingredient in) {
        this.repairIngredient = in;
    }
}