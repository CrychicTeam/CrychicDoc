package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;

public class ChickenRenderer extends MobRenderer<Chicken, ChickenModel<Chicken>> {

    private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation("textures/entity/chicken.png");

    public ChickenRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new ChickenModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
    }

    public ResourceLocation getTextureLocation(Chicken chicken0) {
        return CHICKEN_LOCATION;
    }

    protected float getBob(Chicken chicken0, float float1) {
        float $$2 = Mth.lerp(float1, chicken0.oFlap, chicken0.flap);
        float $$3 = Mth.lerp(float1, chicken0.oFlapSpeed, chicken0.flapSpeed);
        return (Mth.sin($$2) + 1.0F) * $$3;
    }
}