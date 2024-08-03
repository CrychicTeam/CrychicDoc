package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ExperienceOrbRenderer extends EntityRenderer<ExperienceOrb> {

    private static final ResourceLocation EXPERIENCE_ORB_LOCATION = new ResourceLocation("textures/entity/experience_orb.png");

    private static final RenderType RENDER_TYPE = RenderType.itemEntityTranslucentCull(EXPERIENCE_ORB_LOCATION);

    public ExperienceOrbRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.f_114477_ = 0.15F;
        this.f_114478_ = 0.75F;
    }

    protected int getBlockLightLevel(ExperienceOrb experienceOrb0, BlockPos blockPos1) {
        return Mth.clamp(super.getBlockLightLevel(experienceOrb0, blockPos1) + 7, 0, 15);
    }

    public void render(ExperienceOrb experienceOrb0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        int $$6 = experienceOrb0.getIcon();
        float $$7 = (float) ($$6 % 4 * 16 + 0) / 64.0F;
        float $$8 = (float) ($$6 % 4 * 16 + 16) / 64.0F;
        float $$9 = (float) ($$6 / 4 * 16 + 0) / 64.0F;
        float $$10 = (float) ($$6 / 4 * 16 + 16) / 64.0F;
        float $$11 = 1.0F;
        float $$12 = 0.5F;
        float $$13 = 0.25F;
        float $$14 = 255.0F;
        float $$15 = ((float) experienceOrb0.f_19797_ + float2) / 2.0F;
        int $$16 = (int) ((Mth.sin($$15 + 0.0F) + 1.0F) * 0.5F * 255.0F);
        int $$17 = 255;
        int $$18 = (int) ((Mth.sin($$15 + (float) (Math.PI * 4.0 / 3.0)) + 1.0F) * 0.1F * 255.0F);
        poseStack3.translate(0.0F, 0.1F, 0.0F);
        poseStack3.mulPose(this.f_114476_.cameraOrientation());
        poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F));
        float $$19 = 0.3F;
        poseStack3.scale(0.3F, 0.3F, 0.3F);
        VertexConsumer $$20 = multiBufferSource4.getBuffer(RENDER_TYPE);
        PoseStack.Pose $$21 = poseStack3.last();
        Matrix4f $$22 = $$21.pose();
        Matrix3f $$23 = $$21.normal();
        vertex($$20, $$22, $$23, -0.5F, -0.25F, $$16, 255, $$18, $$7, $$10, int5);
        vertex($$20, $$22, $$23, 0.5F, -0.25F, $$16, 255, $$18, $$8, $$10, int5);
        vertex($$20, $$22, $$23, 0.5F, 0.75F, $$16, 255, $$18, $$8, $$9, int5);
        vertex($$20, $$22, $$23, -0.5F, 0.75F, $$16, 255, $$18, $$7, $$9, int5);
        poseStack3.popPose();
        super.render(experienceOrb0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    private static void vertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, int int5, int int6, int int7, float float8, float float9, int int10) {
        vertexConsumer0.vertex(matrixF1, float3, float4, 0.0F).color(int5, int6, int7, 128).uv(float8, float9).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int10).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(ExperienceOrb experienceOrb0) {
        return EXPERIENCE_ORB_LOCATION;
    }
}