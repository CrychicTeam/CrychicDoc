package com.nameless.impactful.mixin;

import com.mojang.datafixers.util.Pair;
import com.nameless.impactful.capabilities.HitStopCap;
import com.nameless.impactful.capabilities.ImpactfulCapabilities;
import com.nameless.impactful.config.CommonConfig;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

@Mixin({ AttackAnimation.class })
public class AttackAnimationMixin {

    @Inject(method = { "getPlaySpeed(Lyesman/epicfight/world/capabilities/entitypatch/LivingEntityPatch;)F" }, at = { @At("RETURN") }, cancellable = true, remap = false)
    public void getPlaySpeed(LivingEntityPatch<?> entitypatch, CallbackInfoReturnable<Float> cir) {
        if (entitypatch instanceof PlayerPatch<?> playerPatch) {
            Player player = playerPatch.getOriginal();
            HitStopCap hitStopCap = (HitStopCap) player.getCapability(ImpactfulCapabilities.INSTANCE).orElse(null);
            if (hitStopCap != null && player.m_20088_().get(HitStopCap.HIT_STOP)) {
                DynamicAnimation animation = playerPatch.<Animator>getAnimator().getPlayerFor(null).getAnimation();
                WeaponCategory category = playerPatch.getAdvancedHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory();
                float k;
                if (animation instanceof AttackAnimation && CommonConfig.hit_stop_by_animation.containsKey(animation) && CommonConfig.camera_shake_by_animation.containsKey(animation)) {
                    k = (Float) ((Pair) CommonConfig.hit_stop_by_animation.getOrDefault(animation, Pair.of(2, 0.5F))).getSecond();
                } else {
                    k = (Float) ((Pair) CommonConfig.hit_stop_by_weapon_categories.getOrDefault(category, Pair.of(2, 0.5F))).getSecond();
                }
                cir.setReturnValue(cir.getReturnValueF() * Math.min(k, 1.0F));
            }
        }
    }
}