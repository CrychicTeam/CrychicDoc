package com.mna.entities.models.faction;

import com.mna.api.tools.RLoc;
import com.mna.entities.faction.Pixie;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PixieModel extends GeoModel<Pixie> {

    private static final ResourceLocation modelFile = RLoc.create("geo/pixie.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/pixie.animation.json");

    private static final ResourceLocation texFile_water = RLoc.create("textures/entity/pixie_water.png");

    private static final ResourceLocation texFile_fire = RLoc.create("textures/entity/pixie_fire.png");

    private static final ResourceLocation texFile_earth = RLoc.create("textures/entity/pixie_earth.png");

    private static final ResourceLocation texFile_wind = RLoc.create("textures/entity/pixie_wind.png");

    public ResourceLocation getAnimationResource(Pixie arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(Pixie arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(Pixie arg0) {
        switch(arg0.getAffinity()) {
            case FIRE:
                return texFile_fire;
            case EARTH:
                return texFile_earth;
            case WIND:
                return texFile_wind;
            case WATER:
            default:
                return texFile_water;
        }
    }
}