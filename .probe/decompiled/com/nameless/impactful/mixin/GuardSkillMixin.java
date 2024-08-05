package com.nameless.impactful.mixin;

import com.mojang.datafixers.util.Pair;
import com.nameless.impactful.config.CommonConfig;
import com.nameless.impactful.network.CameraShake;
import com.nameless.impactful.network.NetWorkManger;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mixin({ GuardSkill.class })
public class GuardSkillMixin {

    @Inject(method = { "getGuardMotion(Lyesman/epicfight/world/capabilities/entitypatch/player/PlayerPatch;Lyesman/epicfight/world/capabilities/item/CapabilityItem;Lyesman/epicfight/skill/guard/GuardSkill$BlockType;)Lyesman/epicfight/api/animation/types/StaticAnimation;" }, at = { @At("HEAD") }, remap = false)
    protected void oGetGuradMotion(PlayerPatch<?> playerpatch, CapabilityItem itemCapability, GuardSkill.BlockType blockType, CallbackInfoReturnable<StaticAnimation> cir) {
        Pair<Integer, Float> k = switch(blockType) {
            case GUARD_BREAK ->
                Pair.of(CommonConfig.GUARDBREAK_CAMERASHAKE_TIME.get(), CommonConfig.GUARDBREAK_CAMERASHAKE_STRENGTH.get().floatValue());
            case GUARD ->
                Pair.of(CommonConfig.GUARD_CAMERASHAKE_TIME.get(), CommonConfig.GUARD_CAMERASHAKE_STRENGTH.get().floatValue());
            case ADVANCED_GUARD ->
                Pair.of(CommonConfig.ADVANCEDGUARD_CAMERASHAKE_TIME.get(), CommonConfig.ADVANCEDGUARD_CAMERASHAKE_STRENGTH.get().floatValue());
            default ->
                Pair.of(10, 1.0F);
        };
        NetWorkManger.sendToPlayer(new CameraShake((Integer) k.getFirst(), (Float) k.getSecond(), 1.5F), (ServerPlayer) playerpatch.getOriginal());
    }
}