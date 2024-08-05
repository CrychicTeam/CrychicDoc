package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.DolphinModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.layers.DolphinCarryingItemLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Dolphin;

public class DolphinRenderer extends MobRenderer<Dolphin, DolphinModel<Dolphin>> {

    private static final ResourceLocation DOLPHIN_LOCATION = new ResourceLocation("textures/entity/dolphin.png");

    public DolphinRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new DolphinModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.DOLPHIN)), 0.7F);
        this.m_115326_(new DolphinCarryingItemLayer(this, entityRendererProviderContext0.getItemInHandRenderer()));
    }

    public ResourceLocation getTextureLocation(Dolphin dolphin0) {
        return DOLPHIN_LOCATION;
    }
}