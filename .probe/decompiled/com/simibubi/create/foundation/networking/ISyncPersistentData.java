package com.simibubi.create.foundation.networking;

import com.simibubi.create.AllPackets;
import java.util.HashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public interface ISyncPersistentData {

    void onPersistentDataUpdated();

    default void syncPersistentDataWithTracking(Entity self) {
        AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> self), new ISyncPersistentData.PersistentDataPacket(self));
    }

    public static class PersistentDataPacket extends SimplePacketBase {

        private int entityId;

        private Entity entity;

        private CompoundTag readData;

        public PersistentDataPacket(Entity entity) {
            this.entity = entity;
            this.entityId = entity.getId();
        }

        public PersistentDataPacket(FriendlyByteBuf buffer) {
            this.entityId = buffer.readInt();
            this.readData = buffer.readNbt();
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeInt(this.entityId);
            buffer.writeNbt(this.entity.getPersistentData());
        }

        @Override
        public boolean handle(NetworkEvent.Context context) {
            context.enqueueWork(() -> {
                Entity entityByID = Minecraft.getInstance().level.getEntity(this.entityId);
                CompoundTag data = entityByID.getPersistentData();
                new HashSet(data.getAllKeys()).forEach(data::m_128473_);
                data.merge(this.readData);
                if (entityByID instanceof ISyncPersistentData) {
                    ((ISyncPersistentData) entityByID).onPersistentDataUpdated();
                }
            });
            return true;
        }
    }
}