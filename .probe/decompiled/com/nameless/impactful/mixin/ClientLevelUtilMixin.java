package com.nameless.impactful.mixin;

import com.nameless.impactful.client.CameraEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.utils.LevelUtil;

@Mixin({ LevelUtil.class })
public class ClientLevelUtilMixin {

    @Inject(method = { "circleSlamFracture(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;DZZZ)Z" }, at = { @At("TAIL") }, remap = false)
    private static void onCasterCircleSlamFracture(LivingEntity caster, Level level, Vec3 center, double radius, boolean noSound, boolean noParticle, boolean hurtEntities, CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null && localPlayer.m_20182_().distanceTo(center) <= radius + 3.0) {
            int t = (int) Math.max(20.0, Math.min(15.0 * radius, 120.0));
            float s = (float) Math.min(2.0 * radius, 12.0);
            CameraEngine.getInstance().shakeCamera(t, s);
        }
    }
}