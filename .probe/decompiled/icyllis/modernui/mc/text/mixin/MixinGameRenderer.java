package icyllis.modernui.mc.text.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import icyllis.modernui.mc.text.TextLayoutEngine;
import icyllis.modernui.mc.text.TextRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GameRenderer.class })
public class MixinGameRenderer {

    @Inject(method = { "preloadUiShader" }, at = { @At("TAIL") })
    private void onPreloadUiShader(ResourceProvider vanillaProvider, CallbackInfo ci) {
        TextRenderType.preloadShaders();
    }

    @Inject(method = { "renderLevel" }, at = { @At("HEAD") })
    private void renderLevelStart(float partialTick, long frameTimeNanos, PoseStack pStack, CallbackInfo ci) {
        TextLayoutEngine.sCurrentInWorldRendering = true;
    }

    @Inject(method = { "renderLevel" }, at = { @At("TAIL") })
    private void renderLevelEnd(float partialTick, long frameTimeNanos, PoseStack pStack, CallbackInfo ci) {
        TextLayoutEngine.sCurrentInWorldRendering = false;
    }
}