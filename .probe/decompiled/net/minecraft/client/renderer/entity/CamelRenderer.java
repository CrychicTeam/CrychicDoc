package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.CamelModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.camel.Camel;

public class CamelRenderer extends MobRenderer<Camel, CamelModel<Camel>> {

    private static final ResourceLocation CAMEL_LOCATION = new ResourceLocation("textures/entity/camel/camel.png");

    public CamelRenderer(EntityRendererProvider.Context entityRendererProviderContext0, ModelLayerLocation modelLayerLocation1) {
        super(entityRendererProviderContext0, new CamelModel<>(entityRendererProviderContext0.bakeLayer(modelLayerLocation1)), 0.7F);
    }

    public ResourceLocation getTextureLocation(Camel camel0) {
        return CAMEL_LOCATION;
    }
}