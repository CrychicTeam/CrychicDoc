package com.mna.items.renderers.models;

import com.mna.api.tools.RLoc;
import com.mna.items.sorcery.bound.ItemBoundAxe;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelBoundAxe extends GeoModel<ItemBoundAxe> {

    private static final ResourceLocation anim = RLoc.create("animations/bound_swordaxe.animation.json");

    private static final ResourceLocation model = RLoc.create("geo/bound_axe.geo.json");

    private static final ResourceLocation texture = RLoc.create("textures/item/sorcery/bound_item.png");

    public ResourceLocation getAnimationResource(ItemBoundAxe animatable) {
        return anim;
    }

    public ResourceLocation getModelResource(ItemBoundAxe object) {
        return model;
    }

    public ResourceLocation getTextureResource(ItemBoundAxe object) {
        return texture;
    }
}