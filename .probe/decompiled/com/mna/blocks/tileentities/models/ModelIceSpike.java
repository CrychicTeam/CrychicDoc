package com.mna.blocks.tileentities.models;

import com.mna.blocks.tileentities.IceSpikeTile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelIceSpike extends GeoModel<IceSpikeTile> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/block/ice_spike.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/block/ice_spike.animation.json");

    private static final ResourceLocation texFile = new ResourceLocation("textures/block/ice.png");

    public ResourceLocation getAnimationResource(IceSpikeTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(IceSpikeTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(IceSpikeTile arg0) {
        return texFile;
    }
}