package com.mna.entities.models.boss;

import com.mna.api.tools.RLoc;
import com.mna.entities.boss.PigDragon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PigDragonModel extends GeoModel<PigDragon> {

    private static final ResourceLocation modelFile = RLoc.create("geo/bosses/pig_dragon.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/bosses/pig_dragon.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/entity/boss/pig_dragon.png");

    public ResourceLocation getAnimationResource(PigDragon arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(PigDragon arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(PigDragon arg0) {
        return texFile;
    }
}