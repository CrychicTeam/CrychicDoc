package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.ElectromancerArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class ElectromancerArmorModel extends DefaultedItemGeoModel<ElectromancerArmorItem> {

    public ElectromancerArmorModel() {
        super(new ResourceLocation("irons_spellbooks", ""));
    }

    public ResourceLocation getModelResource(ElectromancerArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "geo/electromancer_armor.geo.json");
    }

    public ResourceLocation getTextureResource(ElectromancerArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/electromancer.png");
    }

    public ResourceLocation getAnimationResource(ElectromancerArmorItem animatable) {
        return new ResourceLocation("irons_spellbooks", "animations/wizard_armor_animation.json");
    }
}