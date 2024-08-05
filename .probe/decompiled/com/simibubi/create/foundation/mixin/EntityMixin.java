package com.simibubi.create.foundation.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { Entity.class }, priority = 900)
public class EntityMixin {

    @Inject(method = { "fireImmune()Z" }, at = { @At("RETURN") }, cancellable = true)
    public void create$onFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ()) {
            Entity self = (Entity) this;
            boolean immune = self.getPersistentData().getBoolean("CreateFireImmune");
            if (immune) {
                cir.setReturnValue(immune);
            }
        }
    }
}