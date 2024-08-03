package com.mna.items.armor.models;

import com.mna.items.armor.DemonArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DemonArmorModel extends GeoModel<DemonArmorItem> {

    public ResourceLocation getModelResource(DemonArmorItem object) {
        return new ResourceLocation("mna", "geo/armor/demon_armor.geo.json");
    }

    public ResourceLocation getTextureResource(DemonArmorItem object) {
        return new ResourceLocation("mna", "textures/item/armor/model/demon_armor.png");
    }

    public ResourceLocation getAnimationResource(DemonArmorItem animatable) {
        return new ResourceLocation("mna", "animations/armor/demon_armor.animation.json");
    }
}