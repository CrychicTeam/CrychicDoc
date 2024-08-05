package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;

public class SkeletonRenderer extends HumanoidMobRenderer<AbstractSkeleton, SkeletonModel<AbstractSkeleton>> {

    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public SkeletonRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        this(entityRendererProviderContext0, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    public SkeletonRenderer(EntityRendererProvider.Context entityRendererProviderContext0, ModelLayerLocation modelLayerLocation1, ModelLayerLocation modelLayerLocation2, ModelLayerLocation modelLayerLocation3) {
        super(entityRendererProviderContext0, new SkeletonModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation1)), 0.5F);
        this.m_115326_(new HumanoidArmorLayer<>(this, new SkeletonModel(entityRendererProviderContext0.bakeLayer(modelLayerLocation2)), new SkeletonModel(entityRendererProviderContext0.bakeLayer(modelLayerLocation3)), entityRendererProviderContext0.getModelManager()));
    }

    public ResourceLocation getTextureLocation(AbstractSkeleton abstractSkeleton0) {
        return SKELETON_LOCATION;
    }

    protected boolean isShaking(AbstractSkeleton abstractSkeleton0) {
        return abstractSkeleton0.isShaking();
    }
}