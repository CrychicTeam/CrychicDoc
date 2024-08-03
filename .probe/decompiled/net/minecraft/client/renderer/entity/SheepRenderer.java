package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;

public class SheepRenderer extends MobRenderer<Sheep, SheepModel<Sheep>> {

    private static final ResourceLocation SHEEP_LOCATION = new ResourceLocation("textures/entity/sheep/sheep.png");

    public SheepRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new SheepModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.SHEEP)), 0.7F);
        this.m_115326_(new SheepFurLayer(this, entityRendererProviderContext0.getModelSet()));
    }

    public ResourceLocation getTextureLocation(Sheep sheep0) {
        return SHEEP_LOCATION;
    }
}