package com.mna.items.renderers.models;

import com.mna.api.tools.RLoc;
import com.mna.items.sorcery.bound.ItemBoundShield;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelBoundShield extends GeoModel<ItemBoundShield> {

    private static final ResourceLocation anim = RLoc.create("animations/bound_shield.animation.json");

    private static final ResourceLocation model = RLoc.create("geo/bound_shield.geo.json");

    private static final ResourceLocation texture = RLoc.create("textures/item/sorcery/bound_item.png");

    public ResourceLocation getAnimationResource(ItemBoundShield animatable) {
        return anim;
    }

    public ResourceLocation getModelResource(ItemBoundShield object) {
        return model;
    }

    public ResourceLocation getTextureResource(ItemBoundShield object) {
        return texture;
    }
}