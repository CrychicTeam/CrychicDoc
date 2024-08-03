package dev.xkmc.l2complements.mixin;

import dev.xkmc.l2complements.content.feature.EntityFeature;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LocalPlayer.class })
public class LocalPlayerMixin {

    @Inject(at = { @At("TAIL") }, method = { "hurtTo" })
    public void l2complements_hurtTo_stableBody(float v, CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer) this;
        if (EntityFeature.STABLE_BODY.test(self)) {
            self.f_20916_ = 0;
        }
    }

    @Inject(at = { @At("TAIL") }, method = { "handleEntityEvent" })
    public void l2complements_handleEntityEvent_stableBody(byte pId, CallbackInfo ci) {
        LocalPlayer self = (LocalPlayer) this;
        if (EntityFeature.STABLE_BODY.test(self)) {
            self.f_20916_ = 0;
        }
    }
}