package fabric.me.thosea.badoptimizations.mixin.tick;

import net.minecraft.class_1156;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ class_1156.class })
public final class MixinTutorial {

    @Shadow
    @Final
    private class_310 field_5645;

    @Inject(method = { "tick()V" }, at = { @At("HEAD") }, cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (!this.field_5645.method_1530()) {
            ci.cancel();
        }
    }
}