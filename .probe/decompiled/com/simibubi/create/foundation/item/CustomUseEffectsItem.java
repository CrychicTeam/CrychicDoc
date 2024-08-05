package com.simibubi.create.foundation.item;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface CustomUseEffectsItem {

    default Boolean shouldTriggerUseEffects(ItemStack stack, LivingEntity entity) {
        return null;
    }

    boolean triggerUseEffects(ItemStack var1, LivingEntity var2, int var3, RandomSource var4);
}