package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.RunicAnvilTile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RunicAnvilModel extends GeoModel<RunicAnvilTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/anvil_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/anvil_armature.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/material/runic_anvil.png");

    public ResourceLocation getAnimationResource(RunicAnvilTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(RunicAnvilTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(RunicAnvilTile arg0) {
        return texFile;
    }
}