package net.raphimc.immediatelyfast.injection.mixins.hud_batching.consumer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import net.raphimc.immediatelyfast.feature.batching.BatchingRenderLayers;
import net.raphimc.immediatelyfast.feature.batching.BlendFuncDepthFuncState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { GuiGraphics.class }, priority = 500)
public abstract class MixinDrawContext {

    @Shadow
    @Final
    private PoseStack pose;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void fillGradient(VertexConsumer var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8);

    @Inject(method = { "fill(Lnet/minecraft/client/render/RenderLayer;IIIIII)V" }, at = { @At("HEAD") }, cancellable = true)
    private void fillIntoBuffer(RenderType layer, int x1, int y1, int x2, int y2, int z, int color, CallbackInfo ci) {
        if (BatchingBuffers.FILL_CONSUMER != null) {
            ci.cancel();
            if (x1 < x2) {
                x1 ^= x2;
                x2 ^= x1;
                x1 ^= x2;
            }
            if (y1 < y2) {
                y1 ^= y2;
                y2 ^= y1;
                y1 ^= y2;
            }
            Matrix4f matrix = this.pose.last().pose();
            float[] shaderColor = RenderSystem.getShaderColor();
            int argb = (int) (shaderColor[3] * 255.0F) << 24 | (int) (shaderColor[0] * 255.0F) << 16 | (int) (shaderColor[1] * 255.0F) << 8 | (int) (shaderColor[2] * 255.0F);
            color = FastColor.ARGB32.multiply(color, argb);
            VertexConsumer vertexConsumer = BatchingBuffers.FILL_CONSUMER.getBuffer(layer);
            vertexConsumer.vertex(matrix, (float) x1, (float) y2, (float) z).color(color).endVertex();
            vertexConsumer.vertex(matrix, (float) x2, (float) y2, (float) z).color(color).endVertex();
            vertexConsumer.vertex(matrix, (float) x2, (float) y1, (float) z).color(color).endVertex();
            vertexConsumer.vertex(matrix, (float) x1, (float) y1, (float) z).color(color).endVertex();
        }
    }

    @Inject(method = { "fillGradient(Lnet/minecraft/client/render/RenderLayer;IIIIIII)V" }, at = { @At("HEAD") }, cancellable = true)
    private void fillIntoBuffer(RenderType layer, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z, CallbackInfo ci) {
        if (BatchingBuffers.FILL_CONSUMER != null) {
            ci.cancel();
            float[] shaderColor = RenderSystem.getShaderColor();
            int argb = (int) (shaderColor[3] * 255.0F) << 24 | (int) (shaderColor[0] * 255.0F) << 16 | (int) (shaderColor[1] * 255.0F) << 8 | (int) (shaderColor[2] * 255.0F);
            colorStart = FastColor.ARGB32.multiply(colorStart, argb);
            colorEnd = FastColor.ARGB32.multiply(colorEnd, argb);
            this.fillGradient(BatchingBuffers.FILL_CONSUMER.getBuffer(layer), startX, startY, endX, endY, z, colorStart, colorEnd);
        }
    }

    @Inject(method = { "drawTexturedQuad(Lnet/minecraft/util/Identifier;IIIIIFFFF)V" }, at = { @At("HEAD") }, cancellable = true)
    private void drawTexturedQuadIntoBuffer(ResourceLocation texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, CallbackInfo ci) {
        if (BatchingBuffers.TEXTURE_CONSUMER != null) {
            ci.cancel();
            Matrix4f matrix = this.pose.last().pose();
            float[] shaderColor = RenderSystem.getShaderColor();
            int r = (int) (shaderColor[0] * 255.0F);
            int g = (int) (shaderColor[1] * 255.0F);
            int b = (int) (shaderColor[2] * 255.0F);
            int a = (int) (shaderColor[3] * 255.0F);
            VertexConsumer vertexConsumer = BatchingBuffers.TEXTURE_CONSUMER.getBuffer((RenderType) BatchingRenderLayers.COLORED_TEXTURE.apply(this.minecraft.getTextureManager().getTexture(texture).getId(), BlendFuncDepthFuncState.current()));
            vertexConsumer.vertex(matrix, (float) x1, (float) y2, (float) z).uv(u1, v2).color(r, g, b, a).endVertex();
            vertexConsumer.vertex(matrix, (float) x2, (float) y2, (float) z).uv(u2, v2).color(r, g, b, a).endVertex();
            vertexConsumer.vertex(matrix, (float) x2, (float) y1, (float) z).uv(u2, v1).color(r, g, b, a).endVertex();
            vertexConsumer.vertex(matrix, (float) x1, (float) y1, (float) z).uv(u1, v1).color(r, g, b, a).endVertex();
        }
    }

    @Inject(method = { "drawTexturedQuad(Lnet/minecraft/util/Identifier;IIIIIFFFFFFFF)V" }, at = { @At("HEAD") }, cancellable = true)
    private void drawTexturedQuadIntoBuffer(ResourceLocation texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (BatchingBuffers.TEXTURE_CONSUMER != null) {
            ci.cancel();
            Matrix4f matrix = this.pose.last().pose();
            float[] shaderColor = RenderSystem.getShaderColor();
            int argb = (int) (shaderColor[3] * 255.0F) << 24 | (int) (shaderColor[0] * 255.0F) << 16 | (int) (shaderColor[1] * 255.0F) << 8 | (int) (shaderColor[2] * 255.0F);
            int color = FastColor.ARGB32.multiply((int) (alpha * 255.0F) << 24 | (int) (red * 255.0F) << 16 | (int) (green * 255.0F) << 8 | (int) (blue * 255.0F), argb);
            RenderSystem.enableBlend();
            VertexConsumer vertexConsumer = BatchingBuffers.TEXTURE_CONSUMER.getBuffer((RenderType) BatchingRenderLayers.COLORED_TEXTURE.apply(this.minecraft.getTextureManager().getTexture(texture).getId(), BlendFuncDepthFuncState.current()));
            vertexConsumer.vertex(matrix, (float) x1, (float) y2, (float) z).uv(u1, v2).color(color).endVertex();
            vertexConsumer.vertex(matrix, (float) x2, (float) y2, (float) z).uv(u2, v2).color(color).endVertex();
            vertexConsumer.vertex(matrix, (float) x2, (float) y1, (float) z).uv(u2, v1).color(color).endVertex();
            vertexConsumer.vertex(matrix, (float) x1, (float) y1, (float) z).uv(u1, v1).color(color).endVertex();
            RenderSystem.disableBlend();
        }
    }

    @ModifyArg(method = { "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V"))
    private MultiBufferSource renderItemIntoBuffer(ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model) {
        return BatchingBuffers.ITEM_MODEL_CONSUMER != null ? BatchingBuffers.ITEM_MODEL_CONSUMER : vertexConsumers;
    }

    @Inject(method = { "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V" }, at = { @At("HEAD") })
    private void renderItemOverlayIntoBufferStart(Font textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        BatchingBuffers.beginItemOverlayRendering();
    }

    @Inject(method = { "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V" }, at = { @At("RETURN") })
    private void renderItemOverlayIntoBufferEnd(Font textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        BatchingBuffers.endItemOverlayRendering();
    }

    @Inject(method = { "setScissor" }, at = { @At("HEAD") })
    private void forceDrawBatch(CallbackInfo ci) {
        if (BatchingBuffers.isHudBatching() && BatchingBuffers.hasDataToDraw()) {
            BatchingBuffers.forceDrawBuffers();
        }
    }
}