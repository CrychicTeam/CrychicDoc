package fabric.me.thosea.badoptimizations.mixin.debug;

import java.util.List;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_5739;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ class_5739.class })
public class MixinGameEventDebugRenderer {

    @Shadow
    @Final
    private List<?> field_28256;

    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void onInject(class_4587 matrices, class_4597 vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (this.field_28256.isEmpty()) {
            ci.cancel();
        }
    }
}