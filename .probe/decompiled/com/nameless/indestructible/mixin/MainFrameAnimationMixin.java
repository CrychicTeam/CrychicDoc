package com.nameless.indestructible.mixin;

import com.nameless.indestructible.api.animation.types.AnimationEvent;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.MainFrameAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin({ MainFrameAnimation.class })
public class MainFrameAnimationMixin {

    @Inject(method = { "begin(Lyesman/epicfight/world/capabilities/entitypatch/LivingEntityPatch;)V" }, at = { @At("TAIL") }, remap = false)
    public void onBegin(LivingEntityPatch<?> entitypatch, CallbackInfo ci) {
        if (entitypatch instanceof AdvancedCustomHumanoidMobPatch<?> advancedCustomHumanoidMobPatch) {
            advancedCustomHumanoidMobPatch.resetActionTick();
        }
    }

    @Inject(method = { "tick(Lyesman/epicfight/world/capabilities/entitypatch/LivingEntityPatch;)V" }, at = { @At("TAIL") }, remap = false)
    public void onTick(LivingEntityPatch<?> entitypatch, CallbackInfo ci) {
        if (!entitypatch.isLogicalClient() && entitypatch instanceof AdvancedCustomHumanoidMobPatch<?> advancedCustomHumanoidMobPatch && advancedCustomHumanoidMobPatch.hasTimeEvent()) {
            AnimationPlayer player = entitypatch.<Animator>getAnimator().getPlayerFor((DynamicAnimation) this);
            if (player != null) {
                float prevElapsed = player.getPrevElapsedTime();
                float elapsed = player.getElapsedTime();
                for (AnimationEvent.TimeStampedEvent event : advancedCustomHumanoidMobPatch.getTimeEventList()) {
                    event.testAndExecute(entitypatch, prevElapsed, elapsed);
                    if (!entitypatch.getOriginal().isAlive()) {
                        break;
                    }
                }
            }
        }
    }
}