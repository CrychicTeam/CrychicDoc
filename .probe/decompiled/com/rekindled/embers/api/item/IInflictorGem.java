package com.rekindled.embers.api.item;

import javax.annotation.Nullable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IInflictorGem {

    void attuneSource(ItemStack var1, @Nullable LivingEntity var2, DamageSource var3);

    @Nullable
    String getAttunedSource(ItemStack var1);

    float getDamageResistance(ItemStack var1, float var2);
}