package com.cupboard.mixin;

import com.cupboard.Cupboard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ Entity.class })
public abstract class EntityLoadMixin {

    @Shadow
    private float xRot;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract float getYRot();

    @Shadow
    public abstract float getXRot();

    @Shadow
    public abstract void setPosRaw(double var1, double var3, double var5);

    @Shadow
    public abstract void setXRot(float var1);

    @Shadow
    public abstract void setYRot(float var1);

    @Inject(method = { "load" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setYBodyRot(F)V", shift = Shift.AFTER) })
    private void avoidLoadCrash(CompoundTag compoundTag, CallbackInfo ci) {
        if ((!Double.isFinite(this.getX()) || !Double.isFinite(this.getY()) || !Double.isFinite(this.getZ())) && Cupboard.config.getCommonConfig().skipErrorOnEntityLoad) {
            Cupboard.LOGGER.warn("Prevented crash for entity loaded with invalid position, defaulting to 0 70 0. Nbt:" + compoundTag.toString());
            this.setPosRaw(0.0, 70.0, 0.0);
        }
        if (!Double.isFinite((double) this.getYRot()) || !Double.isFinite((double) this.getXRot())) {
            Cupboard.LOGGER.warn("Prevented crash for entity loaded with invalid rotations, defaulting to 0 0. Nbt:" + compoundTag.toString());
            this.setXRot(0.0F);
            this.setYRot(0.0F);
        }
    }

    @Inject(method = { "load" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/CrashReport;forThrowable(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;") }, cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void skipLoadingErroringEntity(CompoundTag compoundTag, CallbackInfo ci, Throwable throwable) {
        if (Cupboard.config.getCommonConfig().skipErrorOnEntityLoad) {
            Cupboard.LOGGER.warn("Prevented crash for entity load. Nbt:" + compoundTag.toString(), throwable);
            ci.cancel();
        }
    }
}