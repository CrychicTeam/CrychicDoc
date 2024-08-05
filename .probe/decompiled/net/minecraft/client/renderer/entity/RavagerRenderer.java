package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.RavagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ravager;

public class RavagerRenderer extends MobRenderer<Ravager, RavagerModel> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/illager/ravager.png");

    public RavagerRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new RavagerModel(entityRendererProviderContext0.bakeLayer(ModelLayers.RAVAGER)), 1.1F);
    }

    public ResourceLocation getTextureLocation(Ravager ravager0) {
        return TEXTURE_LOCATION;
    }
}