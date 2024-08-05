package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownTrident;

public class ThrownTridentRenderer extends EntityRenderer<ThrownTrident> {

    public static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation("textures/entity/trident.png");

    private final TridentModel model;

    public ThrownTridentRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.model = new TridentModel(entityRendererProviderContext0.bakeLayer(ModelLayers.TRIDENT));
    }

    public void render(ThrownTrident thrownTrident0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.mulPose(Axis.YP.rotationDegrees(Mth.lerp(float2, thrownTrident0.f_19859_, thrownTrident0.m_146908_()) - 90.0F));
        poseStack3.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(float2, thrownTrident0.f_19860_, thrownTrident0.m_146909_()) + 90.0F));
        VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect(multiBufferSource4, this.model.m_103119_(this.getTextureLocation(thrownTrident0)), false, thrownTrident0.isFoil());
        this.model.renderToBuffer(poseStack3, $$6, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack3.popPose();
        super.render(thrownTrident0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(ThrownTrident thrownTrident0) {
        return TRIDENT_LOCATION;
    }
}