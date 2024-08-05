package io.redspace.ironsspellbooks.entity.armor.netherite;

import io.redspace.ironsspellbooks.item.armor.NetheriteMageArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class NetheriteMageArmorModel extends DefaultedItemGeoModel<NetheriteMageArmorItem> {

    public NetheriteMageArmorModel() {
        super(new ResourceLocation("irons_spellbooks", ""));
    }

    public ResourceLocation getModelResource(NetheriteMageArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "geo/netherite_armor.geo.json");
    }

    public ResourceLocation getTextureResource(NetheriteMageArmorItem object) {
        return new ResourceLocation("irons_spellbooks", "textures/models/armor/netherite.png");
    }

    public ResourceLocation getAnimationResource(NetheriteMageArmorItem animatable) {
        return new ResourceLocation("irons_spellbooks", "animations/wizard_armor_animation.json");
    }
}