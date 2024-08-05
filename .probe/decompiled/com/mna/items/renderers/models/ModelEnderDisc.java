package com.mna.items.renderers.models;

import com.mna.api.tools.RLoc;
import com.mna.items.artifice.ItemEnderDisk;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelEnderDisc extends GeoModel<ItemEnderDisk> {

    private static final ResourceLocation anim = RLoc.create("animations/ender_disc_armature.animation.json");

    private static final ResourceLocation model = RLoc.create("geo/ender_disc_armature.geo.json");

    private static final ResourceLocation texture = RLoc.create("textures/item/artifice/ender_disc.png");

    public ResourceLocation getAnimationResource(ItemEnderDisk animatable) {
        return anim;
    }

    public ResourceLocation getModelResource(ItemEnderDisk object) {
        return model;
    }

    public ResourceLocation getTextureResource(ItemEnderDisk object) {
        return texture;
    }
}