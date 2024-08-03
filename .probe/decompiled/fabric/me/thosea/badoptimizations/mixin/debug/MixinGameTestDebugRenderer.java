package fabric.me.thosea.badoptimizations.mixin.debug;

import java.util.Map;
import net.minecraft.class_2338;
import net.minecraft.class_4503;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ class_4503.class })
public class MixinGameTestDebugRenderer {

    @Shadow
    @Final
    private Map<class_2338, ?> field_20520;

    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void onRender(class_4587 matrices, class_4597 vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (this.field_20520.isEmpty()) {
            ci.cancel();
        }
    }
}