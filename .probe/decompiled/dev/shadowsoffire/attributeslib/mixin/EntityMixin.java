package dev.shadowsoffire.attributeslib.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ Entity.class })
public abstract class EntityMixin {

    @ModifyVariable(at = @At("HEAD"), method = { "checkFallDamage(DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V" }, argsOnly = true)
    public double apoth_checkFallDamageWithGravity(double yMotion) {
        if (yMotion < 0.0 && this instanceof LivingEntity le) {
            double gravity = le.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get());
            yMotion *= gravity / 0.08;
            if (gravity <= 0.01) {
                yMotion = 0.0;
            }
        }
        return yMotion;
    }
}