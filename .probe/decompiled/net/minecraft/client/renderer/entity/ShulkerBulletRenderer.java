package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ShulkerBullet;

public class ShulkerBulletRenderer extends EntityRenderer<ShulkerBullet> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/shulker/spark.png");

    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE_LOCATION);

    private final ShulkerBulletModel<ShulkerBullet> model;

    public ShulkerBulletRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.model = new ShulkerBulletModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.SHULKER_BULLET));
    }

    protected int getBlockLightLevel(ShulkerBullet shulkerBullet0, BlockPos blockPos1) {
        return 15;
    }

    public void render(ShulkerBullet shulkerBullet0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        float $$6 = Mth.rotLerp(float2, shulkerBullet0.f_19859_, shulkerBullet0.m_146908_());
        float $$7 = Mth.lerp(float2, shulkerBullet0.f_19860_, shulkerBullet0.m_146909_());
        float $$8 = (float) shulkerBullet0.f_19797_ + float2;
        poseStack3.translate(0.0F, 0.15F, 0.0F);
        poseStack3.mulPose(Axis.YP.rotationDegrees(Mth.sin($$8 * 0.1F) * 180.0F));
        poseStack3.mulPose(Axis.XP.rotationDegrees(Mth.cos($$8 * 0.1F) * 180.0F));
        poseStack3.mulPose(Axis.ZP.rotationDegrees(Mth.sin($$8 * 0.15F) * 360.0F));
        poseStack3.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(shulkerBullet0, 0.0F, 0.0F, 0.0F, $$6, $$7);
        VertexConsumer $$9 = multiBufferSource4.getBuffer(this.model.m_103119_(TEXTURE_LOCATION));
        this.model.m_7695_(poseStack3, $$9, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack3.scale(1.5F, 1.5F, 1.5F);
        VertexConsumer $$10 = multiBufferSource4.getBuffer(RENDER_TYPE);
        this.model.m_7695_(poseStack3, $$10, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        poseStack3.popPose();
        super.render(shulkerBullet0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(ShulkerBullet shulkerBullet0) {
        return TEXTURE_LOCATION;
    }
}