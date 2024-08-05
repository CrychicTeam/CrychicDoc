package com.mna.items.renderers.models;

import com.mna.api.tools.RLoc;
import com.mna.items.sorcery.bound.ItemBoundBow;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelBoundBow extends GeoModel<ItemBoundBow> {

    private static final ResourceLocation anim = RLoc.create("animations/bound_bow.animation.json");

    private static final ResourceLocation model = RLoc.create("geo/bound_bow.geo.json");

    private static final ResourceLocation texture = RLoc.create("textures/item/sorcery/bound_item.png");

    public ResourceLocation getAnimationResource(ItemBoundBow animatable) {
        return anim;
    }

    public ResourceLocation getModelResource(ItemBoundBow object) {
        return model;
    }

    public ResourceLocation getTextureResource(ItemBoundBow object) {
        return texture;
    }
}