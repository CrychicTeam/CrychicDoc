package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public abstract class ArrowRenderer<T extends AbstractArrow> extends EntityRenderer<T> {

    public ArrowRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
    }

    public void render(T t0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.mulPose(Axis.YP.rotationDegrees(Mth.lerp(float2, t0.f_19859_, t0.m_146908_()) - 90.0F));
        poseStack3.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(float2, t0.f_19860_, t0.m_146909_())));
        int $$6 = 0;
        float $$7 = 0.0F;
        float $$8 = 0.5F;
        float $$9 = 0.0F;
        float $$10 = 0.15625F;
        float $$11 = 0.0F;
        float $$12 = 0.15625F;
        float $$13 = 0.15625F;
        float $$14 = 0.3125F;
        float $$15 = 0.05625F;
        float $$16 = (float) t0.shakeTime - float2;
        if ($$16 > 0.0F) {
            float $$17 = -Mth.sin($$16 * 3.0F) * $$16;
            poseStack3.mulPose(Axis.ZP.rotationDegrees($$17));
        }
        poseStack3.mulPose(Axis.XP.rotationDegrees(45.0F));
        poseStack3.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack3.translate(-4.0F, 0.0F, 0.0F);
        VertexConsumer $$18 = multiBufferSource4.getBuffer(RenderType.entityCutout(this.m_5478_(t0)));
        PoseStack.Pose $$19 = poseStack3.last();
        Matrix4f $$20 = $$19.pose();
        Matrix3f $$21 = $$19.normal();
        this.vertex($$20, $$21, $$18, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, int5);
        this.vertex($$20, $$21, $$18, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, int5);
        this.vertex($$20, $$21, $$18, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, int5);
        this.vertex($$20, $$21, $$18, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, int5);
        this.vertex($$20, $$21, $$18, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, int5);
        this.vertex($$20, $$21, $$18, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, int5);
        this.vertex($$20, $$21, $$18, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, int5);
        this.vertex($$20, $$21, $$18, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, int5);
        for (int $$22 = 0; $$22 < 4; $$22++) {
            poseStack3.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.vertex($$20, $$21, $$18, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, int5);
            this.vertex($$20, $$21, $$18, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, int5);
            this.vertex($$20, $$21, $$18, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, int5);
            this.vertex($$20, $$21, $$18, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, int5);
        }
        poseStack3.popPose();
        super.render(t0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public void vertex(Matrix4f matrixF0, Matrix3f matrixF1, VertexConsumer vertexConsumer2, int int3, int int4, int int5, float float6, float float7, int int8, int int9, int int10, int int11) {
        vertexConsumer2.vertex(matrixF0, (float) int3, (float) int4, (float) int5).color(255, 255, 255, 255).uv(float6, float7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int11).normal(matrixF1, (float) int8, (float) int10, (float) int9).endVertex();
    }
}