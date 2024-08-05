package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.SilverfishModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;

public class SilverfishRenderer extends MobRenderer<Silverfish, SilverfishModel<Silverfish>> {

    private static final ResourceLocation SILVERFISH_LOCATION = new ResourceLocation("textures/entity/silverfish.png");

    public SilverfishRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new SilverfishModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.SILVERFISH)), 0.3F);
    }

    protected float getFlipDegrees(Silverfish silverfish0) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(Silverfish silverfish0) {
        return SILVERFISH_LOCATION;
    }
}