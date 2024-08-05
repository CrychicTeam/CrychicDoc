package com.mna.blocks.tileentities.models;

import com.mna.blocks.tileentities.LodestarTile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelLodestar extends GeoModel<LodestarTile> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/block/lodestar_armature.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/block/lodestar_armature.animation.json");

    private static final ResourceLocation texFile = new ResourceLocation("mna", "textures/block/material/lodestar.png");

    public ResourceLocation getAnimationResource(LodestarTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(LodestarTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(LodestarTile arg0) {
        return texFile;
    }
}