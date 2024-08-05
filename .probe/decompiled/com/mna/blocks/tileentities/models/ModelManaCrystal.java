package com.mna.blocks.tileentities.models;

import com.mna.blocks.tileentities.ManaCrystalTile;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelManaCrystal extends GeoModel<ManaCrystalTile> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/block/mana_crystal_armature.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/block/mana_crystal_armature.animation.json");

    private static final ResourceLocation texFile = new ResourceLocation("mna", "textures/block/material/mana_crystal/mana_crystal_new.png");

    public ResourceLocation getAnimationResource(ManaCrystalTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(ManaCrystalTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(ManaCrystalTile arg0) {
        return texFile;
    }
}