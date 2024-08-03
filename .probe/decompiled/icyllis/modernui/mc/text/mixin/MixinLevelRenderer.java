package icyllis.modernui.mc.text.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import icyllis.modernui.mc.text.TextLayoutEngine;
import icyllis.modernui.mc.text.TextRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LevelRenderer.class })
public class MixinLevelRenderer {

    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Inject(method = { "renderLevel" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OutlineBufferSource;endOutlineBatch()V") })
    private void endTextBatch(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        if (TextLayoutEngine.sUseTextShadersInWorld) {
            TextRenderType firstSDFFillType = TextRenderType.getFirstSDFFillType();
            TextRenderType firstSDFStrokeType = TextRenderType.getFirstSDFStrokeType();
            if (firstSDFFillType != null) {
                this.renderBuffers.bufferSource().endBatch(firstSDFFillType);
            }
            if (firstSDFStrokeType != null) {
                this.renderBuffers.bufferSource().endBatch(firstSDFStrokeType);
            }
        }
    }
}