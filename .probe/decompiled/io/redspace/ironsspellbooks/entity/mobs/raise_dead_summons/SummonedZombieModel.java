package io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons;

import io.redspace.ironsspellbooks.entity.mobs.SummonedZombie;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SummonedZombieModel extends GeoModel<SummonedZombie> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/summoned_zombie.png");

    public static final ResourceLocation MODEL = new ResourceLocation("irons_spellbooks", "geo/abstract_casting_mob.geo.json");

    public static final ResourceLocation ANIMATIONS = new ResourceLocation("irons_spellbooks", "animations/casting_animations.json");

    public ResourceLocation getTextureResource(SummonedZombie object) {
        return TEXTURE;
    }

    public ResourceLocation getModelResource(SummonedZombie object) {
        return MODEL;
    }

    public ResourceLocation getAnimationResource(SummonedZombie animatable) {
        return ANIMATIONS;
    }
}