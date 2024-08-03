package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class FishingHookRenderer extends EntityRenderer<FishingHook> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/fishing_hook.png");

    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE_LOCATION);

    private static final double VIEW_BOBBING_SCALE = 960.0;

    public FishingHookRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
    }

    public void render(FishingHook fishingHook0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        Player $$6 = fishingHook0.getPlayerOwner();
        if ($$6 != null) {
            poseStack3.pushPose();
            poseStack3.pushPose();
            poseStack3.scale(0.5F, 0.5F, 0.5F);
            poseStack3.mulPose(this.f_114476_.cameraOrientation());
            poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F));
            PoseStack.Pose $$7 = poseStack3.last();
            Matrix4f $$8 = $$7.pose();
            Matrix3f $$9 = $$7.normal();
            VertexConsumer $$10 = multiBufferSource4.getBuffer(RENDER_TYPE);
            vertex($$10, $$8, $$9, int5, 0.0F, 0, 0, 1);
            vertex($$10, $$8, $$9, int5, 1.0F, 0, 1, 1);
            vertex($$10, $$8, $$9, int5, 1.0F, 1, 1, 0);
            vertex($$10, $$8, $$9, int5, 0.0F, 1, 0, 0);
            poseStack3.popPose();
            int $$11 = $$6.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            ItemStack $$12 = $$6.m_21205_();
            if (!$$12.is(Items.FISHING_ROD)) {
                $$11 = -$$11;
            }
            float $$13 = $$6.m_21324_(float2);
            float $$14 = Mth.sin(Mth.sqrt($$13) * (float) Math.PI);
            float $$15 = Mth.lerp(float2, $$6.f_20884_, $$6.f_20883_) * (float) (Math.PI / 180.0);
            double $$16 = (double) Mth.sin($$15);
            double $$17 = (double) Mth.cos($$15);
            double $$18 = (double) $$11 * 0.35;
            double $$19 = 0.8;
            double $$26;
            double $$27;
            double $$28;
            float $$29;
            if ((this.f_114476_.options == null || this.f_114476_.options.getCameraType().isFirstPerson()) && $$6 == Minecraft.getInstance().player) {
                double $$24 = 960.0 / (double) this.f_114476_.options.fov().get().intValue();
                Vec3 $$25 = this.f_114476_.camera.getNearPlane().getPointOnPlane((float) $$11 * 0.525F, -0.1F);
                $$25 = $$25.scale($$24);
                $$25 = $$25.yRot($$14 * 0.5F);
                $$25 = $$25.xRot(-$$14 * 0.7F);
                $$26 = Mth.lerp((double) float2, $$6.f_19854_, $$6.m_20185_()) + $$25.x;
                $$27 = Mth.lerp((double) float2, $$6.f_19855_, $$6.m_20186_()) + $$25.y;
                $$28 = Mth.lerp((double) float2, $$6.f_19856_, $$6.m_20189_()) + $$25.z;
                $$29 = $$6.m_20192_();
            } else {
                $$26 = Mth.lerp((double) float2, $$6.f_19854_, $$6.m_20185_()) - $$17 * $$18 - $$16 * 0.8;
                $$27 = $$6.f_19855_ + (double) $$6.m_20192_() + ($$6.m_20186_() - $$6.f_19855_) * (double) float2 - 0.45;
                $$28 = Mth.lerp((double) float2, $$6.f_19856_, $$6.m_20189_()) - $$16 * $$18 + $$17 * 0.8;
                $$29 = $$6.m_6047_() ? -0.1875F : 0.0F;
            }
            double $$30 = Mth.lerp((double) float2, fishingHook0.f_19854_, fishingHook0.m_20185_());
            double $$31 = Mth.lerp((double) float2, fishingHook0.f_19855_, fishingHook0.m_20186_()) + 0.25;
            double $$32 = Mth.lerp((double) float2, fishingHook0.f_19856_, fishingHook0.m_20189_());
            float $$33 = (float) ($$26 - $$30);
            float $$34 = (float) ($$27 - $$31) + $$29;
            float $$35 = (float) ($$28 - $$32);
            VertexConsumer $$36 = multiBufferSource4.getBuffer(RenderType.lineStrip());
            PoseStack.Pose $$37 = poseStack3.last();
            int $$38 = 16;
            for (int $$39 = 0; $$39 <= 16; $$39++) {
                stringVertex($$33, $$34, $$35, $$36, $$37, fraction($$39, 16), fraction($$39 + 1, 16));
            }
            poseStack3.popPose();
            super.render(fishingHook0, float1, float2, poseStack3, multiBufferSource4, int5);
        }
    }

    private static float fraction(int int0, int int1) {
        return (float) int0 / (float) int1;
    }

    private static void vertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, int int3, float float4, int int5, int int6, int int7) {
        vertexConsumer0.vertex(matrixF1, float4 - 0.5F, (float) int5 - 0.5F, 0.0F).color(255, 255, 255, 255).uv((float) int6, (float) int7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int3).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void stringVertex(float float0, float float1, float float2, VertexConsumer vertexConsumer3, PoseStack.Pose poseStackPose4, float float5, float float6) {
        float $$7 = float0 * float5;
        float $$8 = float1 * (float5 * float5 + float5) * 0.5F + 0.25F;
        float $$9 = float2 * float5;
        float $$10 = float0 * float6 - $$7;
        float $$11 = float1 * (float6 * float6 + float6) * 0.5F + 0.25F - $$8;
        float $$12 = float2 * float6 - $$9;
        float $$13 = Mth.sqrt($$10 * $$10 + $$11 * $$11 + $$12 * $$12);
        $$10 /= $$13;
        $$11 /= $$13;
        $$12 /= $$13;
        vertexConsumer3.vertex(poseStackPose4.pose(), $$7, $$8, $$9).color(0, 0, 0, 255).normal(poseStackPose4.normal(), $$10, $$11, $$12).endVertex();
    }

    public ResourceLocation getTextureLocation(FishingHook fishingHook0) {
        return TEXTURE_LOCATION;
    }
}