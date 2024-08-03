package com.mna.api.entities.construct;

import com.mna.api.entities.construct.materials.ConstructMaterialDiamond;
import com.mna.api.entities.construct.materials.ConstructMaterialGold;
import com.mna.api.entities.construct.materials.ConstructMaterialIron;
import com.mna.api.entities.construct.materials.ConstructMaterialObsidian;
import com.mna.api.entities.construct.materials.ConstructMaterialStone;
import com.mna.api.entities.construct.materials.ConstructMaterialUnknown;
import com.mna.api.entities.construct.materials.ConstructMaterialWickerwood;
import com.mna.api.entities.construct.materials.ConstructMaterialWood;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public abstract class ConstructMaterial {

    public static List<ConstructMaterial> ALL_MATERIALS = new ArrayList();

    public static ConstructMaterial UNKNOWN = new ConstructMaterialUnknown();

    public static ConstructMaterial WICKERWOOD = new ConstructMaterialWickerwood();

    public static ConstructMaterial WOOD = new ConstructMaterialWood();

    public static ConstructMaterial STONE = new ConstructMaterialStone();

    public static ConstructMaterial IRON = new ConstructMaterialIron();

    public static ConstructMaterial GOLD = new ConstructMaterialGold();

    public static ConstructMaterial OBSIDIAN = new ConstructMaterialObsidian();

    public static ConstructMaterial DIAMOND = new ConstructMaterialDiamond();

    public abstract int getHealth();

    public abstract float getBuoyancy();

    public abstract float getSpeed();

    public abstract ResourceLocation getTexture();

    public abstract ResourceLocation getId();

    public abstract float getKnockbackResistance();

    public abstract float getExplosionResistance();

    public abstract Tier getEquivalentTier();

    public abstract float getCooldownMultiplierFor(ConstructCapability var1);

    public abstract List<ItemStack> getDeathLootMaterialDrops(IConstruct<?> var1, DamageSource var2);

    public abstract int getArmorBonus(ConstructSlot var1);

    public abstract int getToughnessBonus(ConstructSlot var1);

    public abstract float getDamageBonus();

    public abstract float getRangedDamageBonus();

    public abstract float getRangedManaCost();

    public abstract int getManaStorage();

    public abstract int getIntelligenceBonus();

    public abstract int getBackpackCapacityBoost();

    static {
        ALL_MATERIALS.add(WICKERWOOD);
        ALL_MATERIALS.add(WOOD);
        ALL_MATERIALS.add(STONE);
        ALL_MATERIALS.add(IRON);
        ALL_MATERIALS.add(GOLD);
        ALL_MATERIALS.add(OBSIDIAN);
        ALL_MATERIALS.add(DIAMOND);
    }
}