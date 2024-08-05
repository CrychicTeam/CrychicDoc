package dev.xkmc.l2library.mixin;

import dev.xkmc.l2library.util.raytrace.EntityTarget;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Entity.class })
public abstract class EntityMixin {

    @Inject(at = { @At("HEAD") }, method = { "isCurrentlyGlowing" }, cancellable = true)
    public void l2library_overrideClientGlow_isCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
        for (EntityTarget target : EntityTarget.LIST) {
            if (target.target == this) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}