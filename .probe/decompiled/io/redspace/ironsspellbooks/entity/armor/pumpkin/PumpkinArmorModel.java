package io.redspace.ironsspellbooks.entity.armor.pumpkin;

import io.redspace.ironsspellbooks.item.armor.PumpkinArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PumpkinArmorModel extends GeoModel<PumpkinArmorItem> {

    public ResourceLocation getModelResource(PumpkinArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "geo/pumpkin_armor.geo.json");
    }

    public ResourceLocation getTextureResource(PumpkinArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/pumpkin.png");
    }

    public ResourceLocation getAnimationResource(PumpkinArmorItem animatable) {
        return new ResourceLocation("irons_spellbooks", "animations/wizard_armor_animation.json");
    }
}