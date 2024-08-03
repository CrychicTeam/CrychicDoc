package com.mna.entities.renderers;

import com.mna.api.recipes.IManaweavePattern;
import com.mna.entities.manaweaving.Manaweave;
import com.mna.tools.math.MathUtils;
import com.mna.tools.math.Vector3;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class ManaweaveRenderer extends EntityRenderer<Manaweave> {

    private static final ResourceLocation SPARKLE_TEXTURE = new ResourceLocation("mna", "textures/particle/sparkle.png");

    private static final RenderType RENDER_TYPE = RenderType.entityTranslucentCull(SPARKLE_TEXTURE);

    public ManaweaveRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(Manaweave entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        ArrayList<IManaweavePattern> patterns = entityIn.getPatterns();
        if (patterns != null) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0, 0.5, 0.0);
            for (IManaweavePattern p : patterns) {
                this.RenderPattern(p, matrixStackIn, bufferIn, entityIn.f_19797_, partialTicks);
            }
            matrixStackIn.popPose();
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    private void RenderPattern(IManaweavePattern pattern, PoseStack matrixStackIn, MultiBufferSource bufferIn, int life, float partialTicks) {
        byte[][] points = pattern.get();
        float offsetX = (float) points.length / 2.0F;
        float offsetY = (float) points[0].length / 2.0F;
        Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
        Quaternionf portalRotation = new Quaternionf(cameraRotation.x(), cameraRotation.y(), cameraRotation.z(), cameraRotation.w());
        float baseScale = 0.125F;
        float fadeInTicks = 5.0F;
        Vector3 colorA = new Vector3(153.0, 0.0, 247.0);
        Vector3 colorB = new Vector3(245.0, 66.0, 144.0);
        int a = (int) (MathUtils.clamp01((float) life / fadeInTicks) * 200.0F);
        float baseAlphaMod = ((float) life + partialTicks) / 9.0F;
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(portalRotation);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStackIn.translate(-2.6 * (double) baseScale, -2.6 * (double) baseScale, 0.0);
        matrixStackIn.scale(baseScale, baseScale, baseScale);
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f renderMatrix = matrixstack$entry.pose();
        Matrix3f normalMatrix = matrixstack$entry.normal();
        VertexConsumer vertexBuilder = bufferIn.getBuffer(RENDER_TYPE);
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j] == 1) {
                    int index = i * j + j;
                    float originX = offsetX - (float) j * 0.5F;
                    float originY = offsetY - (float) i * 0.5F;
                    double pct = Math.abs(Math.sin((double) (baseAlphaMod + (float) index)));
                    int loopA = MathUtils.clamp((int) ((double) a - 75.0 * pct), 0, 255);
                    pct = 1.0 - Math.cos(pct * pct);
                    Vector3 color = Vector3.lerp(colorA, colorB, (float) pct);
                    int r = (int) color.x;
                    int g = (int) color.y;
                    int b = (int) color.z;
                    addVertex(vertexBuilder, renderMatrix, normalMatrix, 0.0F + originX, 0.0F + originY, 0.0F, 1.0F, r, g, b, loopA);
                    addVertex(vertexBuilder, renderMatrix, normalMatrix, 1.0F + originX, 0.0F + originY, 1.0F, 1.0F, r, g, b, loopA);
                    addVertex(vertexBuilder, renderMatrix, normalMatrix, 1.0F + originX, 1.0F + originY, 1.0F, 0.0F, r, g, b, loopA);
                    addVertex(vertexBuilder, renderMatrix, normalMatrix, 0.0F + originX, 1.0F + originY, 0.0F, 0.0F, r, g, b, loopA);
                }
            }
        }
        matrixStackIn.popPose();
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, float x, float y, float u, float v, int r, int g, int b, int a) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(Manaweave entity) {
        return SPARKLE_TEXTURE;
    }
}