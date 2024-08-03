package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemDisplayContext;

public class ThrownItemRenderer<T extends Entity & ItemSupplier> extends EntityRenderer<T> {

    private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;

    private final ItemRenderer itemRenderer;

    private final float scale;

    private final boolean fullBright;

    public ThrownItemRenderer(EntityRendererProvider.Context entityRendererProviderContext0, float float1, boolean boolean2) {
        super(entityRendererProviderContext0);
        this.itemRenderer = entityRendererProviderContext0.getItemRenderer();
        this.scale = float1;
        this.fullBright = boolean2;
    }

    public ThrownItemRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        this(entityRendererProviderContext0, 1.0F, false);
    }

    @Override
    protected int getBlockLightLevel(T t0, BlockPos blockPos1) {
        return this.fullBright ? 15 : super.getBlockLightLevel(t0, blockPos1);
    }

    @Override
    public void render(T t0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        if (t0.tickCount >= 2 || !(this.f_114476_.camera.getEntity().distanceToSqr(t0) < 12.25)) {
            poseStack3.pushPose();
            poseStack3.scale(this.scale, this.scale, this.scale);
            poseStack3.mulPose(this.f_114476_.cameraOrientation());
            poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F));
            this.itemRenderer.renderStatic(t0.getItem(), ItemDisplayContext.GROUND, int5, OverlayTexture.NO_OVERLAY, poseStack3, multiBufferSource4, t0.level(), t0.getId());
            poseStack3.popPose();
            super.render(t0, float1, float2, poseStack3, multiBufferSource4, int5);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity0) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}