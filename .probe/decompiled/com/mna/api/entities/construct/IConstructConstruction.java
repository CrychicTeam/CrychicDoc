package com.mna.api.entities.construct;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.base.ISpellDefinition;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IConstructConstruction {

    boolean isCapabilityEnabled(ConstructCapability var1);

    boolean areCapabilitiesEnabled(ConstructCapability... var1);

    boolean isAnyCapabilityEnabled(ConstructCapability... var1);

    @Nullable
    ConstructMaterial getLowestMaterialCooldownMultiplierForCapability(ConstructCapability var1);

    List<ConstructMaterial> getComposition();

    default ItemStack setPart(ItemStack partStack) {
        return this.setPart(partStack, false);
    }

    ItemStack setPart(ItemStack var1, boolean var2);

    ItemStack getHat();

    void setHat(ItemStack var1);

    ItemStack getBanner();

    void setBanner(ItemStack var1);

    ISpellDefinition[] getCastableSpells();

    List<ItemConstructPart> getPartsForMaterial(ConstructMaterial var1);

    Affinity getRandomContainedAffinity();

    void resetParts();

    int calculateArmor();

    int calculateAttackRate();

    float calculateBuoyancy();

    float calculateDamage();

    float calculateExplosionResistance();

    int calculateFluidCapacity();

    int calculateIntelligence();

    float calculateKnockback();

    float calculateKnockbackResistance();

    float calculateMana();

    int calculateMaxHealth();

    int calculatePerception();

    float calculateRangedDamage();

    float calculateSpeed();

    int calculateToughness();

    boolean isComplete();

    boolean isEmpty();

    IConstructConstruction copy();

    int getAffinityScore(Affinity var1);

    void setAffinityScore(Affinity var1, int var2);

    ConstructCapability[] getEnabledCapabilities();

    Optional<ItemConstructPart> getPart(ConstructSlot var1);

    ItemStack removePart(ConstructSlot var1);

    void ReadNBT(CompoundTag var1);

    void WriteNBT(CompoundTag var1);

    int calculateInventorySize();

    int calculateInventoryStackLimit();
}