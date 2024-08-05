package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.GoldCrownArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GoldCrownModel extends GeoModel<GoldCrownArmorItem> {

    public ResourceLocation getModelResource(GoldCrownArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "geo/tarnished_armor.geo.json");
    }

    public ResourceLocation getTextureResource(GoldCrownArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/gold_crown.png");
    }

    public ResourceLocation getAnimationResource(GoldCrownArmorItem animatable) {
        return new ResourceLocation("irons_spellbooks", "animations/wizard_armor_animation.json");
    }
}