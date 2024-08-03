package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemDisplayContext;

public class FireworkEntityRenderer extends EntityRenderer<FireworkRocketEntity> {

    private final ItemRenderer itemRenderer;

    public FireworkEntityRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.itemRenderer = entityRendererProviderContext0.getItemRenderer();
    }

    public void render(FireworkRocketEntity fireworkRocketEntity0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.mulPose(this.f_114476_.cameraOrientation());
        poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F));
        if (fireworkRocketEntity0.isShotAtAngle()) {
            poseStack3.mulPose(Axis.ZP.rotationDegrees(180.0F));
            poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack3.mulPose(Axis.XP.rotationDegrees(90.0F));
        }
        this.itemRenderer.renderStatic(fireworkRocketEntity0.getItem(), ItemDisplayContext.GROUND, int5, OverlayTexture.NO_OVERLAY, poseStack3, multiBufferSource4, fireworkRocketEntity0.m_9236_(), fireworkRocketEntity0.m_19879_());
        poseStack3.popPose();
        super.render(fireworkRocketEntity0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(FireworkRocketEntity fireworkRocketEntity0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}