package com.mna.entities.renderers.ritual;

import com.mna.entities.rituals.TimeChangeBall;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class TimeChangeBallRenderer extends EntityRenderer<TimeChangeBall> {

    private static final float triangle_height = (float) (Math.sqrt(3.0) / 2.0);

    private static final int[] sun_orange_start = new int[] { 255, 255, 0 };

    private static final int[] sun_orange_end = new int[] { 255, 120, 0 };

    private static final int[] moon_blue_start = new int[] { 0, 255, 255 };

    private static final int[] moon_blue_end = new int[] { 0, 0, 255 };

    private static final int[] moon_purp_start = new int[] { 255, 0, 255 };

    private static final int[] moon_purp_end = new int[] { 138, 0, 255 };

    public TimeChangeBallRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(TimeChangeBall entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        renderRadiant(entityIn, matrixStackIn, bufferIn, 0.0F);
    }

    private static void renderRadiant(TimeChangeBall entityIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, float offsetFactor) {
        float rotationByAge = ((float) entityIn.getAge() + offsetFactor) / 220.0F;
        Random random = new Random(1234L);
        VertexConsumer lightingBuilder = bufferIn.getBuffer(RenderType.lightning());
        matrixStackIn.pushPose();
        Vec3 motion = entityIn.m_20184_();
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-entityIn.m_146909_()));
        matrixStackIn.scale((float) motion.x, (float) motion.y, (float) motion.z);
        for (int i = 0; i < 40; i++) {
            float f = random.nextFloat();
            int[] colorStart;
            int[] colorEnd;
            if (entityIn.getTimeChangeType() == TimeChangeBall.TIME_CHANGE_DAY) {
                colorStart = sun_orange_start;
                colorEnd = sun_orange_end;
            } else if ((double) f > 0.5) {
                colorStart = moon_blue_start;
                colorEnd = moon_blue_end;
            } else {
                colorStart = moon_purp_start;
                colorEnd = moon_purp_end;
            }
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F + 90.0F * rotationByAge));
            float hOffset = random.nextFloat() * 0.25F;
            float vOffset = random.nextFloat() * 0.25F;
            Matrix4f currentMatrix = matrixStackIn.last().pose();
            int alpha = 255;
            addStartVertices(lightingBuilder, currentMatrix, alpha, colorStart);
            addVertexNegativeOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addVertexPositiveOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addStartVertices(lightingBuilder, currentMatrix, alpha, colorStart);
            addVertexPositiveOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addVertexNoOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addStartVertices(lightingBuilder, currentMatrix, alpha, colorStart);
            addVertexNoOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
            addVertexNegativeOffset(lightingBuilder, currentMatrix, hOffset, vOffset, colorEnd);
        }
        matrixStackIn.popPose();
    }

    private static void addStartVertices(VertexConsumer vertexBuilder, Matrix4f renderMatrix, int alpha, int[] colors) {
        vertexBuilder.vertex(renderMatrix, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).endVertex();
        vertexBuilder.vertex(renderMatrix, 0.0F, 0.0F, 0.0F).color(colors[0], colors[1], colors[2], alpha).endVertex();
    }

    private static void addVertexNegativeOffset(VertexConsumer vertexBuilder, Matrix4f renderMatrix, float vOffset, float hOffset, int[] colors) {
        vertexBuilder.vertex(renderMatrix, -triangle_height * hOffset, vOffset, -0.5F * hOffset).color(colors[0], colors[1], colors[2], 0).endVertex();
    }

    private static void addVertexPositiveOffset(VertexConsumer vertexBuilder, Matrix4f renderMatrix, float vOffset, float hOffset, int[] colors) {
        vertexBuilder.vertex(renderMatrix, triangle_height * hOffset, vOffset, -0.5F * hOffset).color(colors[0], colors[1], colors[2], 0).endVertex();
    }

    private static void addVertexNoOffset(VertexConsumer vertexBuilder, Matrix4f renderMatrix, float vOffset, float hOffset, int[] colors) {
        vertexBuilder.vertex(renderMatrix, 0.0F, vOffset, hOffset).color(colors[0], colors[1], colors[2], 0).endVertex();
    }

    public ResourceLocation getTextureLocation(TimeChangeBall entity) {
        return null;
    }
}