package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.blockentity.CrystalCellBlockEntity;
import com.rekindled.embers.render.EmbersRenderTypes;
import java.util.Random;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class CrystalCellBlockEntityRenderer implements BlockEntityRenderer<CrystalCellBlockEntity> {

    public ResourceLocation texture = new ResourceLocation("embers:textures/block/crystal_material.png");

    Random random = new Random();

    public CrystalCellBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(CrystalCellBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        this.random.setSeed(blockEntity.seed);
        float capacityFactor = 120000.0F;
        double emberCapacity = blockEntity.renderCapacity;
        double lerpCapacity = emberCapacity * (double) partialTick + blockEntity.renderCapacityLast * (double) (1.0F - partialTick);
        int numLayers = 2 + (int) Math.floor(lerpCapacity / (double) capacityFactor) + 1;
        int numLayersOld = numLayers - 1;
        float growthFactor = this.getGrowthFactor(capacityFactor, lerpCapacity);
        float layerHeight = 0.25F;
        float height = layerHeight * (float) numLayers * growthFactor + (float) numLayersOld * layerHeight * (1.0F - growthFactor);
        float[] widths = new float[numLayers + 1];
        float[] oldWidths = new float[numLayers + 1];
        for (float i = 0.0F; i < (float) (numLayers + 1); i++) {
            float rand = this.random.nextFloat();
            if (i < (float) numLayers / 2.0F) {
                widths[(int) i] = i / ((float) numLayers / 2.0F) * (layerHeight * 0.1875F + layerHeight * 0.09375F * rand) * (float) numLayers;
            } else {
                widths[(int) i] = ((float) numLayers - i) / ((float) numLayers / 2.0F) * (layerHeight * 0.1875F + layerHeight * 0.09375F * rand) * (float) numLayers;
            }
            if (!(i >= (float) numLayersOld)) {
                if ((double) i < (double) numLayersOld / 2.0) {
                    oldWidths[(int) i] = i / ((float) numLayersOld / 2.0F) * (layerHeight * 0.1875F + layerHeight * 0.09375F * rand) * (float) numLayersOld;
                } else {
                    oldWidths[(int) i] = ((float) numLayersOld - i) / ((float) numLayersOld / 2.0F) * (layerHeight * 0.1875F + layerHeight * 0.09375F * rand) * (float) numLayersOld;
                }
            }
        }
        RenderSystem.setShaderTexture(0, this.texture);
        RenderSystem.disableCull();
        VertexConsumer buffer = bufferSource.getBuffer(ConfigManager.RENDER_FALLBACK.get() ? EmbersRenderTypes.CRYSTAL_FALLBACK : EmbersRenderTypes.CRYSTAL);
        for (float j = 0.0F; j < 12.0F; j++) {
            poseStack.pushPose();
            float scale = j / 12.0F;
            poseStack.translate(0.5, (double) (height / 2.0F) + 1.5, 0.5);
            poseStack.scale(scale, scale, scale);
            poseStack.mulPose(Axis.YP.rotationDegrees(partialTick + (float) (blockEntity.ticksExisted % 360L)));
            poseStack.mulPose(Axis.XP.rotationDegrees(30.0F * (float) Math.sin(Math.toRadians((double) (partialTick / 3.0F + (float) blockEntity.ticksExisted / 3.0F % 360.0F)))));
            Matrix4f matrix4f = poseStack.last().pose();
            for (int i = 0; i < widths.length - 1; i++) {
                float width = widths[i] * growthFactor + oldWidths[i] * (1.0F - growthFactor);
                float nextWidth = widths[i + 1] * growthFactor + oldWidths[i + 1] * (1.0F - growthFactor);
                buffer.vertex(matrix4f, -width, layerHeight * (float) i - height / 2.0F, -width).uv(0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, width, layerHeight * (float) i - height / 2.0F, -width).uv(0.5F, 0.0F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, nextWidth, layerHeight + layerHeight * (float) i - height / 2.0F, -nextWidth).uv(0.5F, 0.5F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, -nextWidth, layerHeight + layerHeight * (float) i - height / 2.0F, -nextWidth).uv(0.0F, 0.5F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, -width, layerHeight * (float) i - height / 2.0F, width).uv(0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, width, layerHeight * (float) i - height / 2.0F, width).uv(0.5F, 0.0F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, nextWidth, layerHeight + layerHeight * (float) i - height / 2.0F, nextWidth).uv(0.5F, 0.5F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, -nextWidth, layerHeight + layerHeight * (float) i - height / 2.0F, nextWidth).uv(0.0F, 0.5F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, -width, layerHeight * (float) i - height / 2.0F, -width).uv(0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, -width, layerHeight * (float) i - height / 2.0F, width).uv(0.5F, 0.0F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, -nextWidth, layerHeight + layerHeight * (float) i - height / 2.0F, nextWidth).uv(0.5F, 0.5F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, -nextWidth, layerHeight + layerHeight * (float) i - height / 2.0F, -nextWidth).uv(0.0F, 0.5F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, width, layerHeight * (float) i - height / 2.0F, -width).uv(0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, width, layerHeight * (float) i - height / 2.0F, width).uv(0.5F, 0.0F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, nextWidth, layerHeight + layerHeight * (float) i - height / 2.0F, nextWidth).uv(0.5F, 0.5F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
                buffer.vertex(matrix4f, nextWidth, layerHeight + layerHeight * (float) i - height / 2.0F, -nextWidth).uv(0.0F, 0.5F).color(1.0F, 1.0F, 1.0F, 0.65F).endVertex();
            }
            poseStack.popPose();
        }
        RenderSystem.enableCull();
    }

    private float getGrowthFactor(float capacityFactor, double emberCapacity) {
        return (float) (emberCapacity % (double) capacityFactor) / capacityFactor;
    }
}