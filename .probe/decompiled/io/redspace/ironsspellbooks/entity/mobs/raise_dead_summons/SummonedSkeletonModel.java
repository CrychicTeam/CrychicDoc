package io.redspace.ironsspellbooks.entity.mobs.raise_dead_summons;

import io.redspace.ironsspellbooks.entity.mobs.SummonedSkeleton;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SummonedSkeletonModel extends GeoModel<SummonedSkeleton> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/summoned_skeleton.png");

    public static final ResourceLocation MODEL = new ResourceLocation("irons_spellbooks", "geo/skeleton_mob.geo.json");

    public static final ResourceLocation ANIMATIONS = new ResourceLocation("irons_spellbooks", "animations/casting_animations.json");

    public ResourceLocation getTextureResource(SummonedSkeleton object) {
        return TEXTURE;
    }

    public ResourceLocation getModelResource(SummonedSkeleton object) {
        return new ResourceLocation("irons_spellbooks", "geo/skeleton_mob.geo.json");
    }

    public ResourceLocation getAnimationResource(SummonedSkeleton animatable) {
        return ANIMATIONS;
    }
}