package net.liopyu.entityjs.client.nonliving.model;

import net.liopyu.entityjs.builders.nonliving.BaseEntityBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IAnimatableJSNL;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.model.GeoModel;

public class NonLivingEntityModel<T extends Entity & IAnimatableJSNL> extends GeoModel<T> {

    private final BaseEntityBuilder<T> builder;

    public NonLivingEntityModel(BaseEntityBuilder<T> builder) {
        this.builder = builder;
    }

    public ResourceLocation getModelResource(T object) {
        return (ResourceLocation) this.builder.modelResource.apply(object);
    }

    public ResourceLocation getTextureResource(T object) {
        return (ResourceLocation) this.builder.textureResource.apply(object);
    }

    public ResourceLocation getAnimationResource(T animatable) {
        return (ResourceLocation) this.builder.animationResource.apply(animatable);
    }
}