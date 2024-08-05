package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class GuardianRenderer extends MobRenderer<Guardian, GuardianModel> {

    private static final ResourceLocation GUARDIAN_LOCATION = new ResourceLocation("textures/entity/guardian.png");

    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation("textures/entity/guardian_beam.png");

    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_LOCATION);

    public GuardianRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        this(entityRendererProviderContext0, 0.5F, ModelLayers.GUARDIAN);
    }

    protected GuardianRenderer(EntityRendererProvider.Context entityRendererProviderContext0, float float1, ModelLayerLocation modelLayerLocation2) {
        super(entityRendererProviderContext0, new GuardianModel(entityRendererProviderContext0.bakeLayer(modelLayerLocation2)), float1);
    }

    public boolean shouldRender(Guardian guardian0, Frustum frustum1, double double2, double double3, double double4) {
        if (super.shouldRender(guardian0, frustum1, double2, double3, double4)) {
            return true;
        } else {
            if (guardian0.hasActiveAttackTarget()) {
                LivingEntity $$5 = guardian0.getActiveAttackTarget();
                if ($$5 != null) {
                    Vec3 $$6 = this.getPosition($$5, (double) $$5.m_20206_() * 0.5, 1.0F);
                    Vec3 $$7 = this.getPosition(guardian0, (double) guardian0.m_20192_(), 1.0F);
                    return frustum1.isVisible(new AABB($$7.x, $$7.y, $$7.z, $$6.x, $$6.y, $$6.z));
                }
            }
            return false;
        }
    }

    private Vec3 getPosition(LivingEntity livingEntity0, double double1, float float2) {
        double $$3 = Mth.lerp((double) float2, livingEntity0.f_19790_, livingEntity0.m_20185_());
        double $$4 = Mth.lerp((double) float2, livingEntity0.f_19791_, livingEntity0.m_20186_()) + double1;
        double $$5 = Mth.lerp((double) float2, livingEntity0.f_19792_, livingEntity0.m_20189_());
        return new Vec3($$3, $$4, $$5);
    }

    public void render(Guardian guardian0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        super.render(guardian0, float1, float2, poseStack3, multiBufferSource4, int5);
        LivingEntity $$6 = guardian0.getActiveAttackTarget();
        if ($$6 != null) {
            float $$7 = guardian0.getAttackAnimationScale(float2);
            float $$8 = guardian0.getClientSideAttackTime() + float2;
            float $$9 = $$8 * 0.5F % 1.0F;
            float $$10 = guardian0.m_20192_();
            poseStack3.pushPose();
            poseStack3.translate(0.0F, $$10, 0.0F);
            Vec3 $$11 = this.getPosition($$6, (double) $$6.m_20206_() * 0.5, float2);
            Vec3 $$12 = this.getPosition(guardian0, (double) $$10, float2);
            Vec3 $$13 = $$11.subtract($$12);
            float $$14 = (float) ($$13.length() + 1.0);
            $$13 = $$13.normalize();
            float $$15 = (float) Math.acos($$13.y);
            float $$16 = (float) Math.atan2($$13.z, $$13.x);
            poseStack3.mulPose(Axis.YP.rotationDegrees(((float) (Math.PI / 2) - $$16) * (180.0F / (float) Math.PI)));
            poseStack3.mulPose(Axis.XP.rotationDegrees($$15 * (180.0F / (float) Math.PI)));
            int $$17 = 1;
            float $$18 = $$8 * 0.05F * -1.5F;
            float $$19 = $$7 * $$7;
            int $$20 = 64 + (int) ($$19 * 191.0F);
            int $$21 = 32 + (int) ($$19 * 191.0F);
            int $$22 = 128 - (int) ($$19 * 64.0F);
            float $$23 = 0.2F;
            float $$24 = 0.282F;
            float $$25 = Mth.cos($$18 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
            float $$26 = Mth.sin($$18 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
            float $$27 = Mth.cos($$18 + (float) (Math.PI / 4)) * 0.282F;
            float $$28 = Mth.sin($$18 + (float) (Math.PI / 4)) * 0.282F;
            float $$29 = Mth.cos($$18 + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
            float $$30 = Mth.sin($$18 + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
            float $$31 = Mth.cos($$18 + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
            float $$32 = Mth.sin($$18 + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
            float $$33 = Mth.cos($$18 + (float) Math.PI) * 0.2F;
            float $$34 = Mth.sin($$18 + (float) Math.PI) * 0.2F;
            float $$35 = Mth.cos($$18 + 0.0F) * 0.2F;
            float $$36 = Mth.sin($$18 + 0.0F) * 0.2F;
            float $$37 = Mth.cos($$18 + (float) (Math.PI / 2)) * 0.2F;
            float $$38 = Mth.sin($$18 + (float) (Math.PI / 2)) * 0.2F;
            float $$39 = Mth.cos($$18 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
            float $$40 = Mth.sin($$18 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
            float $$42 = 0.0F;
            float $$43 = 0.4999F;
            float $$44 = -1.0F + $$9;
            float $$45 = $$14 * 2.5F + $$44;
            VertexConsumer $$46 = multiBufferSource4.getBuffer(BEAM_RENDER_TYPE);
            PoseStack.Pose $$47 = poseStack3.last();
            Matrix4f $$48 = $$47.pose();
            Matrix3f $$49 = $$47.normal();
            vertex($$46, $$48, $$49, $$33, $$14, $$34, $$20, $$21, $$22, 0.4999F, $$45);
            vertex($$46, $$48, $$49, $$33, 0.0F, $$34, $$20, $$21, $$22, 0.4999F, $$44);
            vertex($$46, $$48, $$49, $$35, 0.0F, $$36, $$20, $$21, $$22, 0.0F, $$44);
            vertex($$46, $$48, $$49, $$35, $$14, $$36, $$20, $$21, $$22, 0.0F, $$45);
            vertex($$46, $$48, $$49, $$37, $$14, $$38, $$20, $$21, $$22, 0.4999F, $$45);
            vertex($$46, $$48, $$49, $$37, 0.0F, $$38, $$20, $$21, $$22, 0.4999F, $$44);
            vertex($$46, $$48, $$49, $$39, 0.0F, $$40, $$20, $$21, $$22, 0.0F, $$44);
            vertex($$46, $$48, $$49, $$39, $$14, $$40, $$20, $$21, $$22, 0.0F, $$45);
            float $$50 = 0.0F;
            if (guardian0.f_19797_ % 2 == 0) {
                $$50 = 0.5F;
            }
            vertex($$46, $$48, $$49, $$25, $$14, $$26, $$20, $$21, $$22, 0.5F, $$50 + 0.5F);
            vertex($$46, $$48, $$49, $$27, $$14, $$28, $$20, $$21, $$22, 1.0F, $$50 + 0.5F);
            vertex($$46, $$48, $$49, $$31, $$14, $$32, $$20, $$21, $$22, 1.0F, $$50);
            vertex($$46, $$48, $$49, $$29, $$14, $$30, $$20, $$21, $$22, 0.5F, $$50);
            poseStack3.popPose();
        }
    }

    private static void vertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float float5, int int6, int int7, int int8, float float9, float float10) {
        vertexConsumer0.vertex(matrixF1, float3, float4, float5).color(int6, int7, int8, 255).uv(float9, float10).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(Guardian guardian0) {
        return GUARDIAN_LOCATION;
    }
}