package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.WanderingMagicianArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WanderingMagicianModel extends GeoModel<WanderingMagicianArmorItem> {

    public ResourceLocation getModelResource(WanderingMagicianArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "geo/wandering_magician_armor.geo.json");
    }

    public ResourceLocation getTextureResource(WanderingMagicianArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/wandering_magician.png");
    }

    public ResourceLocation getAnimationResource(WanderingMagicianArmorItem animatable) {
        return new ResourceLocation("irons_spellbooks", "animations/wizard_armor_animation.json");
    }
}