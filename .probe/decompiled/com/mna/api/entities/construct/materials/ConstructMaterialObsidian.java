package com.mna.api.entities.construct.materials;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.IConstruct;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public class ConstructMaterialObsidian extends ConstructMaterial {

    public static final ResourceLocation texture = new ResourceLocation("mna", "textures/entity/animated_construct/obsidian.png");

    @Override
    public int getHealth() {
        return 8;
    }

    @Override
    public float getBuoyancy() {
        return 0.2F;
    }

    @Override
    public float getSpeed() {
        return 0.09F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.4F;
    }

    @Override
    public float getExplosionResistance() {
        return 0.16F;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public Tier getEquivalentTier() {
        return Tiers.DIAMOND;
    }

    @Override
    public List<ItemStack> getDeathLootMaterialDrops(IConstruct<?> constrcut, DamageSource source) {
        return Arrays.asList(new ItemStack(Items.OBSIDIAN, 3));
    }

    @Override
    public float getCooldownMultiplierFor(ConstructCapability action) {
        return action != ConstructCapability.MINE && action != ConstructCapability.CHOP_WOOD ? 0.65F : 0.4F;
    }

    @Override
    public int getArmorBonus(ConstructSlot slot) {
        return 5;
    }

    @Override
    public int getToughnessBonus(ConstructSlot slot) {
        return 0;
    }

    @Override
    public float getDamageBonus() {
        return 5.0F;
    }

    @Override
    public int getIntelligenceBonus() {
        return 20;
    }

    @Override
    public int getManaStorage() {
        return 1000;
    }

    @Override
    public float getRangedDamageBonus() {
        return 10.0F;
    }

    @Override
    public float getRangedManaCost() {
        return 100.0F;
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("mna", "obsidian");
    }

    @Override
    public int getBackpackCapacityBoost() {
        return 32;
    }
}