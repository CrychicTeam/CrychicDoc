package forge.me.thosea.badoptimizations.mixin.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.BeeDebugRenderer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BeeDebugRenderer.class })
public class MixinBeeDebugRenderer {

    @Shadow
    @Final
    private Map<BlockPos, ?> hives;

    @Shadow
    @Final
    private Map<UUID, BeeDebugRenderer.BeeInfo> beeInfosPerEntity;

    @Inject(method = { "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;DDD)V" }, at = { @At("HEAD") }, cancellable = true)
    private void onRender(PoseStack matrices, MultiBufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (this.hives.isEmpty() && this.beeInfosPerEntity.isEmpty()) {
            ci.cancel();
        }
    }
}