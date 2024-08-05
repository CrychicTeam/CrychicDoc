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

public class ConstructMaterialDiamond extends ConstructMaterial {

    public static final ResourceLocation texture = new ResourceLocation("mna", "textures/entity/animated_construct/diamond.png");

    @Override
    public int getHealth() {
        return 10;
    }

    @Override
    public float getBuoyancy() {
        return 0.0F;
    }

    @Override
    public float getSpeed() {
        return 0.15F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }

    @Override
    public float getExplosionResistance() {
        return 0.08F;
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
        return Arrays.asList(new ItemStack(Items.DIAMOND, 3));
    }

    @Override
    public float getCooldownMultiplierFor(ConstructCapability action) {
        return action != ConstructCapability.MINE && action != ConstructCapability.CHOP_WOOD ? 0.5F : 0.1F;
    }

    @Override
    public int getArmorBonus(ConstructSlot slot) {
        return 6;
    }

    @Override
    public int getToughnessBonus(ConstructSlot slot) {
        return 1;
    }

    @Override
    public float getDamageBonus() {
        return 4.0F;
    }

    @Override
    public int getIntelligenceBonus() {
        return 50;
    }

    @Override
    public int getManaStorage() {
        return 2000;
    }

    @Override
    public float getRangedDamageBonus() {
        return 15.0F;
    }

    @Override
    public float getRangedManaCost() {
        return 125.0F;
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("mna", "diamond");
    }

    @Override
    public int getBackpackCapacityBoost() {
        return 64;
    }
}