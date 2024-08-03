package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.FrogModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.frog.Frog;

public class FrogRenderer extends MobRenderer<Frog, FrogModel<Frog>> {

    public FrogRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new FrogModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.FROG)), 0.3F);
    }

    public ResourceLocation getTextureLocation(Frog frog0) {
        return frog0.getVariant().texture();
    }
}