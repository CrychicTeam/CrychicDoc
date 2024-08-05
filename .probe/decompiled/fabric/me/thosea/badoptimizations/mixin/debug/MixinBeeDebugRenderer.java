package fabric.me.thosea.badoptimizations.mixin.debug;

import java.util.Map;
import java.util.UUID;
import net.minecraft.class_2338;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4703;
import net.minecraft.class_4703.class_5243;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ class_4703.class })
public class MixinBeeDebugRenderer {

    @Shadow
    @Final
    private Map<class_2338, ?> field_21533;

    @Shadow
    @Final
    private Map<UUID, class_5243> field_21534;

    @Inject(method = { "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;DDD)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onRender(class_4587 matrices, class_4597 vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (this.field_21533.isEmpty() && this.field_21534.isEmpty()) {
            ci.cancel();
        }
    }
}