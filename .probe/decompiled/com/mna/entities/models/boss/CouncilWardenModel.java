package com.mna.entities.models.boss;

import com.mna.api.tools.RLoc;
import com.mna.entities.boss.CouncilWarden;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CouncilWardenModel extends GeoModel<CouncilWarden> {

    private static final ResourceLocation modelFile = RLoc.create("geo/bosses/council_warden.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/bosses/council_warden.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/entity/boss/council_warden.png");

    public ResourceLocation getAnimationResource(CouncilWarden arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(CouncilWarden arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(CouncilWarden arg0) {
        return texFile;
    }
}