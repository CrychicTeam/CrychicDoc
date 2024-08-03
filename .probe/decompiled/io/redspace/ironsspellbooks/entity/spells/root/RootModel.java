package io.redspace.ironsspellbooks.entity.spells.root;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RootModel extends GeoModel<RootEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/root.png");

    private static final ResourceLocation MODEL = new ResourceLocation("irons_spellbooks", "geo/root.geo.json");

    public static final ResourceLocation ANIMS = new ResourceLocation("irons_spellbooks", "animations/root_animations.json");

    public ResourceLocation getTextureResource(RootEntity object) {
        return TEXTURE;
    }

    public ResourceLocation getModelResource(RootEntity object) {
        return MODEL;
    }

    public ResourceLocation getAnimationResource(RootEntity animatable) {
        return ANIMS;
    }
}