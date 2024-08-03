package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.rekindled.embers.blockentity.CrystalSeedBlockEntity;
import com.rekindled.embers.render.EmbersRenderTypes;
import java.util.Random;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CrystalSeedBlockEntityRenderer implements BlockEntityRenderer<CrystalSeedBlockEntity> {

    public CrystalSeedBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(CrystalSeedBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null) {
            VertexConsumer buffer = bufferSource.getBuffer((RenderType) EmbersRenderTypes.CRYSTAL_SEED.apply(blockEntity.texture));
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Axis.XP.rotationDegrees(15.0F * (float) Math.sin(Math.toRadians((double) ((float) blockEntity.ticksExisted + partialTick)))));
            this.drawCrystal(buffer, poseStack.last().pose(), poseStack.last().normal(), 0.0F, 0.0F, 0.0F, ((float) blockEntity.ticksExisted + partialTick) * 6.0F, 1.0F, 0.25F, 0.0F, 0.75F, 1.0F, packedLight, packedOverlay);
            poseStack.mulPose(Axis.XP.rotationDegrees(-15.0F * (float) Math.sin(Math.toRadians((double) ((float) blockEntity.ticksExisted + partialTick)))));
            poseStack.mulPose(Axis.XP.rotationDegrees(-15.0F * (float) Math.sin(Math.toRadians((double) (2.5F * ((float) blockEntity.ticksExisted + partialTick))))));
            float oneAng = 360.0F / (float) blockEntity.willSpawn.length;
            Random crystalRandom = new Random(blockEntity.m_58899_().asLong());
            for (int i = 0; i < blockEntity.willSpawn.length; i++) {
                if (blockEntity.willSpawn[i]) {
                    float distVariation = 1.0F;
                    if (blockEntity.willSpawn.length > 12) {
                        distVariation += crystalRandom.nextFloat() * 0.5F;
                    }
                    float offX = distVariation * 0.4F * (float) Math.sin(Math.toRadians((double) (oneAng * (float) i + ((float) blockEntity.ticksExisted + partialTick) * 2.0F)));
                    float offZ = distVariation * 0.4F * (float) Math.cos(Math.toRadians((double) (oneAng * (float) i + ((float) blockEntity.ticksExisted + partialTick) * 2.0F)));
                    float texWidth = 0.25F * (float) blockEntity.size / 1000.0F;
                    float texHeight = 0.5F * (float) blockEntity.size / 1000.0F;
                    float texX = crystalRandom.nextFloat() * (1.0F - texWidth);
                    float texY = crystalRandom.nextFloat() * (1.0F - texHeight);
                    float sizeVariation = 0.5F + crystalRandom.nextFloat() * 0.5F;
                    this.drawCrystal(buffer, poseStack.last().pose(), poseStack.last().normal(), offX, 0.0F, offZ, ((float) blockEntity.ticksExisted + partialTick) * 2.0F, sizeVariation * 0.4F * ((float) blockEntity.size / 1000.0F), texX, texY, texX + texWidth, texY + texHeight, packedLight, packedOverlay);
                }
            }
            poseStack.popPose();
        }
    }

    public void drawCrystal(VertexConsumer b, Matrix4f matrix4f, Matrix3f normal, float x, float y, float z, float rotation, float size, float minU, float minV, float maxU, float maxV, int packedLight, int packedOverlay) {
        float offX1 = size * 0.25F * (float) Math.sin(Math.toRadians((double) rotation));
        float offZ1 = size * 0.25F * (float) Math.cos(Math.toRadians((double) rotation));
        float offX2 = size * 0.25F * (float) Math.sin(Math.toRadians((double) (rotation + 90.0F)));
        float offZ2 = size * 0.25F * (float) Math.cos(Math.toRadians((double) (rotation + 90.0F)));
        float pos1Y = y - size * 0.5F;
        float pos2X = x + offX1;
        float pos2Z = z + offZ1;
        float pos3X = x + offX2;
        float pos3Z = z + offZ2;
        float pos4X = x - offX1;
        float pos4Z = z - offZ1;
        float pos5X = x - offX2;
        float pos5Z = z - offZ2;
        float pos6Y = y + size * 0.5F;
        Vector3f bottom = new Vector3f(x, pos1Y, z);
        Vector3f normal1 = new Vector3f(pos5X, y, pos5Z).sub(bottom).cross(new Vector3f(pos4X, y, pos4Z).sub(bottom)).normalize().mul(-1.0F);
        Vector3f normal2 = new Vector3f(pos2X, y, pos2Z).sub(bottom).cross(new Vector3f(pos5X, y, pos5Z).sub(bottom)).normalize().mul(-1.0F);
        Vector3f normal3 = new Vector3f(pos3X, y, pos3Z).sub(bottom).cross(new Vector3f(pos2X, y, pos2Z).sub(bottom)).normalize().mul(-1.0F);
        Vector3f normal4 = new Vector3f(pos4X, y, pos4Z).sub(bottom).cross(new Vector3f(pos3X, y, pos3Z).sub(bottom)).normalize().mul(-1.0F);
        Vector3f normal5 = new Vector3f(normal3).mul(-1.0F);
        Vector3f normal6 = new Vector3f(normal4).mul(-1.0F);
        Vector3f normal7 = new Vector3f(normal1).mul(-1.0F);
        Vector3f normal8 = new Vector3f(normal2).mul(-1.0F);
        b.vertex(matrix4f, x, pos1Y, z).color(255, 255, 255, 255).uv((minU + maxU) / 2.0F, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal1.x, -normal1.y, normal1.z).endVertex();
        b.vertex(matrix4f, pos3X, y, pos3Z).color(255, 255, 255, 255).uv(maxU, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal1.x, -normal1.y, normal1.z).endVertex();
        b.vertex(matrix4f, pos2X, y, pos2Z).color(255, 255, 255, 255).uv(minU, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal1.x, -normal1.y, normal1.z).endVertex();
        b.vertex(matrix4f, x, pos1Y, z).color(255, 255, 255, 255).uv((minU + maxU) / 2.0F, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal2.x, -normal2.y, normal2.z).endVertex();
        b.vertex(matrix4f, pos4X, y, pos4Z).color(255, 255, 255, 255).uv(maxU, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal2.x, -normal2.y, normal2.z).endVertex();
        b.vertex(matrix4f, pos3X, y, pos3Z).color(255, 255, 255, 255).uv(minU, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal2.x, -normal2.y, normal2.z).endVertex();
        b.vertex(matrix4f, x, pos1Y, z).color(255, 255, 255, 255).uv((minU + maxU) / 2.0F, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal3.x, -normal3.y, normal3.z).endVertex();
        b.vertex(matrix4f, pos5X, y, pos5Z).color(255, 255, 255, 255).uv(maxU, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal3.x, -normal3.y, normal3.z).endVertex();
        b.vertex(matrix4f, pos4X, y, pos4Z).color(255, 255, 255, 255).uv(minU, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal3.x, -normal3.y, normal3.z).endVertex();
        b.vertex(matrix4f, x, pos1Y, z).color(255, 255, 255, 255).uv((minU + maxU) / 2.0F, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal4.x, -normal4.y, normal4.z).endVertex();
        b.vertex(matrix4f, pos2X, y, pos2Z).color(255, 255, 255, 255).uv(maxU, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal4.x, -normal4.y, normal4.z).endVertex();
        b.vertex(matrix4f, pos5X, y, pos5Z).color(255, 255, 255, 255).uv(minU, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal4.x, -normal4.y, normal4.z).endVertex();
        b.vertex(matrix4f, x, pos6Y, z).color(255, 255, 255, 255).uv((minU + maxU) / 2.0F, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal5.x, -normal5.y, normal5.z).endVertex();
        b.vertex(matrix4f, pos2X, y, pos2Z).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal5.x, -normal5.y, normal5.z).endVertex();
        b.vertex(matrix4f, pos3X, y, pos3Z).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal5.x, -normal5.y, normal5.z).endVertex();
        b.vertex(matrix4f, x, pos6Y, z).color(255, 255, 255, 255).uv((minU + maxU) / 2.0F, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal6.x, -normal6.y, normal6.z).endVertex();
        b.vertex(matrix4f, pos3X, y, pos3Z).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal6.x, -normal6.y, normal6.z).endVertex();
        b.vertex(matrix4f, pos4X, y, pos4Z).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal6.x, -normal6.y, normal6.z).endVertex();
        b.vertex(matrix4f, x, pos6Y, z).color(255, 255, 255, 255).uv((minU + maxU) / 2.0F, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal7.x, -normal7.y, normal7.z).endVertex();
        b.vertex(matrix4f, pos4X, y, pos4Z).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal7.x, -normal7.y, normal7.z).endVertex();
        b.vertex(matrix4f, pos5X, y, pos5Z).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal7.x, -normal7.y, normal7.z).endVertex();
        b.vertex(matrix4f, x, pos6Y, z).color(255, 255, 255, 255).uv((minU + maxU) / 2.0F, minV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal8.x, -normal8.y, normal8.z).endVertex();
        b.vertex(matrix4f, pos5X, y, pos5Z).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal8.x, -normal8.y, normal8.z).endVertex();
        b.vertex(matrix4f, pos2X, y, pos2Z).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(packedOverlay).uv2(packedLight).normal(normal, normal8.x, -normal8.y, normal8.z).endVertex();
    }
}