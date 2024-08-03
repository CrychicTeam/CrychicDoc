package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BeeStingerLayer<T extends LivingEntity, M extends PlayerModel<T>> extends StuckInBodyLayer<T, M> {

    private static final ResourceLocation BEE_STINGER_LOCATION = new ResourceLocation("textures/entity/bee/bee_stinger.png");

    public BeeStingerLayer(LivingEntityRenderer<T, M> livingEntityRendererTM0) {
        super(livingEntityRendererTM0);
    }

    @Override
    protected int numStuck(T t0) {
        return t0.getStingerCount();
    }

    @Override
    protected void renderStuckItem(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Entity entity3, float float4, float float5, float float6, float float7) {
        float $$8 = Mth.sqrt(float4 * float4 + float6 * float6);
        float $$9 = (float) (Math.atan2((double) float4, (double) float6) * 180.0F / (float) Math.PI);
        float $$10 = (float) (Math.atan2((double) float5, (double) $$8) * 180.0F / (float) Math.PI);
        poseStack0.translate(0.0F, 0.0F, 0.0F);
        poseStack0.mulPose(Axis.YP.rotationDegrees($$9 - 90.0F));
        poseStack0.mulPose(Axis.ZP.rotationDegrees($$10));
        float $$11 = 0.0F;
        float $$12 = 0.125F;
        float $$13 = 0.0F;
        float $$14 = 0.0625F;
        float $$15 = 0.03125F;
        poseStack0.mulPose(Axis.XP.rotationDegrees(45.0F));
        poseStack0.scale(0.03125F, 0.03125F, 0.03125F);
        poseStack0.translate(2.5F, 0.0F, 0.0F);
        VertexConsumer $$16 = multiBufferSource1.getBuffer(RenderType.entityCutoutNoCull(BEE_STINGER_LOCATION));
        for (int $$17 = 0; $$17 < 4; $$17++) {
            poseStack0.mulPose(Axis.XP.rotationDegrees(90.0F));
            PoseStack.Pose $$18 = poseStack0.last();
            Matrix4f $$19 = $$18.pose();
            Matrix3f $$20 = $$18.normal();
            vertex($$16, $$19, $$20, -4.5F, -1, 0.0F, 0.0F, int2);
            vertex($$16, $$19, $$20, 4.5F, -1, 0.125F, 0.0F, int2);
            vertex($$16, $$19, $$20, 4.5F, 1, 0.125F, 0.0625F, int2);
            vertex($$16, $$19, $$20, -4.5F, 1, 0.0F, 0.0625F, int2);
        }
    }

    private static void vertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, int int4, float float5, float float6, int int7) {
        vertexConsumer0.vertex(matrixF1, float3, (float) int4, 0.0F).color(255, 255, 255, 255).uv(float5, float6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int7).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }
}