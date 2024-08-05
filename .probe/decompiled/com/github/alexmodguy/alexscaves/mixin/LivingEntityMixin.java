package com.github.alexmodguy.alexscaves.mixin;

import com.github.alexmodguy.alexscaves.server.entity.util.DarknessIncarnateUserAccessor;
import com.github.alexmodguy.alexscaves.server.entity.util.EntityDropChanceAccessor;
import com.github.alexmodguy.alexscaves.server.entity.util.HeadRotationEntityAccessor;
import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import com.github.alexmodguy.alexscaves.server.entity.util.MagneticEntityAccessor;
import com.github.alexmodguy.alexscaves.server.entity.util.WatcherPossessionAccessor;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LivingEntity.class })
public abstract class LivingEntityMixin extends Entity implements HeadRotationEntityAccessor, WatcherPossessionAccessor, DarknessIncarnateUserAccessor, EntityDropChanceAccessor {

    @Shadow
    public float yHeadRotO;

    @Shadow
    @Final
    public WalkAnimationState walkAnimation;

    @Shadow
    public float yHeadRot;

    private float prevHeadYaw;

    private float prevHeadYaw0;

    private float prevHeadPitch;

    private float prevHeadPitch0;

    private boolean watcherPossessionFlag;

    private boolean slowFallingFlag;

    @Shadow
    @Override
    public abstract float getYHeadRot();

    @Shadow
    public abstract boolean hasEffect(MobEffect var1);

    @Shadow
    public abstract boolean addEffect(MobEffectInstance var1);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = { "Lnet/minecraft/world/entity/LivingEntity;calculateEntityAnimation(Z)V" }, remap = true, cancellable = true, at = { @At("HEAD") })
    public void ac_calculateEntityAnimation(boolean b, CallbackInfo ci) {
        if (MagnetUtil.isPulledByMagnets(this) && ((MagneticEntityAccessor) this).getMagneticAttachmentFace() != Direction.DOWN) {
            ci.cancel();
            float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
            float f2 = Math.min(f1 * 6.0F, 1.0F);
            this.walkAnimation.update(f2, 0.4F);
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/LivingEntity;tick()V" }, remap = true, at = { @At("TAIL") })
    public void ac_livingTick(CallbackInfo ci) {
        if (this.hasSlowFallingFlag()) {
            this.setSlowFallingFlag(false);
            this.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 80, 0, false, false, false));
        }
    }

    @Inject(method = { "Lnet/minecraft/world/entity/LivingEntity;increaseAirSupply(I)I" }, remap = true, cancellable = true, at = { @At("HEAD") })
    protected void ac_increaseAirSupply(int air, CallbackInfoReturnable<Integer> cir) {
        if (this.hasEffect(ACEffectRegistry.BUBBLED.get())) {
            cir.setReturnValue(air);
        }
    }

    @Override
    public void setMagnetHeadRotation() {
        this.prevHeadYaw = this.getYHeadRot();
        this.prevHeadYaw0 = this.yHeadRotO;
        this.prevHeadPitch = this.m_146909_();
        this.prevHeadPitch0 = this.f_19860_;
        MagnetUtil.rotateHead((LivingEntity) this);
    }

    @Override
    public void resetMagnetHeadRotation() {
        this.yHeadRot = this.prevHeadYaw;
        this.yHeadRotO = this.prevHeadYaw0;
        this.m_146926_(this.prevHeadPitch);
        this.f_19860_ = this.prevHeadPitch0;
    }

    @Override
    public void setPossessedByWatcher(boolean possessedByWatcher) {
        this.watcherPossessionFlag = possessedByWatcher;
    }

    @Override
    public boolean isPossessedByWatcher() {
        return this.watcherPossessionFlag;
    }

    @Override
    public void setSlowFallingFlag(boolean slowFallingFlag) {
        this.slowFallingFlag = slowFallingFlag;
    }

    @Override
    public boolean hasSlowFallingFlag() {
        return this.slowFallingFlag;
    }
}