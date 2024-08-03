package io.redspace.ironsspellbooks.entity.spells.ice_block;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class IceBlockModel extends GeoModel<IceBlockProjectile> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/ice_block.png");

    private static final ResourceLocation MODEL = new ResourceLocation("irons_spellbooks", "geo/ice_block_projectile.geo.json");

    public static final ResourceLocation ANIMS = new ResourceLocation("irons_spellbooks", "animations/ice_block_animations.json");

    public ResourceLocation getTextureResource(IceBlockProjectile object) {
        return TEXTURE;
    }

    public ResourceLocation getModelResource(IceBlockProjectile object) {
        return MODEL;
    }

    public ResourceLocation getAnimationResource(IceBlockProjectile animatable) {
        return ANIMS;
    }
}