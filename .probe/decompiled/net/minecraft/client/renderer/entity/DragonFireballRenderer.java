package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.DragonFireball;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class DragonFireballRenderer extends EntityRenderer<DragonFireball> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_fireball.png");

    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

    public DragonFireballRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
    }

    protected int getBlockLightLevel(DragonFireball dragonFireball0, BlockPos blockPos1) {
        return 15;
    }

    public void render(DragonFireball dragonFireball0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.scale(2.0F, 2.0F, 2.0F);
        poseStack3.mulPose(this.f_114476_.cameraOrientation());
        poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose $$6 = poseStack3.last();
        Matrix4f $$7 = $$6.pose();
        Matrix3f $$8 = $$6.normal();
        VertexConsumer $$9 = multiBufferSource4.getBuffer(RENDER_TYPE);
        vertex($$9, $$7, $$8, int5, 0.0F, 0, 0, 1);
        vertex($$9, $$7, $$8, int5, 1.0F, 0, 1, 1);
        vertex($$9, $$7, $$8, int5, 1.0F, 1, 1, 0);
        vertex($$9, $$7, $$8, int5, 0.0F, 1, 0, 0);
        poseStack3.popPose();
        super.render(dragonFireball0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    private static void vertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, int int3, float float4, int int5, int int6, int int7) {
        vertexConsumer0.vertex(matrixF1, float4 - 0.5F, (float) int5 - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float) int6, (float) int7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int3).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(DragonFireball dragonFireball0) {
        return TEXTURE_LOCATION;
    }
}