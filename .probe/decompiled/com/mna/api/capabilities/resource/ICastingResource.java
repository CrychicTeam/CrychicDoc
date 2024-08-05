package com.mna.api.capabilities.resource;

import java.util.HashMap;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ICastingResource {

    ResourceLocation getRegistryName();

    SyncStatus getSyncStatus();

    void clearSyncStatus();

    float getAmount();

    boolean hasEnough(LivingEntity var1, float var2);

    boolean hasEnoughAbsolute(LivingEntity var1, float var2);

    void setAmount(float var1);

    void consume(LivingEntity var1, float var2);

    void restore(float var1);

    float getMaxAmount();

    float getMaxAmountBaseline();

    void setMaxAmount(float var1);

    void setMaxAmountByLevel(int var1);

    void addModifier(String var1, float var2);

    HashMap<String, Float> getModifiers();

    void removeModifier(String var1);

    void clearModifiers();

    int getRegenerationRate(LivingEntity var1);

    void setRegenerationRate(int var1);

    void addRegenerationModifier(String var1, float var2);

    HashMap<String, Float> getRegenerationModifiers();

    void removeRegenerationModifier(String var1);

    void clearRegenerationModifiers();

    float getRegenerationModifier(LivingEntity var1);

    default boolean hungerAffectsRegenRate() {
        return true;
    }

    default boolean artificialRestore() {
        return true;
    }

    default boolean canRechargeFrom(ItemStack batteryItem) {
        return true;
    }

    default void copyFrom(ICastingResource other) {
        if (other.getRegistryName() == this.getRegistryName()) {
            this.clearModifiers();
            other.getModifiers().forEach((k, v) -> this.addModifier(k, v));
            this.clearRegenerationModifiers();
            other.getRegenerationModifiers().forEach((k, v) -> this.addRegenerationModifier(k, v));
            this.setMaxAmount(other.getMaxAmount());
            this.setAmount(other.getAmount());
        }
    }

    default void writeNBT(CompoundTag nbt) {
        CompoundTag resourceNBT = new CompoundTag();
        resourceNBT.putFloat("amount", this.getAmount());
        resourceNBT.putFloat("maximum", this.getMaxAmountBaseline());
        ListTag maximumModifiers = new ListTag();
        for (Entry<String, Float> entry : this.getModifiers().entrySet()) {
            CompoundTag modifier = new CompoundTag();
            modifier.putString("key", (String) entry.getKey());
            modifier.putFloat("value", (Float) entry.getValue());
            maximumModifiers.add(modifier);
        }
        resourceNBT.put("maximum_modifiers", maximumModifiers);
        ListTag regenerationModifiers = new ListTag();
        for (Entry<String, Float> entry : this.getRegenerationModifiers().entrySet()) {
            CompoundTag modifier = new CompoundTag();
            modifier.putString("key", (String) entry.getKey());
            modifier.putFloat("value", (Float) entry.getValue());
            regenerationModifiers.add(modifier);
        }
        resourceNBT.put("regeneration_modifiers", regenerationModifiers);
        nbt.put(this.getRegistryName().toString(), resourceNBT);
    }

    default void readNBT(CompoundTag nbt) {
        if (nbt.contains(this.getRegistryName().toString())) {
            CompoundTag resourceNBT = nbt.getCompound(this.getRegistryName().toString());
            this.setAmount(resourceNBT.getFloat("amount"));
            this.setMaxAmount(resourceNBT.getFloat("maximum"));
            ListTag maximumModifiers = resourceNBT.getList("maximum_modifiers", 10);
            this.getModifiers().clear();
            maximumModifiers.forEach(inbt -> this.addModifier(((CompoundTag) inbt).getString("key"), ((CompoundTag) inbt).getFloat("value")));
            ListTag regenerationModifiers = resourceNBT.getList("regeneration_modifiers", 10);
            this.getRegenerationModifiers().clear();
            regenerationModifiers.forEach(inbt -> this.addRegenerationModifier(((CompoundTag) inbt).getString("key"), ((CompoundTag) inbt).getFloat("value")));
        }
    }

    void setNeedsSync();
}