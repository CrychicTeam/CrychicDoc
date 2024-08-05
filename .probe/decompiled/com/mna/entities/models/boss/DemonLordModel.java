package com.mna.entities.models.boss;

import com.mna.api.tools.RLoc;
import com.mna.entities.boss.DemonLord;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DemonLordModel extends GeoModel<DemonLord> {

    private static final ResourceLocation modelFile = RLoc.create("geo/bosses/demon_lord.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/bosses/demon_lord.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/entity/boss/demon_lord.png");

    public ResourceLocation getAnimationResource(DemonLord arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(DemonLord arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(DemonLord arg0) {
        return texFile;
    }
}