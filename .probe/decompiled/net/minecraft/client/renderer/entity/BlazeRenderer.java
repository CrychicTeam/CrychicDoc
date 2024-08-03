package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Blaze;

public class BlazeRenderer extends MobRenderer<Blaze, BlazeModel<Blaze>> {

    private static final ResourceLocation BLAZE_LOCATION = new ResourceLocation("textures/entity/blaze.png");

    public BlazeRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new BlazeModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.BLAZE)), 0.5F);
    }

    protected int getBlockLightLevel(Blaze blaze0, BlockPos blockPos1) {
        return 15;
    }

    public ResourceLocation getTextureLocation(Blaze blaze0) {
        return BLAZE_LOCATION;
    }
}