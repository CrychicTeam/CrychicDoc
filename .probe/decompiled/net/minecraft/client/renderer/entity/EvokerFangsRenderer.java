package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.EvokerFangs;

public class EvokerFangsRenderer extends EntityRenderer<EvokerFangs> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/illager/evoker_fangs.png");

    private final EvokerFangsModel<EvokerFangs> model;

    public EvokerFangsRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.model = new EvokerFangsModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.EVOKER_FANGS));
    }

    public void render(EvokerFangs evokerFangs0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        float $$6 = evokerFangs0.getAnimationProgress(float2);
        if ($$6 != 0.0F) {
            float $$7 = 2.0F;
            if ($$6 > 0.9F) {
                $$7 *= (1.0F - $$6) / 0.1F;
            }
            poseStack3.pushPose();
            poseStack3.mulPose(Axis.YP.rotationDegrees(90.0F - evokerFangs0.m_146908_()));
            poseStack3.scale(-$$7, -$$7, $$7);
            float $$8 = 0.03125F;
            poseStack3.translate(0.0, -0.626, 0.0);
            poseStack3.scale(0.5F, 0.5F, 0.5F);
            this.model.setupAnim(evokerFangs0, $$6, 0.0F, 0.0F, evokerFangs0.m_146908_(), evokerFangs0.m_146909_());
            VertexConsumer $$9 = multiBufferSource4.getBuffer(this.model.m_103119_(TEXTURE_LOCATION));
            this.model.m_7695_(poseStack3, $$9, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack3.popPose();
            super.render(evokerFangs0, float1, float2, poseStack3, multiBufferSource4, int5);
        }
    }

    public ResourceLocation getTextureLocation(EvokerFangs evokerFangs0) {
        return TEXTURE_LOCATION;
    }
}