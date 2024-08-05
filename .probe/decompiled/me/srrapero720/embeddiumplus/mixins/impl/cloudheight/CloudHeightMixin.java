package me.srrapero720.embeddiumplus.mixins.impl.cloudheight;

import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ DimensionSpecialEffects.class })
public class CloudHeightMixin {

    @Shadow
    @Final
    private float cloudLevel;

    @Inject(method = { "getCloudHeight" }, at = { @At("HEAD") }, cancellable = true)
    private void inject$cloudHeight(CallbackInfoReturnable<Float> cir) {
        if (this.cloudLevel == 192.0F) {
            cir.setReturnValue((float) EmbyConfig.cloudsHeightCache);
        }
    }
}