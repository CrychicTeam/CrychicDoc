package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Ocelot;

public class OcelotRenderer extends MobRenderer<Ocelot, OcelotModel<Ocelot>> {

    private static final ResourceLocation CAT_OCELOT_LOCATION = new ResourceLocation("textures/entity/cat/ocelot.png");

    public OcelotRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new OcelotModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.OCELOT)), 0.4F);
    }

    public ResourceLocation getTextureLocation(Ocelot ocelot0) {
        return CAT_OCELOT_LOCATION;
    }
}