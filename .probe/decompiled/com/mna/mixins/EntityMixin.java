package com.mna.mixins;

import com.mna.effects.EffectInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Entity.class })
public abstract class EntityMixin {

    @Shadow
    public Level level;

    @Inject(at = { @At("RETURN") }, method = { "canEnterPose" }, cancellable = true)
    public void mna$canEnterPose(Pose pose, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) this;
        if (entity instanceof Player) {
            LivingEntity livingEntity = (LivingEntity) entity;
            MobEffectInstance shrinkEffect = livingEntity.getEffect(EffectInit.REDUCE.get());
            if (shrinkEffect != null) {
                float scale = 1.0F - 0.1F * (float) (shrinkEffect.getAmplifier() + 1);
                EntityDimensions entitysize = this.getDimensions(pose);
                entitysize = entitysize.scale(scale);
                float f = entitysize.width / 2.0F;
                Vec3 vector3d = new Vec3(this.getX() - (double) f, this.getY(), this.getZ() - (double) f);
                Vec3 vector3d1 = new Vec3(this.getX() + (double) f, this.getY() + (double) entitysize.height, this.getZ() + (double) f);
                AABB box = new AABB(vector3d, vector3d1);
                cir.setReturnValue(this.level.m_45756_(livingEntity, box.deflate(1.0E-7)));
            }
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "playSound" }, cancellable = true)
    public void mna$playSound(SoundEvent pSound, float pVolume, float pPitch, CallbackInfo cbi) {
        Entity entity = (Entity) this;
        if (entity instanceof LivingEntity) {
            MobEffectInstance efct = ((LivingEntity) entity).getEffect(EffectInit.REDUCE.get());
            boolean playAndCancel = false;
            if (efct != null) {
                float pitchChange = 0.1F * (float) efct.getAmplifier();
                pPitch += pitchChange;
                playAndCancel = true;
            }
            efct = ((LivingEntity) entity).getEffect(EffectInit.ENLARGE.get());
            if (efct != null) {
                float pitchChange = 0.05F * (float) efct.getAmplifier();
                pPitch -= pitchChange;
                playAndCancel = true;
            }
            if (playAndCancel) {
                if (!this.isSilent()) {
                    this.level.playSound((Player) null, this.getX(), this.getY(), this.getZ(), pSound, this.getSoundSource(), pVolume, pPitch);
                }
                cbi.cancel();
            }
        }
    }

    @Shadow
    public abstract EntityDimensions getDimensions(Pose var1);

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract boolean isSilent();

    @Shadow
    public abstract SoundSource getSoundSource();
}