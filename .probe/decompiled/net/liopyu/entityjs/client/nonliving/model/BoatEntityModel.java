package net.liopyu.entityjs.client.nonliving.model;

import net.liopyu.entityjs.builders.nonliving.vanilla.BoatEntityBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IAnimatableJSNL;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import software.bernie.geckolib.model.GeoModel;

public class BoatEntityModel<T extends Boat & IAnimatableJSNL> extends GeoModel<T> {

    private final BoatEntityBuilder<T> builder;

    public BoatEntityModel(BoatEntityBuilder<?> builder) {
        this.builder = (BoatEntityBuilder<T>) builder;
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