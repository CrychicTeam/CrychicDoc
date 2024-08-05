package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Spider;

public class SpiderRenderer<T extends Spider> extends MobRenderer<T, SpiderModel<T>> {

    private static final ResourceLocation SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/spider.png");

    public SpiderRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        this(entityRendererProviderContext0, ModelLayers.SPIDER);
    }

    public SpiderRenderer(EntityRendererProvider.Context entityRendererProviderContext0, ModelLayerLocation modelLayerLocation1) {
        super(entityRendererProviderContext0, new SpiderModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation1)), 0.8F);
        this.m_115326_(new SpiderEyesLayer<>(this));
    }

    protected float getFlipDegrees(T t0) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(T t0) {
        return SPIDER_LOCATION;
    }
}