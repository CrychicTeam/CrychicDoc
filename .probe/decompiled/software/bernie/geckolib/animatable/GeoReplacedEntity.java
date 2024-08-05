package software.bernie.geckolib.animatable;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.network.GeckoLibNetwork;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.network.packet.EntityAnimDataSyncPacket;
import software.bernie.geckolib.network.packet.EntityAnimTriggerPacket;

public interface GeoReplacedEntity extends SingletonGeoAnimatable {

    EntityType<?> getReplacingEntityType();

    @Nullable
    default <D> D getAnimData(Entity entity, SerializableDataTicket<D> dataTicket) {
        return this.getAnimatableInstanceCache().getManagerForId((long) entity.getId()).getData(dataTicket);
    }

    default <D> void setAnimData(Entity relatedEntity, SerializableDataTicket<D> dataTicket, D data) {
        if (relatedEntity.level().isClientSide()) {
            this.getAnimatableInstanceCache().getManagerForId((long) relatedEntity.getId()).setData(dataTicket, data);
        } else {
            GeckoLibNetwork.send(new EntityAnimDataSyncPacket<>(relatedEntity.getId(), dataTicket, data), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> relatedEntity));
        }
    }

    default void triggerAnim(Entity relatedEntity, @Nullable String controllerName, String animName) {
        if (relatedEntity.level().isClientSide()) {
            this.getAnimatableInstanceCache().getManagerForId((long) relatedEntity.getId()).tryTriggerAnimation(controllerName, animName);
        } else {
            GeckoLibNetwork.send(new EntityAnimTriggerPacket(relatedEntity.getId(), controllerName, animName), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> relatedEntity));
        }
    }

    @Override
    default double getTick(Object entity) {
        return (double) ((Entity) entity).tickCount;
    }
}