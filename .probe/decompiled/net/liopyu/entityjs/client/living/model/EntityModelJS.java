package net.liopyu.entityjs.client.living.model;

import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.model.GeoModel;

public class EntityModelJS<T extends LivingEntity & IAnimatableJS> extends GeoModel<T> {

    private final BaseLivingEntityBuilder<T> builder;

    public EntityModelJS(BaseLivingEntityBuilder<T> builder) {
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