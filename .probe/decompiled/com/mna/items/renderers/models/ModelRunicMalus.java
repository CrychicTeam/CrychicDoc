package com.mna.items.renderers.models;

import com.mna.api.tools.RLoc;
import com.mna.items.relic.ItemRunicMalus;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ModelRunicMalus extends GeoModel<ItemRunicMalus> {

    private static final ResourceLocation anim = RLoc.create("animations/runic_malus.animation.json");

    private static final ResourceLocation model = RLoc.create("geo/runic_malus_armature.geo.json");

    private static final ResourceLocation texture = RLoc.create("textures/item/artifice/runic_malus.png");

    public ResourceLocation getAnimationResource(ItemRunicMalus animatable) {
        return anim;
    }

    public ResourceLocation getModelResource(ItemRunicMalus object) {
        return model;
    }

    public ResourceLocation getTextureResource(ItemRunicMalus object) {
        return texture;
    }
}