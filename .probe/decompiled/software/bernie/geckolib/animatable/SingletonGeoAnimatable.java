package software.bernie.geckolib.animatable;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.network.GeckoLibNetwork;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.network.packet.AnimDataSyncPacket;
import software.bernie.geckolib.network.packet.AnimTriggerPacket;

public interface SingletonGeoAnimatable extends GeoAnimatable {

    static void registerSyncedAnimatable(GeoAnimatable animatable) {
        GeckoLibNetwork.registerSyncedAnimatable(animatable);
    }

    @Nullable
    default <D> D getAnimData(long instanceId, SerializableDataTicket<D> dataTicket) {
        return this.getAnimatableInstanceCache().getManagerForId(instanceId).getData(dataTicket);
    }

    default <D> void setAnimData(Entity relatedEntity, long instanceId, SerializableDataTicket<D> dataTicket, D data) {
        if (relatedEntity.level().isClientSide()) {
            this.getAnimatableInstanceCache().getManagerForId(instanceId).setData(dataTicket, data);
        } else {
            this.syncAnimData(instanceId, dataTicket, data, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> relatedEntity));
        }
    }

    default <D> void syncAnimData(long instanceId, SerializableDataTicket<D> dataTicket, D data, PacketDistributor.PacketTarget packetTarget) {
        GeckoLibNetwork.send(new AnimDataSyncPacket<>(this.getClass().toString(), instanceId, dataTicket, data), packetTarget);
    }

    default <D> void triggerAnim(Entity relatedEntity, long instanceId, @Nullable String controllerName, String animName) {
        if (relatedEntity.level().isClientSide()) {
            this.getAnimatableInstanceCache().getManagerForId(instanceId).tryTriggerAnimation(controllerName, animName);
        } else {
            GeckoLibNetwork.send(new AnimTriggerPacket(this.getClass().toString(), instanceId, controllerName, animName), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> relatedEntity));
        }
    }

    default <D> void triggerAnim(long instanceId, @Nullable String controllerName, String animName, PacketDistributor.PacketTarget packetTarget) {
        GeckoLibNetwork.send(new AnimTriggerPacket(this.getClass().toString(), instanceId, controllerName, animName), packetTarget);
    }

    @Nullable
    @Override
    default AnimatableInstanceCache animatableCacheOverride() {
        return new SingletonAnimatableInstanceCache(this);
    }
}