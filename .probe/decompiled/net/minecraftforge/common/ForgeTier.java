package net.minecraftforge.common;

import java.util.function.Supplier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public final class ForgeTier implements Tier {

    private final int level;

    private final int uses;

    private final float speed;

    private final float attackDamageBonus;

    private final int enchantmentValue;

    @NotNull
    private final TagKey<Block> tag;

    @NotNull
    private final Supplier<Ingredient> repairIngredient;

    public ForgeTier(int level, int uses, float speed, float attackDamageBonus, int enchantmentValue, @NotNull TagKey<Block> tag, @NotNull Supplier<Ingredient> repairIngredient) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
        this.tag = tag;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @NotNull
    public TagKey<Block> getTag() {
        return this.tag;
    }

    @NotNull
    @Override
    public Ingredient getRepairIngredient() {
        return (Ingredient) this.repairIngredient.get();
    }

    public String toString() {
        return "ForgeTier[level=" + this.level + ", uses=" + this.uses + ", speed=" + this.speed + ", attackDamageBonus=" + this.attackDamageBonus + ", enchantmentValue=" + this.enchantmentValue + ", tag=" + this.tag + ", repairIngredient=" + this.repairIngredient + "]";
    }
}