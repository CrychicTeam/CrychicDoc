package com.simibubi.create.foundation.mixin.accessor;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ LivingEntity.class })
public interface LivingEntityAccessor {

    @Invoker("spawnItemParticles")
    void create$callSpawnItemParticles(ItemStack var1, int var2);
}