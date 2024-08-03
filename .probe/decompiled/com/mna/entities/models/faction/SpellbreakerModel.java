package com.mna.entities.models.faction;

import com.mna.entities.faction.Spellbreaker;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpellbreakerModel extends GeoModel<Spellbreaker> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/spellbreaker.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/spellbreaker_anim.json");

    private static final ResourceLocation texFile_t1 = new ResourceLocation("mna", "textures/entity/spellbreaker_t1.png");

    private static final ResourceLocation texFile_t2 = new ResourceLocation("mna", "textures/entity/spellbreaker_t2.png");

    private static final ResourceLocation texFile_t3 = new ResourceLocation("mna", "textures/entity/spellbreaker_t3.png");

    public ResourceLocation getAnimationResource(Spellbreaker arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(Spellbreaker arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(Spellbreaker arg0) {
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