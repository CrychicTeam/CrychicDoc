package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.ConstructWorkbenchTile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ConstructGantryModel extends GeoModel<ConstructWorkbenchTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/construct_arch_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/construct_arch_armature.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/material/runic_anvil.png");

    public ResourceLocation getAnimationResource(ConstructWorkbenchTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(ConstructWorkbenchTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(ConstructWorkbenchTile arg0) {
        return texFile;
    }
}