package com.craisinlord.integrated_api.mixins.entities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.decoration.HangingEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = { HangingEntity.class }, priority = 1200)
public class HangingEntityMixin {

    @WrapOperation(method = { "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V" }, at = { @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V", remap = false) }, require = 0)
    private void integratedapi_lowerLoggingLevel(Logger instance, String s, Object o, Operation<Void> original) {
        instance.debug(s, o);
    }
}