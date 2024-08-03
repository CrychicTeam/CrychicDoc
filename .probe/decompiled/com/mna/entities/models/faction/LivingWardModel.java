package com.mna.entities.models.faction;

import com.mna.entities.faction.LivingWard;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LivingWardModel extends GeoModel<LivingWard> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/arcane_eye.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/arcane_eye.animation.json");

    private static final ResourceLocation texFile_t1 = new ResourceLocation("mna", "textures/entity/arcane_eye_t1.png");

    private static final ResourceLocation texFile_t2 = new ResourceLocation("mna", "textures/entity/arcane_eye_t1.png");

    private static final ResourceLocation texFile_t3 = new ResourceLocation("mna", "textures/entity/arcane_eye_t1.png");

    public ResourceLocation getAnimationResource(LivingWard arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(LivingWard arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(LivingWard arg0) {
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