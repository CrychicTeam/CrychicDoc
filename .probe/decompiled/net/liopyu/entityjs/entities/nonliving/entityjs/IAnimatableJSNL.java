package net.liopyu.entityjs.entities.nonliving.entityjs;

import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.Objects;
import javax.annotation.Nullable;
import net.liopyu.entityjs.builders.nonliving.BaseEntityBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.network.GeckoLibNetwork;
import software.bernie.geckolib.network.packet.AnimTriggerPacket;
import software.bernie.geckolib.network.packet.EntityAnimTriggerPacket;

public interface IAnimatableJSNL extends GeoAnimatable, GeoEntity {

    BaseEntityBuilder<?> getBuilder();

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar data) {
        for (BaseEntityBuilder.AnimationControllerSupplier<?> supplier : this.getBuilder().animationSuppliers) {
            data.add(((BaseEntityBuilder.AnimationControllerSupplier<Object>) supplier).get(UtilsJS.cast(this)));
        }
    }

    @Override
    default void triggerAnim(@Nullable String controllerName, String animName) {
        Entity entity = (Entity) this;
        if (entity.level().isClientSide()) {
            this.getAnimatableInstanceCache().getManagerForId((long) entity.getId()).tryTriggerAnimation(controllerName, animName);
        } else {
            GeckoLibNetwork.send(new EntityAnimTriggerPacket(entity.getId(), controllerName, animName), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity));
        }
    }

    @Override
    AnimatableInstanceCache getAnimatableInstanceCache();

    default <D> void triggerAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, String animName) {
        if (relatedEntity.level().isClientSide()) {
            this.getAnimatableInstanceCache().getManagerForId(instanceId).tryTriggerAnimation(controllerName, animName);
        } else {
            GeckoLibNetwork.send(new AnimTriggerPacket(this.getClass().toString(), instanceId, controllerName, animName), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> relatedEntity));
        }
    }

    @Override
    default double getTick(Object entity) {
        return (double) ((Entity) entity).tickCount;
    }

    default String getTypeId() {
        return ((ResourceLocation) Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(this.getType()))).toString();
    }

    EntityType<?> getType();
}