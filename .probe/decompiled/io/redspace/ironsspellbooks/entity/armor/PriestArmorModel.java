package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.PriestArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class PriestArmorModel extends DefaultedItemGeoModel<PriestArmorItem> {

    public PriestArmorModel() {
        super(new ResourceLocation("irons_spellbooks", "armor/priest"));
    }

    public ResourceLocation getModelResource(PriestArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "geo/priest_armor.geo.json");
    }

    public ResourceLocation getTextureResource(PriestArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/priest.png");
    }

    public ResourceLocation getAnimationResource(PriestArmorItem animatable) {
        return new ResourceLocation("irons_spellbooks", "animations/wizard_armor_animation.json");
    }
}