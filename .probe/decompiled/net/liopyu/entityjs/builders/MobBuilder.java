package net.liopyu.entityjs.builders;

import net.liopyu.entityjs.builders.living.entityjs.PathfinderMobBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;

public abstract class MobBuilder<T extends PathfinderMob & IAnimatableJS> extends PathfinderMobBuilder<T> {

    public MobBuilder(ResourceLocation i) {
        super(i);
    }
}