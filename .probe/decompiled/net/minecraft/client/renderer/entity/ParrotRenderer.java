package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Parrot;

public class ParrotRenderer extends MobRenderer<Parrot, ParrotModel> {

    private static final ResourceLocation RED_BLUE = new ResourceLocation("textures/entity/parrot/parrot_red_blue.png");

    private static final ResourceLocation BLUE = new ResourceLocation("textures/entity/parrot/parrot_blue.png");

    private static final ResourceLocation GREEN = new ResourceLocation("textures/entity/parrot/parrot_green.png");

    private static final ResourceLocation YELLOW_BLUE = new ResourceLocation("textures/entity/parrot/parrot_yellow_blue.png");

    private static final ResourceLocation GREY = new ResourceLocation("textures/entity/parrot/parrot_grey.png");

    public ParrotRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0, new ParrotModel(entityRendererProviderContext0.bakeLayer(ModelLayers.PARROT)), 0.3F);
    }

    public ResourceLocation getTextureLocation(Parrot parrot0) {
        return getVariantTexture(parrot0.getVariant());
    }

    public static ResourceLocation getVariantTexture(Parrot.Variant parrotVariant0) {
        return switch(parrotVariant0) {
            case RED_BLUE ->
                RED_BLUE;
            case BLUE ->
                BLUE;
            case GREEN ->
                GREEN;
            case YELLOW_BLUE ->
                YELLOW_BLUE;
            case GRAY ->
                GREY;
        };
    }

    public float getBob(Parrot parrot0, float float1) {
        float $$2 = Mth.lerp(float1, parrot0.oFlap, parrot0.flap);
        float $$3 = Mth.lerp(float1, parrot0.oFlapSpeed, parrot0.flapSpeed);
        return (Mth.sin($$2) + 1.0F) * $$3;
    }
}