package com.mna.items.armor.models;

import com.mna.items.armor.FeyArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FeyArmorModel extends GeoModel<FeyArmorItem> {

    public ResourceLocation getModelResource(FeyArmorItem object) {
        return new ResourceLocation("mna", "geo/armor/fey_armor.geo.json");
    }

    public ResourceLocation getTextureResource(FeyArmorItem object) {
        return new ResourceLocation("mna", "textures/item/armor/model/fey_armor.png");
    }

    public ResourceLocation getAnimationResource(FeyArmorItem animatable) {
        return new ResourceLocation("mna", "animations/armor/fey_armor.animation.json");
    }
}