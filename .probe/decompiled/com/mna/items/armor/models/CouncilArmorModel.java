package com.mna.items.armor.models;

import com.mna.items.armor.CouncilArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CouncilArmorModel extends GeoModel<CouncilArmorItem> {

    public ResourceLocation getModelResource(CouncilArmorItem object) {
        return new ResourceLocation("mna", "geo/armor/council_armor.geo.json");
    }

    public ResourceLocation getTextureResource(CouncilArmorItem object) {
        return new ResourceLocation("mna", "textures/item/armor/model/council_armor.png");
    }

    public ResourceLocation getAnimationResource(CouncilArmorItem animatable) {
        return new ResourceLocation("mna", "animations/armor/council_armor.animation.json");
    }
}