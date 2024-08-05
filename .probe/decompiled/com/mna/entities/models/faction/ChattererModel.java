package com.mna.entities.models.faction;

import com.mna.api.tools.RLoc;
import com.mna.entities.faction.Chatterer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ChattererModel extends GeoModel<Chatterer> {

    private static final ResourceLocation modelFile = RLoc.create("geo/chatterer.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/chatterer.animation.json");

    private static final ResourceLocation texFile_t1 = RLoc.create("textures/entity/chatterer.png");

    public ResourceLocation getAnimationResource(Chatterer arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(Chatterer arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(Chatterer arg0) {
        return texFile_t1;
    }
}