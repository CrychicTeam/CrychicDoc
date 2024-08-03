package org.violetmoon.quark.mixin.mixins;

import net.minecraft.world.entity.item.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.quark.content.experimental.module.GameNerfsModule;

@Mixin({ FallingBlockEntity.class })
public class FallingBlockEntityMixin {

    @Inject(method = { "tick" }, at = { @At("HEAD"), @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V", shift = Shift.AFTER) }, cancellable = true)
    public void stopTickingIfRemoved(CallbackInfo ci) {
        if (GameNerfsModule.stopFallingBlocksDuping() && ((FallingBlockEntity) this).m_213877_()) {
            ci.cancel();
        }
    }
}