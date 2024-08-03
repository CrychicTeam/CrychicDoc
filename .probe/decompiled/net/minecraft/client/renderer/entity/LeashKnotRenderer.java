package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.LeashKnotModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;

public class LeashKnotRenderer extends EntityRenderer<LeashFenceKnotEntity> {

    private static final ResourceLocation KNOT_LOCATION = new ResourceLocation("textures/entity/lead_knot.png");

    private final LeashKnotModel<LeashFenceKnotEntity> model;

    public LeashKnotRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.model = new LeashKnotModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.LEASH_KNOT));
    }

    public void render(LeashFenceKnotEntity leashFenceKnotEntity0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.scale(-1.0F, -1.0F, 1.0F);
        this.model.setupAnim(leashFenceKnotEntity0, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer $$6 = multiBufferSource4.getBuffer(this.model.m_103119_(KNOT_LOCATION));
        this.model.m_7695_(poseStack3, $$6, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack3.popPose();
        super.render(leashFenceKnotEntity0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(LeashFenceKnotEntity leashFenceKnotEntity0) {
        return KNOT_LOCATION;
    }
}