package fabric.me.thosea.badoptimizations.mixin;

import java.util.Map;
import java.util.Queue;
import net.minecraft.class_3999;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_702;
import net.minecraft.class_703;
import net.minecraft.class_765;
import net.minecraft.class_4597.class_4598;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ class_702.class })
public class MixinParticleManager {

    @Shadow
    @Final
    private Map<class_3999, Queue<class_703>> field_3830;

    @Inject(method = { "renderParticles" }, at = { @At("HEAD") }, cancellable = true)
    private void onRender(class_4587 matrices, class_4598 vertexConsumers, class_765 lightmapTextureManager, class_4184 camera, float tickDelta, CallbackInfo ci) {
        if (this.field_3830.isEmpty()) {
            ci.cancel();
        }
    }
}