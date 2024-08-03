package com.rekindled.embers.api.item;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IInflictorGemHolder {

    int getGemSlots(ItemStack var1);

    boolean canAttachGem(ItemStack var1, ItemStack var2);

    void attachGem(ItemStack var1, ItemStack var2, int var3);

    ItemStack detachGem(ItemStack var1, int var2);

    void clearGems(ItemStack var1);

    default int getAttachedGemCount(ItemStack holder) {
        int amt = 0;
        for (ItemStack stack : this.getAttachedGems(holder)) {
            if (!stack.isEmpty()) {
                amt++;
            }
        }
        return amt;
    }

    ItemStack[] getAttachedGems(ItemStack var1);

    float getTotalDamageResistance(LivingEntity var1, DamageSource var2, ItemStack var3);
}