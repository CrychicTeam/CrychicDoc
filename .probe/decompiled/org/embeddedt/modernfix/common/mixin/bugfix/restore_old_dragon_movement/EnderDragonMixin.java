package org.embeddedt.modernfix.common.mixin.bugfix.restore_old_dragon_movement;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin({ EnderDragon.class })
public class EnderDragonMixin {

    @ModifyArg(method = { "aiStep" }, slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;getFlyTargetLocation()Lnet/minecraft/world/phys/Vec3;")), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;", ordinal = 0), index = 1)
    private double fixVerticalVelocityScale(double y) {
        return y * 10.0;
    }
}