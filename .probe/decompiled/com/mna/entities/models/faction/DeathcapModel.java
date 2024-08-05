package com.mna.entities.models.faction;

import com.mna.entities.faction.Deathcap;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DeathcapModel extends GeoModel<Deathcap> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/deathcap.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/deathcap.animation.json");

    public static final ResourceLocation texFile_t1 = new ResourceLocation("mna", "textures/entity/deathcap_t1.png");

    public static final ResourceLocation texFile_t2 = new ResourceLocation("mna", "textures/entity/deathcap_t2.png");

    public static final ResourceLocation texFile_t3 = new ResourceLocation("mna", "textures/entity/deathcap_t3.png");

    public ResourceLocation getAnimationResource(Deathcap arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(Deathcap arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(Deathcap arg0) {
        switch(arg0.getTier()) {
            case 0:
                return texFile_t1;
            case 1:
                return texFile_t2;
            case 2:
                return texFile_t3;
            default:
                return texFile_t1;
        }
    }
}