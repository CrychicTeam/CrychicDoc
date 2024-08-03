package dev.shadowsoffire.attributeslib.mixin;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ NearestAttackableTargetGoal.class })
public abstract class NearestAttackableTargetGoalMixin extends TargetGoal {

    @Nullable
    Predicate<LivingEntity> ctorTargetPredicate;

    @Shadow
    TargetingConditions targetConditions;

    public NearestAttackableTargetGoalMixin(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
    }

    @Inject(method = { "<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;IZZLjava/util/function/Predicate;)V" }, at = { @At("TAIL") })
    private void apoth_cachePredicate(Mob pMob, Class<?> pTargetType, int pRandomInterval, boolean pMustSee, boolean pMustReach, @Nullable Predicate<LivingEntity> pTargetPredicate, CallbackInfo ci) {
        this.ctorTargetPredicate = pTargetPredicate;
    }

    @Inject(method = { "findTarget()V" }, at = { @At("HEAD") })
    private void apoth_updateFollowRange(CallbackInfo ci) {
        this.targetConditions.range(this.m_7623_());
    }
}