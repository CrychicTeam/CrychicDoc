package com.nameless.indestructible.mixin;

import com.nameless.indestructible.client.UIConfig;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.gui.HealthBarIndicator;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin({ HealthBarIndicator.class })
public class HealthBarIndicatorMixin {

    @Inject(method = { "shouldDraw(Lnet/minecraft/world/entity/LivingEntity;Lyesman/epicfight/world/capabilities/entitypatch/LivingEntityPatch;Lyesman/epicfight/client/world/capabilites/entitypatch/player/LocalPlayerPatch;)Z" }, at = { @At("RETURN") }, cancellable = true, remap = false)
    public void shouldDraw(LivingEntity entityIn, LivingEntityPatch<?> entitypatch, LocalPlayerPatch playerpatch, CallbackInfoReturnable<Boolean> cir) {
        if (UIConfig.REPLACE_UI.get()) {
            cir.setReturnValue(false);
        }
    }
}