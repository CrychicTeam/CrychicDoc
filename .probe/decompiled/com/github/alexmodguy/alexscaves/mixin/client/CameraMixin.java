package com.github.alexmodguy.alexscaves.mixin.client;

import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Camera.class })
public abstract class CameraMixin {

    @Shadow
    private float yRot;

    @Shadow
    private float xRot;

    @Shadow
    private boolean initialized;

    @Shadow
    public abstract void move(double var1, double var3, double var5);

    @Shadow
    protected abstract void setPosition(Vec3 var1);

    @Shadow
    protected abstract double getMaxZoom(double var1);

    @Shadow
    protected abstract void setRotation(float var1, float var2);

    @Inject(method = { "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V" }, remap = true, at = { @At("TAIL") })
    public void ac_onSyncedDataUpdated(BlockGetter level, Entity entity, boolean detatched, boolean mirrored, float partialTicks, CallbackInfo ci) {
        Direction dir = MagnetUtil.getEntityMagneticDirection(entity);
        if (dir != Direction.DOWN && dir != Direction.UP) {
            this.setPosition(MagnetUtil.getEyePositionForAttachment(entity, dir, partialTicks));
            if (detatched) {
                if (mirrored) {
                    this.setRotation(this.yRot + 180.0F, -this.xRot);
                }
                this.move(-this.getMaxZoom(4.0), 0.0, 0.0);
            }
        }
    }

    @Inject(method = { "Lnet/minecraft/client/Camera;getFluidInCamera()Lnet/minecraft/world/level/material/FogType;" }, remap = true, cancellable = true, at = { @At("HEAD") })
    public void ac_getFluidInCamera(CallbackInfoReturnable<FogType> cir) {
        if (this.initialized && Minecraft.getInstance().player != null && Minecraft.getInstance().player.m_21023_(ACEffectRegistry.BUBBLED.get()) && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
            cir.setReturnValue(FogType.WATER);
        }
    }
}