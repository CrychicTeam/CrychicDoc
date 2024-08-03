package com.simibubi.create.foundation.mixin.accessor;

import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ AbstractProjectileDispenseBehavior.class })
public interface AbstractProjectileDispenseBehaviorAccessor {

    @Invoker("getProjectile")
    Projectile create$callGetProjectile(Level var1, Position var2, ItemStack var3);

    @Invoker("getUncertainty")
    float create$callGetUncertainty();

    @Invoker("getPower")
    float create$callGetPower();
}