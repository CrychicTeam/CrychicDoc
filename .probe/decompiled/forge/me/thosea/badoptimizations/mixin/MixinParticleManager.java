package forge.me.thosea.badoptimizations.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import java.util.Queue;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ParticleEngine.class })
public class MixinParticleManager {

    @Shadow
    @Final
    private Map<ParticleRenderType, Queue<Particle>> particles;

    @Inject(method = { "renderParticles" }, at = { @At("HEAD") }, cancellable = true)
    private void onRender(PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, LightTexture lightmapTextureManager, Camera camera, float tickDelta, CallbackInfo ci) {
        if (this.particles.isEmpty()) {
            ci.cancel();
        }
    }
}