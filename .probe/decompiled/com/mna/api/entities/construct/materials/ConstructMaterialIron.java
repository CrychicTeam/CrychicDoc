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

public class ConstructMaterialIron extends ConstructMaterial {

    public static final ResourceLocation texture = new ResourceLocation("mna", "textures/entity/animated_construct/iron.png");

    @Override
    public int getHealth() {
        return 14;
    }

    @Override
    public float getBuoyancy() {
        return -3.0F;
    }

    @Override
    public float getSpeed() {
        return 0.09F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.3F;
    }

    @Override
    public float getExplosionResistance() {
        return 0.04F;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public Tier getEquivalentTier() {
        return Tiers.IRON;
    }

    @Override
    public List<ItemStack> getDeathLootMaterialDrops(IConstruct<?> constrcut, DamageSource source) {
        return Arrays.asList(new ItemStack(Items.IRON_INGOT, 3));
    }

    @Override
    public float getCooldownMultiplierFor(ConstructCapability action) {
        return action != ConstructCapability.MINE && action != ConstructCapability.CHOP_WOOD ? 0.75F : 0.5F;
    }

    @Override
    public int getArmorBonus(ConstructSlot slot) {
        return 8;
    }

    @Override
    public int getToughnessBonus(ConstructSlot slot) {
        return 3;
    }

    @Override
    public float getDamageBonus() {
        return 3.0F;
    }

    @Override
    public int getIntelligenceBonus() {
        return 16;
    }

    @Override
    public int getManaStorage() {
        return 400;
    }

    @Override
    public float getRangedDamageBonus() {
        return 7.0F;
    }

    @Override
    public float getRangedManaCost() {
        return 50.0F;
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("mna", "iron");
    }

    @Override
    public int getBackpackCapacityBoost() {
        return 16;
    }
}