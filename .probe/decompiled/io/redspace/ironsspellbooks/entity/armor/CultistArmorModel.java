package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.CultistArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class CultistArmorModel extends DefaultedItemGeoModel<CultistArmorItem> {

    public CultistArmorModel() {
        super(new ResourceLocation("irons_spellbooks", ""));
    }

    public ResourceLocation getModelResource(CultistArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "geo/cultist_armor.geo.json");
    }

    public ResourceLocation getTextureResource(CultistArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/cultist.png");
    }

    public ResourceLocation getAnimationResource(CultistArmorItem animatable) {
        return new ResourceLocation("irons_spellbooks", "animations/wizard_armor_animation.json");
    }
}