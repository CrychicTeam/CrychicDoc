package forge.me.thosea.badoptimizations.mixin.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.BrainDebugRenderer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BrainDebugRenderer.class })
public class MixinVillageDebugRenderer {

    @Shadow
    @Final
    private Map<BlockPos, BrainDebugRenderer.PoiInfo> pois;

    @Shadow
    @Final
    private Map<UUID, BrainDebugRenderer.BrainDump> brainDumpsPerEntity;

    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void onRender(PoseStack matrices, MultiBufferSource vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (this.pois.isEmpty() && this.brainDumpsPerEntity.isEmpty()) {
            ci.cancel();
        }
    }
}