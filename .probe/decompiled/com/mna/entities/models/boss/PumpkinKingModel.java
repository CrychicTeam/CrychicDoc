package com.mna.entities.models.boss;

import com.mna.entities.boss.PumpkinKing;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PumpkinKingModel extends GeoModel<PumpkinKing> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/pumpkin_king.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/pumpkin_king.animation.json");

    private static final ResourceLocation texFile_t1 = new ResourceLocation("mna", "textures/entity/boss/pumpkin_king.png");

    public ResourceLocation getAnimationResource(PumpkinKing arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(PumpkinKing arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(PumpkinKing arg0) {
        return texFile_t1;
    }
}