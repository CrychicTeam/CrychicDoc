package io.redspace.ironsspellbooks.entity.armor;

import io.redspace.ironsspellbooks.item.armor.PyromancerArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PyromancerArmorModel extends GeoModel<PyromancerArmorItem> {

    public ResourceLocation getModelResource(PyromancerArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "geo/pyromancer_armor.geo.json");
    }

    public ResourceLocation getTextureResource(PyromancerArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/pyromancer.png");
    }

    public ResourceLocation getAnimationResource(PyromancerArmorItem animatable) {
        return new ResourceLocation("irons_spellbooks", "animations/wizard_armor_animation.json");
    }
}