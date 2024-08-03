package com.mna.entities.models.faction;

import com.mna.entities.faction.SkeletonAssassin;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SkeletonAssassinModel extends GeoModel<SkeletonAssassin> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/skeleton_assassin.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/skeleton_assassin.anim.json");

    private static final ResourceLocation texFile_t1 = new ResourceLocation("mna", "textures/entity/skeleton_assassin_t1.png");

    private static final ResourceLocation texFile_t2 = new ResourceLocation("mna", "textures/entity/skeleton_assassin_t2.png");

    private static final ResourceLocation texFile_t3 = new ResourceLocation("mna", "textures/entity/skeleton_assassin_t3.png");

    public ResourceLocation getAnimationResource(SkeletonAssassin arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(SkeletonAssassin arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(SkeletonAssassin arg0) {
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