package net.raphimc.immediatelyfast.injection.mixins.sign_text_buffering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.phys.Vec3;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.sign_text_buffering.NoSetTextAnglesMatrixStack;
import net.raphimc.immediatelyfast.feature.sign_text_buffering.SignAtlasFramebuffer;
import net.raphimc.immediatelyfast.injection.interfaces.ISignText;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ SignRenderer.class })
public abstract class MixinSignBlockEntityRenderer {

    @Shadow
    @Final
    private Font font;

    @Shadow
    abstract void renderSignText(BlockPos var1, SignText var2, PoseStack var3, MultiBufferSource var4, int var5, int var6, int var7, boolean var8);

    @Shadow
    protected abstract void translateSignText(PoseStack var1, boolean var2, Vec3 var3);

    @Shadow
    abstract Vec3 getTextOffset();

    @Inject(method = { "renderText" }, at = { @At("HEAD") }, cancellable = true)
    private void renderBufferedSignText(BlockPos pos, SignText signText, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int lineHeight, int lineWidth, boolean front, CallbackInfo ci) {
        if (!(matrices instanceof NoSetTextAnglesMatrixStack)) {
            ISignText iSignText = (ISignText) signText;
            if (iSignText.immediatelyFast$shouldCache()) {
                SignAtlasFramebuffer.Slot slot = (SignAtlasFramebuffer.Slot) ImmediatelyFast.signTextCache.slotCache.getIfPresent(signText);
                if (slot == null) {
                    int width = this.immediatelyFast$getTextWidth(signText, lineWidth);
                    int height = 4 * lineHeight;
                    if (width <= 0 || height <= 0) {
                        iSignText.immediatelyFast$setShouldCache(false);
                        return;
                    }
                    int padding = signText.hasGlowingText() ? 2 : 0;
                    slot = ImmediatelyFast.signTextCache.signAtlasFramebuffer.findSlot(width + padding, height + padding);
                    if (slot == null) {
                        ImmediatelyFast.LOGGER.warn("Failed to find a free slot for sign text (" + ImmediatelyFast.signTextCache.slotCache.size() + " sign texts in atlas). Falling back to immediate mode rendering.");
                        iSignText.immediatelyFast$setShouldCache(false);
                        return;
                    }
                    Matrix4f projectionMatrix = new Matrix4f().setOrtho(0.0F, 4096.0F, 4096.0F, 0.0F, 1000.0F, 21000.0F);
                    RenderSystem.backupProjectionMatrix();
                    RenderSystem.setProjectionMatrix(projectionMatrix, VertexSorting.ORTHOGRAPHIC_Z);
                    PoseStack modelViewMatrix = RenderSystem.getModelViewStack();
                    modelViewMatrix.pushPose();
                    modelViewMatrix.setIdentity();
                    modelViewMatrix.translate(0.0F, 0.0F, -11000.0F);
                    RenderSystem.applyModelViewMatrix();
                    float fogStart = RenderSystem.getShaderFogStart();
                    FogRenderer.setupNoFog();
                    ImmediatelyFast.signTextCache.signAtlasFramebuffer.m_83947_(true);
                    MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                    PoseStack matrixStack = new NoSetTextAnglesMatrixStack();
                    matrixStack.translate((float) slot.x, (float) slot.y, 0.0F);
                    matrixStack.translate((float) slot.width / 2.0F, (float) slot.height / 2.0F, 0.0F);
                    this.renderSignText(Minecraft.getInstance().cameraEntity.blockPosition(), signText, matrixStack, immediate, light, lineHeight, lineWidth, front);
                    immediate.endBatch();
                    Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
                    RenderSystem.setShaderFogStart(fogStart);
                    modelViewMatrix.popPose();
                    RenderSystem.applyModelViewMatrix();
                    RenderSystem.restoreProjectionMatrix();
                    ImmediatelyFast.signTextCache.slotCache.put(signText, slot);
                }
                float u1 = (float) slot.x / 4096.0F;
                float u2 = ((float) slot.x + (float) slot.width) / 4096.0F;
                float v1 = 1.0F - (float) slot.y / 4096.0F;
                float v2 = 1.0F - ((float) slot.y + (float) slot.height) / 4096.0F;
                if (signText.hasGlowingText()) {
                    light = 15728880;
                }
                matrices.pushPose();
                this.translateSignText(matrices, front, this.getTextOffset());
                matrices.translate((float) (-slot.width) / 2.0F, (float) (-slot.height) / 2.0F, 0.0F);
                Matrix4f matrix4f = matrices.last().pose();
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(ImmediatelyFast.signTextCache.renderLayer);
                vertexConsumer.vertex(matrix4f, 0.0F, (float) slot.height, 0.0F).color(255, 255, 255, 255).uv(u1, v2).uv2(light).endVertex();
                vertexConsumer.vertex(matrix4f, (float) slot.width, (float) slot.height, 0.0F).color(255, 255, 255, 255).uv(u2, v2).uv2(light).endVertex();
                vertexConsumer.vertex(matrix4f, (float) slot.width, 0.0F, 0.0F).color(255, 255, 255, 255).uv(u2, v1).uv2(light).endVertex();
                vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 255).uv(u1, v1).uv2(light).endVertex();
                matrices.popPose();
                ci.cancel();
            }
        }
    }

    @Redirect(method = { "renderText" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/SignBlockEntityRenderer;setTextAngles(Lnet/minecraft/client/util/math/MatrixStack;ZLnet/minecraft/util/math/Vec3d;)V"))
    private void dontSetTextAngles(SignRenderer instance, PoseStack matrices, boolean front, Vec3 translation) {
        if (!(matrices instanceof NoSetTextAnglesMatrixStack)) {
            this.translateSignText(matrices, front, translation);
        }
    }

    @Unique
    private int immediatelyFast$getTextWidth(SignText signText, int lineWidth) {
        FormattedCharSequence[] orderedTexts = signText.getRenderMessages(Minecraft.getInstance().isTextFilteringEnabled(), text -> {
            List<FormattedCharSequence> list = this.font.split(text, lineWidth);
            return list.isEmpty() ? FormattedCharSequence.EMPTY : (FormattedCharSequence) list.get(0);
        });
        int width = 0;
        for (FormattedCharSequence orderedText : orderedTexts) {
            width = Math.max(width, this.font.width(orderedText));
        }
        if (width % 2 != 0) {
            width++;
        }
        return width;
    }
}