package com.mna.tools.render;

import com.mna.ManaAndArtifice;
import com.mna.api.particles.IWorldRenderUtils;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.api.tools.RLoc;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class WorldRenderUtils implements IWorldRenderUtils {

    public static final WorldRenderUtils INSTANCE = new WorldRenderUtils();

    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    public static final int FULL_BRIGHTNESS = 15728880;

    public static void renderFlatRadiant(Entity entityIn, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale) {
        renderFlatRadiant((float) entityIn.tickCount, pose, bufferSource, innerColor, outerColor, alpha, scale, true);
    }

    public static void renderFlatRadiant(float ageTicks, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale, boolean grow) {
        float rotationByAge = ageTicks / 100.0F;
        Random random = new Random(432L);
        VertexConsumer vertexconsumer2 = bufferSource.getBuffer(MARenderTypes.RADIANT_RENDER_TYPE);
        pose.pushPose();
        if (grow) {
            float growth = 1.0F + scale * 25.0F;
            pose.scale(growth, growth, growth);
        } else {
            pose.scale(scale, scale, scale);
        }
        alpha = Math.min(alpha, 255);
        for (int i = 0; i < 20; i++) {
            pose.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            pose.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + 90.0F * rotationByAge / 5.0F));
            float hOffset = random.nextFloat() * 0.25F;
            float vOffset = random.nextFloat() * 0.25F;
            Matrix4f matrix4f = pose.last().pose();
            vertex01(vertexconsumer2, matrix4f, innerColor, alpha);
            vertex2(vertexconsumer2, matrix4f, hOffset, vOffset, outerColor);
            vertex3(vertexconsumer2, matrix4f, hOffset, 0.0F, innerColor);
        }
        pose.popPose();
    }

    public static void renderRadiant(float ageTicks, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale, boolean grow) {
        float rotationByAge = ageTicks / 200.0F;
        Random random = new Random(432L);
        VertexConsumer vertexconsumer2 = bufferSource.getBuffer(MARenderTypes.RADIANT_RENDER_TYPE);
        pose.pushPose();
        if (grow) {
            float growth = 1.0F + scale * 25.0F;
            pose.scale(growth, growth, growth);
        } else {
            pose.scale(scale, scale, scale);
        }
        alpha = Math.min(alpha, 64);
        for (int i = 0; i < 40; i++) {
            pose.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            pose.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            pose.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F));
            pose.mulPose(Axis.XP.rotationDegrees(random.nextFloat() * 360.0F));
            pose.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
            pose.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360.0F + 90.0F * rotationByAge));
            float hOffset = i % 3 == 0 ? random.nextFloat() * 0.55F : random.nextFloat() * 0.25F;
            float vOffset = random.nextFloat() * 0.25F;
            Matrix4f matrix4f = pose.last().pose();
            vertex01(vertexconsumer2, matrix4f, innerColor, alpha);
            vertex2(vertexconsumer2, matrix4f, hOffset, vOffset, outerColor);
            vertex3(vertexconsumer2, matrix4f, hOffset, vOffset, innerColor);
            vertex01(vertexconsumer2, matrix4f, innerColor, alpha);
            vertex3(vertexconsumer2, matrix4f, hOffset, vOffset, outerColor);
            vertex4(vertexconsumer2, matrix4f, hOffset, vOffset, innerColor);
            vertex01(vertexconsumer2, matrix4f, innerColor, alpha);
            vertex4(vertexconsumer2, matrix4f, hOffset, vOffset, outerColor);
            vertex2(vertexconsumer2, matrix4f, hOffset, vOffset, innerColor);
        }
        pose.popPose();
    }

    public static void renderRadiant(Entity entityIn, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale) {
        renderRadiant((float) entityIn.tickCount, pose, bufferSource, innerColor, outerColor, alpha, scale, true);
    }

    public static void renderRadiantWithDirection(Entity entityIn, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale) {
        float rotationByAge = (float) entityIn.tickCount / 220.0F;
        Random random = new Random(432L);
        VertexConsumer lightingBuilder = bufferSource.getBuffer(MARenderTypes.RADIANT_RENDER_TYPE);
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(entityIn.getYRot() - 90.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(entityIn.getXRot() + 90.0F));
        pose.translate(0.0, -0.25, 0.0);
        pose.scale(scale, scale * 3.0F, scale);
        for (int i = 0; i < 20; i++) {
            pose.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F + 90.0F * rotationByAge));
            float hOffset = random.nextFloat() * 0.25F;
            float vOffset = random.nextFloat() * 0.25F;
            Matrix4f currentMatrix = pose.last().pose();
            vertex01(lightingBuilder, currentMatrix, innerColor, alpha);
            vertex2(lightingBuilder, currentMatrix, hOffset, vOffset, outerColor);
            vertex3(lightingBuilder, currentMatrix, hOffset, vOffset, innerColor);
            vertex01(lightingBuilder, currentMatrix, innerColor, alpha);
            vertex3(lightingBuilder, currentMatrix, hOffset, vOffset, outerColor);
            vertex4(lightingBuilder, currentMatrix, hOffset, vOffset, innerColor);
            vertex01(lightingBuilder, currentMatrix, innerColor, alpha);
            vertex4(lightingBuilder, currentMatrix, hOffset, vOffset, outerColor);
            vertex2(lightingBuilder, currentMatrix, hOffset, vOffset, innerColor);
        }
        pose.popPose();
    }

    public static void renderLightRay(float ageTicks, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale, float angle) {
        Random random = new Random(432L);
        VertexConsumer vertexconsumer2 = bufferSource.getBuffer(MARenderTypes.RADIANT_RENDER_TYPE);
        alpha = Math.min(alpha, 255);
        pose.pushPose();
        pose.scale(scale, scale, scale);
        pose.mulPose(Axis.ZP.rotationDegrees(angle));
        float hOffset = random.nextFloat() * 0.25F;
        float vOffset = random.nextFloat() * 0.25F;
        Matrix4f matrix4f = pose.last().pose();
        vertex1(vertexconsumer2, matrix4f, innerColor, alpha);
        vertex2(vertexconsumer2, matrix4f, hOffset, vOffset, outerColor);
        vertex3(vertexconsumer2, matrix4f, hOffset, 0.0F, innerColor);
        pose.popPose();
    }

    public static void renderLightBeam(float ageTicks, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale, float width, float flareScale, float angle) {
        Random random = new Random(432L);
        VertexConsumer vertexconsumer2 = bufferSource.getBuffer(MARenderTypes.RADIANT_RENDER_TYPE);
        alpha = Math.min(alpha, 255);
        pose.pushPose();
        pose.scale(scale, scale, scale);
        pose.mulPose(Axis.ZP.rotationDegrees(angle));
        float vOffset = random.nextFloat() * 0.25F;
        Matrix4f matrix4f = pose.last().pose();
        vertexconsumer2.vertex(matrix4f, -width * flareScale, 0.0F, 0.0F).color(innerColor[0], innerColor[1], innerColor[2], alpha).endVertex();
        vertexconsumer2.vertex(matrix4f, -width, vOffset, 0.0F).color(outerColor[0], outerColor[1], outerColor[2], 0).endVertex();
        vertexconsumer2.vertex(matrix4f, width, vOffset, 0.0F).color(outerColor[0], outerColor[1], outerColor[2], 0).endVertex();
        vertexconsumer2.vertex(matrix4f, width * flareScale, 0.0F, 0.0F).color(innerColor[0], innerColor[1], innerColor[2], alpha).endVertex();
        pose.popPose();
    }

    public static void renderBeam(Level world, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, Vec3 start, Vec3 end, float lengthPct, int[] color, RenderType renderType) {
        renderBeam(world, partialTicks, pose, bufferSource, packedLight, start, end, lengthPct, color, 0.01F, renderType);
    }

    public static void renderBeam(Level world, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, Vec3 start, Vec3 end, float lengthPct, int[] color, float width, RenderType renderType) {
        renderBeam(world, partialTicks, pose, bufferSource, packedLight, start, end, lengthPct, color, 176, width, renderType);
    }

    public static void renderBeam(Level world, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, Vec3 start, Vec3 end, float lengthPct, int[] color, int alpha, float width, RenderType renderType) {
        float worldPartialTicks = (float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks;
        float uvSlideRate = worldPartialTicks * 0.1F % 1.0F;
        Vec3 direction = end.subtract(start);
        float beamLength = (float) direction.length() * lengthPct;
        direction = direction.normalize();
        float aCosDirectionY = (float) Math.acos(direction.y);
        float atan2DirectionXZ = (float) Math.atan2(direction.z, direction.x);
        float rotation = 0.0F;
        int red = color[0];
        int green = color[1];
        int blue = color[2];
        float x1Start = Mth.cos(rotation + (float) Math.PI) * width;
        float x1End = Mth.cos(rotation) * width;
        float y1Start = Mth.sin(rotation + (float) Math.PI) * width;
        float y1End = Mth.sin(rotation) * width;
        float x2Start = Mth.cos(rotation + (float) (Math.PI / 2)) * width;
        float x2End = Mth.cos(rotation + (float) (Math.PI * 3.0 / 2.0)) * width;
        float y2Start = Mth.sin(rotation + (float) (Math.PI / 2)) * width;
        float y2End = Mth.sin(rotation + (float) (Math.PI * 3.0 / 2.0)) * width;
        float vSlide = -1.0F - uvSlideRate;
        float f30 = beamLength * 2.5F + vSlide;
        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(((float) (Math.PI / 2) - atan2DirectionXZ) * (180.0F / (float) Math.PI)));
        pose.mulPose(Axis.XP.rotationDegrees(aCosDirectionY * (180.0F / (float) Math.PI)));
        VertexConsumer vertexBuilder = bufferSource.getBuffer(renderType);
        PoseStack.Pose activeStack = pose.last();
        Matrix4f renderMatrix = activeStack.pose();
        Matrix3f normalMatrix = activeStack.normal();
        float uMin = 0.05F;
        float uMax = 0.95F;
        createVertex(vertexBuilder, renderMatrix, normalMatrix, x1Start, beamLength, y1Start, red, green, blue, alpha, uMax, f30);
        createVertex(vertexBuilder, renderMatrix, normalMatrix, x1Start, 0.0F, y1Start, red, green, blue, alpha, uMax, vSlide);
        createVertex(vertexBuilder, renderMatrix, normalMatrix, x1End, 0.0F, y1End, red, green, blue, alpha, uMin, vSlide);
        createVertex(vertexBuilder, renderMatrix, normalMatrix, x1End, beamLength, y1End, red, green, blue, alpha, uMin, f30);
        createVertex(vertexBuilder, renderMatrix, normalMatrix, x2Start, beamLength, y2Start, red, green, blue, alpha, uMax, f30);
        createVertex(vertexBuilder, renderMatrix, normalMatrix, x2Start, 0.0F, y2Start, red, green, blue, alpha, uMax, vSlide);
        createVertex(vertexBuilder, renderMatrix, normalMatrix, x2End, 0.0F, y2End, red, green, blue, alpha, uMin, vSlide);
        createVertex(vertexBuilder, renderMatrix, normalMatrix, x2End, beamLength, y2End, red, green, blue, alpha, uMin, f30);
        pose.popPose();
    }

    public static void renderManaweavePatternNoTransparent(ManaweavingPattern pattern, Quaternionf rotation, PoseStack pose, MultiBufferSource bufferSource, boolean inventory) {
        renderManaweavePattern_internal(pattern, rotation, pose, bufferSource, inventory, RenderType.entityCutout(RLoc.create("textures/particle/sparkle.png")), new int[] { 109, 227, 220, 176 });
    }

    public static void renderManaweavePatternNoTransparent(ManaweavingPattern pattern, Quaternionf rotation, PoseStack pose, MultiBufferSource bufferSource, boolean inventory, int[] color) {
        renderManaweavePattern_internal(pattern, rotation, pose, bufferSource, inventory, RenderType.entityCutout(RLoc.create("textures/particle/sparkle.png")), color);
    }

    public static void renderManaweavePattern(IManaweavePattern pattern, Quaternionf rotation, PoseStack pose, MultiBufferSource bufferSource, boolean inventory) {
        renderManaweavePattern_internal(pattern, rotation, pose, bufferSource, inventory, MARenderTypes.RENDER_TYPE_MANAWEAVE, new int[] { 109, 227, 220, 176 });
    }

    public static void renderManaweavePattern(IManaweavePattern pattern, Quaternionf rotation, PoseStack pose, MultiBufferSource bufferSource, boolean inventory, int[] color) {
        renderManaweavePattern_internal(pattern, rotation, pose, bufferSource, inventory, MARenderTypes.RENDER_TYPE_MANAWEAVE, color);
    }

    public static void renderManaweavePattern_internal(IManaweavePattern pattern, Quaternionf rotation, PoseStack pose, MultiBufferSource bufferSource, boolean inventory, RenderType renderType, int[] color) {
        byte[][] points = pattern.get();
        float offsetX = (float) points.length / 2.0F;
        float offsetY = (float) points[0].length / 2.0F;
        float baseScale = 0.15F;
        pose.pushPose();
        pose.translate(0.0F, 2.0F, 0.0F);
        pose.mulPose(rotation);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        pose.translate(-3.6 * (double) baseScale, -3.6 * (double) baseScale, 0.0);
        pose.scale(baseScale, baseScale, baseScale);
        PoseStack.Pose pose$entry = pose.last();
        Matrix4f renderMatrix = pose$entry.pose();
        Matrix3f normalMatrix = pose$entry.normal();
        VertexConsumer vertexBuilder = bufferSource.getBuffer(renderType);
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j] == 1) {
                    float originX = offsetX - (float) j * 0.5F;
                    float originY = offsetY - (float) i * 0.5F;
                    createVertex(vertexBuilder, renderMatrix, normalMatrix, 0.0F + originX, 0.0F + originY, 0.0F, color[0], color[1], color[2], color[3], 0.0F, 1.0F);
                    createVertex(vertexBuilder, renderMatrix, normalMatrix, 1.0F + originX, 0.0F + originY, 0.0F, color[0], color[1], color[2], color[3], 1.0F, 1.0F);
                    createVertex(vertexBuilder, renderMatrix, normalMatrix, 1.0F + originX, 1.0F + originY, 0.0F, color[0], color[1], color[2], color[3], 1.0F, 0.0F);
                    createVertex(vertexBuilder, renderMatrix, normalMatrix, 0.0F + originX, 1.0F + originY, 0.0F, color[0], color[1], color[2], color[3], 0.0F, 0.0F);
                }
            }
        }
        pose.popPose();
    }

    public static void renderProgressBar(PoseStack pose, MultiBufferSource bufferSource, float percent, int[] rgb, int alpha) {
        Matrix4f matrix4f = pose.last().pose();
        Matrix3f normalMatrix = pose.last().normal();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(MARenderTypes.SOLID_RENDER_TYPE);
        vertexconsumer.vertex(matrix4f, 0.5F, -0.5F, 0.0F).color(rgb[0], rgb[1], rgb[2], alpha).uv2(15728880).normal(normalMatrix, 0.0F, 0.0F, 1.0F).endVertex();
        vertexconsumer.vertex(matrix4f, 0.5F - percent, -0.5F, 0.0F).color(rgb[0], rgb[1], rgb[2], alpha).uv2(15728880).normal(normalMatrix, 0.0F, 0.0F, 1.0F).endVertex();
        vertexconsumer.vertex(matrix4f, 0.5F - percent, 0.5F, 0.0F).color(rgb[0], rgb[1], rgb[2], alpha).uv2(15728880).normal(normalMatrix, 0.0F, 0.0F, 1.0F).endVertex();
        vertexconsumer.vertex(matrix4f, 0.5F, 0.5F, 0.0F).color(rgb[0], rgb[1], rgb[2], alpha).uv2(15728880).normal(normalMatrix, 0.0F, 0.0F, 1.0F).endVertex();
    }

    private static void vertex01(VertexConsumer vertexConsumer, Matrix4f renderMatrix, int[] rgb, int alpha) {
        vertexConsumer.vertex(renderMatrix, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
        vertexConsumer.vertex(renderMatrix, 0.0F, 0.0F, 0.0F).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
    }

    private static void vertex1(VertexConsumer vertexConsumer, Matrix4f renderMatrix, int[] rgb, int alpha) {
        vertexConsumer.vertex(renderMatrix, 0.0F, 0.0F, 0.0F).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
        vertexConsumer.vertex(renderMatrix, 0.0F, 0.0F, 0.0F).color(rgb[0], rgb[1], rgb[2], alpha).endVertex();
    }

    private static void vertex2(VertexConsumer vertexConsumer, Matrix4f renderMatrix, float y, float length, int[] rgb) {
        vertexConsumer.vertex(renderMatrix, -HALF_SQRT_3 * length, y, -0.2F * length).color(rgb[0], rgb[1], rgb[2], 0).endVertex();
    }

    private static void vertex3(VertexConsumer vertexConsumer, Matrix4f renderMatrix, float y, float length, int[] rgb) {
        vertexConsumer.vertex(renderMatrix, HALF_SQRT_3 * length, y, -0.2F * length).color(rgb[0], rgb[1], rgb[2], 0).endVertex();
    }

    private static void vertex4(VertexConsumer vertexConsumer, Matrix4f renderMatrix, float y, float length, int[] rgb) {
        vertexConsumer.vertex(renderMatrix, 0.0F, y, 1.0F * length).color(rgb[0], rgb[1], rgb[2], 0).endVertex();
    }

    private static void createVertex(VertexConsumer vertexBuilder, Matrix4f renderMatrix, Matrix3f normalMatrix, float x, float y, float z, int colorRed, int colorGreen, int colorBlue, int alpha, float u, float v) {
        vertexBuilder.vertex(renderMatrix, x, y, z).color(colorRed, colorGreen, colorBlue, alpha).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 1.0F, 0.0F, 0.0F).endVertex();
    }

    @Override
    public void radiant(Entity entityIn, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale) {
        renderRadiant(entityIn, pose, bufferSource, innerColor, outerColor, alpha, scale);
    }

    @Override
    public void radiant(float ageTicks, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale, boolean grow) {
        renderRadiant(ageTicks, pose, bufferSource, innerColor, outerColor, alpha, scale, grow);
    }

    @Override
    public void directionalRadiant(Entity entityIn, PoseStack pose, MultiBufferSource bufferSource, int[] innerColor, int[] outerColor, int alpha, float scale) {
        renderRadiantWithDirection(entityIn, pose, bufferSource, innerColor, outerColor, alpha, scale);
    }

    @Override
    public void beam(Level world, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, Vec3 start, Vec3 end, float lengthPct, int[] color, float width, RenderType renderType) {
        renderBeam(world, partialTicks, pose, bufferSource, packedLight, start, end, lengthPct, color, renderType);
    }

    @Override
    public void beam(Level world, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, Vec3 start, Vec3 end, float lengthPct, int[] color, int alpha, float width, RenderType renderType) {
        renderBeam(world, partialTicks, pose, bufferSource, packedLight, start, end, lengthPct, color, renderType);
    }

    @Override
    public void beam(Level world, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, Vec3 start, Vec3 end, float lengthPct, int[] color, RenderType renderType) {
        renderBeam(world, partialTicks, pose, bufferSource, packedLight, start, end, lengthPct, color, packedLight, lengthPct, renderType);
    }

    @Override
    public void manaweavePattern(ManaweavingPattern pattern, Quaternionf rotation, PoseStack pose, MultiBufferSource bufferSource, boolean inventory) {
        renderManaweavePattern(pattern, rotation, pose, bufferSource, inventory);
    }

    @Override
    public void manaweavePattern(ManaweavingPattern pattern, Quaternionf rotation, PoseStack pose, MultiBufferSource bufferSource, boolean inventory, int[] color) {
        renderManaweavePattern(pattern, rotation, pose, bufferSource, inventory, color);
    }

    @Override
    public void manaweavePatternNoTransparent(ManaweavingPattern pattern, Quaternionf rotation, PoseStack pose, MultiBufferSource bufferSource, boolean inventory) {
        renderManaweavePatternNoTransparent(pattern, rotation, pose, bufferSource, inventory);
    }

    @Override
    public void manaweavePatternNoTransparent(ManaweavingPattern pattern, Quaternionf rotation, PoseStack pose, MultiBufferSource bufferSource, boolean inventory, int[] color) {
        renderManaweavePatternNoTransparent(pattern, rotation, pose, bufferSource, inventory, color);
    }
}