package com.mna.blocks.tileentities.models;

import com.mna.blocks.tileentities.ImpaleSpikeTile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelImpaleSpike extends GeoModel<ImpaleSpikeTile> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/block/impale_spike.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/block/impale_spike.animation.json");

    private static final ResourceLocation texFile = new ResourceLocation("textures/block/dripstone_block.png");

    public ResourceLocation getAnimationResource(ImpaleSpikeTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(ImpaleSpikeTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(ImpaleSpikeTile arg0) {
        return texFile;
    }
}