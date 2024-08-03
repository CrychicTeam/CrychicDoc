package software.bernie.geckolib.animatable;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.network.GeckoLibNetwork;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.network.packet.EntityAnimDataSyncPacket;
import software.bernie.geckolib.network.packet.EntityAnimTriggerPacket;

public interface GeoEntity extends GeoAnimatable {

    @Nullable
    default <D> D getAnimData(SerializableDataTicket<D> dataTicket) {
        return this.getAnimatableInstanceCache().getManagerForId((long) ((Entity) this).getId()).getData(dataTicket);
    }

    default <D> void setAnimData(SerializableDataTicket<D> dataTicket, D data) {
        Entity entity = (Entity) this;
        if (entity.level().isClientSide()) {
            this.getAnimatableInstanceCache().getManagerForId((long) entity.getId()).setData(dataTicket, data);
        } else {
            GeckoLibNetwork.send(new EntityAnimDataSyncPacket<>(entity.getId(), dataTicket, data), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity));
        }
    }

    default void triggerAnim(@Nullable String controllerName, String animName) {
        Entity entity = (Entity) this;
        if (entity.level().isClientSide()) {
            this.getAnimatableInstanceCache().getManagerForId((long) entity.getId()).tryTriggerAnimation(controllerName, animName);
        } else {
            GeckoLibNetwork.send(new EntityAnimTriggerPacket(entity.getId(), controllerName, animName), PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity));
        }
    }

    @Override
    default double getTick(Object entity) {
        return (double) ((Entity) entity).tickCount;
    }
}