package com.mna.items.armor.models;

import com.mna.items.armor.BoneArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BoneArmorModel extends GeoModel<BoneArmorItem> {

    public ResourceLocation getModelResource(BoneArmorItem object) {
        return new ResourceLocation("mna", "geo/armor/bone_armor.geo.json");
    }

    public ResourceLocation getTextureResource(BoneArmorItem object) {
        return new ResourceLocation("mna", "textures/item/armor/model/bone_armor.png");
    }

    public ResourceLocation getAnimationResource(BoneArmorItem animatable) {
        return new ResourceLocation("mna", "animations/armor/bone_armor.animation.json");
    }
}