package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.PlaguedArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PlaguedArmorModel extends GeoModel<PlaguedArmorItem> {

    public ResourceLocation getModelResource(PlaguedArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "geo/plagued_armor.geo.json");
    }

    public ResourceLocation getTextureResource(PlaguedArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/plagued.png");
    }

    public ResourceLocation getAnimationResource(PlaguedArmorItem animatable) {
        return new ResourceLocation("irons_spellbooks", "animations/wizard_armor_animation.json");
    }
}