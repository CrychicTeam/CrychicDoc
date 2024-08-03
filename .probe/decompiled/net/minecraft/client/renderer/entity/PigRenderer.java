package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;

public class PigRenderer extends MobRenderer<Pig, PigModel<Pig>> {

    private static final ResourceLocation PIG_LOCATION = new ResourceLocation("textures/entity/pig/pig.png");

    public PigRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new PigModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.PIG)), 0.7F);
        this.m_115326_(new SaddleLayer<>(this, new PigModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.PIG_SADDLE)), new ResourceLocation("textures/entity/pig/pig_saddle.png")));
    }

    public ResourceLocation getTextureLocation(Pig pig0) {
        return PIG_LOCATION;
    }
}