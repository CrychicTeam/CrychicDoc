package forge.me.thosea.badoptimizations.mixin.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.GameEventListenerRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GameEventListenerRenderer.class })
public class MixinGameEventDebugRenderer {

    @Shadow
    @Final
    private List<?> trackedGameEvents;

    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void onInject(PoseStack matrices, MultiBufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (this.trackedGameEvents.isEmpty()) {
            ci.cancel();
        }
    }
}