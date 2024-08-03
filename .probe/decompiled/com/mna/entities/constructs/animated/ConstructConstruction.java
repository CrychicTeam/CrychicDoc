package com.mna.entities.constructs.animated;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.IConstructConstruction;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class ConstructConstruction implements IConstructConstruction {

    private static final String KEY_NBT = "animated_construct_composition";

    private static final String KEY_AFFINITY = "animated_construct_affinity";

    private static final String KEY_SPELLS = "animated_construct_spells";

    private static final String KEY_HAT = "animated_construct_hat";

    private static final String KEY_BANNER = "animated_construct_banner";

    private static final float WIND_SPEED_MODIFIER = 0.015F;

    private static final float WATER_BUOYANCY_MODIFIER = 4.0F;

    private HashMap<ConstructSlot, ItemConstructPart> partsList;

    private ArrayList<ConstructMaterial> composition;

    private ArrayList<ConstructCapability> enabledCapabilities;

    private HashMap<Affinity, Integer> affinityData;

    private HashMap<ConstructSlot, ISpellDefinition> castableSpells;

    private ItemStack hat = ItemStack.EMPTY;

    private ItemStack banner = ItemStack.EMPTY;

    public ConstructConstruction() {
        this.partsList = new HashMap();
        this.composition = new ArrayList();
        this.enabledCapabilities = new ArrayList();
        this.affinityData = new HashMap();
        this.castableSpells = new HashMap();
    }

    @Override
    public boolean isComplete() {
        for (ConstructSlot slot : ConstructSlot.values()) {
            if (!this.partsList.containsKey(slot) || this.partsList.get(slot) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        for (ConstructSlot slot : ConstructSlot.values()) {
            if (this.partsList.containsKey(slot) && this.partsList.get(slot) != null) {
                return false;
            }
        }
        return true;
    }

    private void calculateCompositionAndCapabilities() {
        this.composition.clear();
        this.enabledCapabilities.clear();
        for (ItemConstructPart part : this.partsList.values()) {
            if (!this.composition.contains(part.getMaterial())) {
                this.composition.add(part.getMaterial());
            }
            for (ConstructCapability cap : part.getEnabledCapabilities()) {
                if (!this.enabledCapabilities.contains(cap)) {
                    this.enabledCapabilities.add(cap);
                }
            }
        }
    }

    @Override
    public float calculateExplosionResistance() {
        float resistance = 0.0F;
        for (ItemConstructPart part : this.partsList.values()) {
            resistance += part.getMaterial().getExplosionResistance();
        }
        return resistance;
    }

    @Override
    public float calculateBuoyancy() {
        float buoyancy = 0.0F;
        for (ItemConstructPart part : this.partsList.values()) {
            buoyancy += part.getMaterial().getBuoyancy();
        }
        return buoyancy + (float) this.getAffinityScore(Affinity.WATER) * 4.0F;
    }

    @Override
    public int calculateMaxHealth() {
        int maxHealth = 0;
        for (ItemConstructPart part : this.partsList.values()) {
            maxHealth += part.getMaterial().getHealth();
        }
        return maxHealth;
    }

    @Override
    public float calculateSpeed() {
        float speed = 0.0F;
        for (ItemConstructPart part : this.partsList.values()) {
            speed += part.getMaterial().getSpeed() + part.getBonusSpeed();
        }
        return speed + (float) this.getAffinityScore(Affinity.WIND) * 0.015F;
    }

    @Override
    public int calculateAttackRate() {
        int attackRate = 20;
        for (ItemConstructPart part : this.partsList.values()) {
            attackRate += part.getAttackSpeedModifier();
        }
        return attackRate;
    }

    @Override
    public float calculateDamage() {
        float damage = 1.0F;
        for (ItemConstructPart part : this.partsList.values()) {
            damage += part.getAttackDamage();
        }
        return damage + (float) this.getAffinityScore(Affinity.EARTH) * 0.5F;
    }

    @Override
    public int calculateIntelligence() {
        int intelligence = 8;
        for (ItemConstructPart part : this.partsList.values()) {
            intelligence += part.getIntelligenceBonus();
        }
        return intelligence;
    }

    @Override
    public int calculatePerception() {
        int perception = 12 + this.getAffinityScore(Affinity.WATER) * 4;
        for (ItemConstructPart part : this.partsList.values()) {
            perception += part.getPerceptionDistanceBonus();
        }
        return perception;
    }

    @Override
    public float calculateKnockback() {
        float knockback = 0.0F;
        for (ItemConstructPart part : this.partsList.values()) {
            knockback += part.getKnockbackBonus();
        }
        return (float) ((double) knockback + 0.25 * (double) this.getAffinityScore(Affinity.WIND));
    }

    @Override
    public float calculateKnockbackResistance() {
        float kbResist = 0.0F;
        for (ItemConstructPart part : this.partsList.values()) {
            kbResist += part.getMaterial().getKnockbackResistance();
        }
        return MathUtils.clamp01(kbResist);
    }

    @Override
    public float calculateMana() {
        float mana = 0.0F;
        for (ItemConstructPart part : this.partsList.values()) {
            mana += (float) part.getManaCapacity();
        }
        return (float) ((double) mana + (double) mana * 0.25 * (double) this.getAffinityScore(Affinity.ARCANE));
    }

    @Override
    public int calculateFluidCapacity() {
        int cap = 0;
        for (ItemConstructPart part : this.partsList.values()) {
            cap += part.getFluidCapacity();
        }
        return (int) ((double) cap + (double) cap * 0.25 * (double) this.getAffinityScore(Affinity.WATER));
    }

    @Override
    public int calculateArmor() {
        int armor = this.getAffinityScore(Affinity.EARTH);
        for (ItemConstructPart part : this.partsList.values()) {
            armor += part.getArmor();
        }
        return armor;
    }

    @Override
    public int calculateToughness() {
        int toughness = this.getAffinityScore(Affinity.EARTH) / 2;
        for (ItemConstructPart part : this.partsList.values()) {
            toughness += part.getToughness();
        }
        return toughness;
    }

    @Override
    public float calculateRangedDamage() {
        float damage = 0.0F;
        for (ItemConstructPart part : this.partsList.values()) {
            damage += part.getRangedAttackDamage();
        }
        return damage + (float) this.getAffinityScore(Affinity.WIND) * 0.5F;
    }

    @Override
    public int calculateInventorySize() {
        int inventorySize = 0;
        for (ItemConstructPart part : this.partsList.values()) {
            inventorySize += part.getInventorySizeBonus();
        }
        return inventorySize;
    }

    @Override
    public int calculateInventoryStackLimit() {
        int limit = 0;
        for (ItemConstructPart part : this.partsList.values()) {
            limit += part.getBackpackCapacityBoost();
        }
        int enderAffinity = this.getAffinityScore(Affinity.ENDER);
        limit *= enderAffinity + 1;
        return Mth.clamp(limit, 1, 64);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return (ConstructCapability[]) this.enabledCapabilities.toArray(new ConstructCapability[0]);
    }

    @Override
    public boolean isCapabilityEnabled(ConstructCapability cap) {
        return this.enabledCapabilities.contains(cap);
    }

    @Override
    public boolean areCapabilitiesEnabled(ConstructCapability... caps) {
        for (ConstructCapability cap : caps) {
            if (!this.enabledCapabilities.contains(cap)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isAnyCapabilityEnabled(ConstructCapability... caps) {
        for (ConstructCapability cap : caps) {
            if (this.enabledCapabilities.contains(cap)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void resetParts() {
        this.partsList.clear();
        this.calculateCompositionAndCapabilities();
    }

    @Override
    public ItemStack setPart(ItemStack part) {
        return this.setPart(part, true);
    }

    @Override
    public ItemStack setPart(ItemStack part, boolean recalculate) {
        ItemConstructPart partItem = (ItemConstructPart) part.getItem();
        ItemStack output = ItemStack.EMPTY;
        if (this.partsList.containsKey(partItem.getSlot()) && this.partsList.get(partItem.getSlot()) != null) {
            output = new ItemStack((ItemLike) this.partsList.get(partItem.getSlot()));
        }
        this.partsList.put(partItem.getSlot(), partItem);
        if (output.getItem() instanceof ICanContainSpell) {
            ISpellDefinition recipe = (ISpellDefinition) this.castableSpells.getOrDefault(partItem.getSlot(), null);
            if (recipe != null && recipe.isValid()) {
                recipe.writeToNBT(output.getOrCreateTag());
            }
        }
        if (partItem instanceof ICanContainSpell) {
            SpellRecipe recipe = SpellRecipe.fromNBT(part.getTag());
            if (recipe.isValid()) {
                this.castableSpells.put(partItem.getSlot(), recipe);
            }
        }
        if (recalculate) {
            this.calculateCompositionAndCapabilities();
        }
        return output;
    }

    @Override
    public ItemStack removePart(ConstructSlot slot) {
        if (this.partsList.containsKey(slot)) {
            ISpellDefinition spell = (ISpellDefinition) this.castableSpells.remove(slot);
            ItemStack removed = new ItemStack((ItemLike) this.partsList.remove(slot));
            if (spell != null) {
                spell.writeToNBT(removed.getOrCreateTag());
            }
            return removed;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setHat(ItemStack stack) {
        this.hat = stack.copy();
    }

    @Override
    public void setBanner(ItemStack stack) {
        this.banner = stack.copy();
    }

    @Override
    public ItemStack getHat() {
        return this.hat;
    }

    @Override
    public ItemStack getBanner() {
        return this.banner;
    }

    @Override
    public Optional<ItemConstructPart> getPart(ConstructSlot slot) {
        return this.partsList.containsKey(slot) ? Optional.of((ItemConstructPart) this.partsList.get(slot)) : Optional.empty();
    }

    @Override
    public List<ConstructMaterial> getComposition() {
        return (List<ConstructMaterial>) this.composition.clone();
    }

    @Override
    public List<ItemConstructPart> getPartsForMaterial(ConstructMaterial material) {
        return (List<ItemConstructPart>) this.partsList.values().stream().filter(p -> p.getMaterial() == material).collect(Collectors.toList());
    }

    @Override
    public int getAffinityScore(Affinity aff) {
        return this.affinityData.containsKey(aff) ? (Integer) this.affinityData.get(aff.getShiftAffinity()) : 0;
    }

    @Override
    public void setAffinityScore(Affinity aff, int value) {
        this.affinityData.put(aff, Mth.clamp(value, 0, 8));
    }

    @Override
    public Affinity getRandomContainedAffinity() {
        Affinity[] contained = (Affinity[]) this.affinityData.keySet().toArray(new Affinity[0]);
        return contained.length == 0 ? Affinity.UNKNOWN : contained[(int) (Math.random() * (double) contained.length)];
    }

    public ConstructConstruction copy() {
        ConstructConstruction clone = new ConstructConstruction();
        for (ItemConstructPart part : this.partsList.values()) {
            clone.setPart(new ItemStack(part), false);
        }
        clone.castableSpells.clear();
        this.castableSpells.entrySet().forEach(e -> clone.castableSpells.put((ConstructSlot) e.getKey(), (ISpellDefinition) e.getValue()));
        clone.calculateCompositionAndCapabilities();
        clone.setHat(this.getHat());
        clone.setBanner(this.getBanner());
        return clone;
    }

    @Override
    public void ReadNBT(CompoundTag compound) {
        if (compound.contains("animated_construct_composition")) {
            this.partsList.clear();
            CompoundTag constructData = compound.getCompound("animated_construct_composition");
            for (ConstructSlot slot : ConstructSlot.values()) {
                if (constructData.contains(slot.toString())) {
                    ResourceLocation rLoc = new ResourceLocation(constructData.getString(slot.toString()));
                    if (!rLoc.getPath().isEmpty()) {
                        Item item = ForgeRegistries.ITEMS.getValue(rLoc);
                        if (item != null && item instanceof ItemConstructPart) {
                            this.partsList.put(((ItemConstructPart) item).getSlot(), (ItemConstructPart) item);
                        }
                    }
                }
            }
            if (constructData.contains("animated_construct_affinity")) {
                CompoundTag affinity = constructData.getCompound("animated_construct_affinity");
                for (Affinity aff : Affinity.values()) {
                    if (affinity.contains(aff.toString())) {
                        this.affinityData.put(aff, affinity.getInt(aff.toString()));
                    }
                }
            }
            if (constructData.contains("animated_construct_spells")) {
                ListTag spells = constructData.getList("animated_construct_spells", 10);
                this.castableSpells.clear();
                spells.forEach(tag -> {
                    if (((CompoundTag) tag).contains("slot")) {
                        SpellRecipe recipe = SpellRecipe.fromNBT((CompoundTag) tag);
                        ConstructSlot slotx = ConstructSlot.values()[((CompoundTag) tag).getInt("slot")];
                        if (recipe.isValid()) {
                            this.castableSpells.put(slotx, recipe);
                        }
                    }
                });
            }
            if (constructData.contains("animated_construct_hat")) {
                this.setHat(ItemStack.of(constructData.getCompound("animated_construct_hat")));
            }
            if (constructData.contains("animated_construct_banner")) {
                this.setBanner(ItemStack.of(constructData.getCompound("animated_construct_banner")));
            }
            this.calculateCompositionAndCapabilities();
        }
    }

    @Override
    public void WriteNBT(CompoundTag compound) {
        CompoundTag data = new CompoundTag();
        if (this.partsList != null) {
            this.partsList.forEach((k, v) -> data.putString(k.toString(), v == null ? "" : ForgeRegistries.ITEMS.getKey(v).toString()));
        }
        CompoundTag affinity = new CompoundTag();
        if (this.affinityData != null) {
            for (Affinity aff : this.affinityData.keySet()) {
                affinity.putInt(aff.toString(), (Integer) this.affinityData.get(aff));
            }
        }
        data.put("animated_construct_affinity", affinity);
        ListTag spells = new ListTag();
        this.castableSpells.entrySet().forEach(e -> {
            CompoundTag tag = new CompoundTag();
            ((ISpellDefinition) e.getValue()).writeToNBT(tag);
            tag.putInt("slot", ((ConstructSlot) e.getKey()).ordinal());
            spells.add(tag);
        });
        data.put("animated_construct_spells", spells);
        if (!this.hat.isEmpty()) {
            data.put("animated_construct_hat", this.hat.save(new CompoundTag()));
        }
        if (!this.banner.isEmpty()) {
            data.put("animated_construct_banner", this.banner.save(new CompoundTag()));
        }
        compound.put("animated_construct_composition", data);
    }

    @Nullable
    @Override
    public ConstructMaterial getLowestMaterialCooldownMultiplierForCapability(ConstructCapability capability) {
        List<ItemConstructPart> possibleParts = (List<ItemConstructPart>) this.partsList.values().stream().filter(px -> this.arrayContains(px.getEnabledCapabilities(), capability)).collect(Collectors.toList());
        ConstructMaterial highest = ConstructMaterial.UNKNOWN;
        for (ItemConstructPart p : possibleParts) {
            if (p.getMaterial().getCooldownMultiplierFor(capability) < highest.getCooldownMultiplierFor(capability)) {
                highest = p.getMaterial();
            }
        }
        return highest == ConstructMaterial.UNKNOWN ? null : highest;
    }

    private <T> boolean arrayContains(T[] arr, T search) {
        for (T elem : arr) {
            if (elem.equals(search)) {
                return true;
            }
        }
        return false;
    }

    public void copyFrom(ConstructConstruction construct) {
        for (ItemConstructPart part : construct.partsList.values()) {
            this.setPart(new ItemStack(part), false);
        }
        this.castableSpells.clear();
        construct.castableSpells.forEach((k, v) -> this.castableSpells.put(k, v));
        this.calculateCompositionAndCapabilities();
        this.setHat(construct.getHat());
        this.setBanner(construct.getBanner());
    }

    @Override
    public ISpellDefinition[] getCastableSpells() {
        return (ISpellDefinition[]) this.castableSpells.values().toArray(new ISpellDefinition[0]);
    }
}