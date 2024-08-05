package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.SnowGolem;

public class SnowGolemRenderer extends MobRenderer<SnowGolem, SnowGolemModel<SnowGolem>> {

    private static final ResourceLocation SNOW_GOLEM_LOCATION = new ResourceLocation("textures/entity/snow_golem.png");

    public SnowGolemRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new SnowGolemModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.SNOW_GOLEM)), 0.5F);
        this.m_115326_(new SnowGolemHeadLayer(this, entityRendererProviderContext0.getBlockRenderDispatcher(), entityRendererProviderContext0.getItemRenderer()));
    }

    public ResourceLocation getTextureLocation(SnowGolem snowGolem0) {
        return SNOW_GOLEM_LOCATION;
    }
}