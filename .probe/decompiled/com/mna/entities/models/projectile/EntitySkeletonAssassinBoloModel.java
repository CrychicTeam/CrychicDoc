package com.mna.entities.models.projectile;

import com.mna.entities.projectile.SkeletonAssassinBolo;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EntitySkeletonAssassinBoloModel extends GeoModel<SkeletonAssassinBolo> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/bolo.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/none.anim.json");

    private static final ResourceLocation texFile = new ResourceLocation("textures/entity/lead_knot.png");

    public ResourceLocation getAnimationResource(SkeletonAssassinBolo arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(SkeletonAssassinBolo arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(SkeletonAssassinBolo arg0) {
        return texFile;
    }
}