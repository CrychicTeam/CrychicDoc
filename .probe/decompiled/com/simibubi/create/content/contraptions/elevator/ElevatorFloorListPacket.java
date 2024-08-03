package com.simibubi.create.content.contraptions.elevator;

import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.IntAttached;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class ElevatorFloorListPacket extends SimplePacketBase {

    private int entityId;

    private List<IntAttached<Couple<String>>> floorsList;

    public ElevatorFloorListPacket(AbstractContraptionEntity entity, List<IntAttached<Couple<String>>> floorsList) {
        this.entityId = entity.m_19879_();
        this.floorsList = floorsList;
    }

    public ElevatorFloorListPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        int size = buffer.readInt();
        this.floorsList = new ArrayList();
        for (int i = 0; i < size; i++) {
            this.floorsList.add(IntAttached.with(buffer.readInt(), Couple.create(buffer.readUtf(), buffer.readUtf())));
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.floorsList.size());
        for (IntAttached<Couple<String>> entry : this.floorsList) {
            buffer.writeInt(entry.getFirst());
            entry.getSecond().forEach(buffer::m_130070_);
        }
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level.getEntity(this.entityId) instanceof AbstractContraptionEntity ace) {
                if (ace.getContraption() instanceof ElevatorContraption ec) {
                    ec.namesList = this.floorsList;
                    ec.syncControlDisplays();
                }
            }
        });
        return true;
    }

    public static class RequestFloorList extends SimplePacketBase {

        private int entityId;

        public RequestFloorList(AbstractContraptionEntity entity) {
            this.entityId = entity.m_19879_();
        }

        public RequestFloorList(FriendlyByteBuf buffer) {
            this.entityId = buffer.readInt();
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeInt(this.entityId);
        }

        @Override
        public boolean handle(NetworkEvent.Context context) {
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender.m_9236_().getEntity(this.entityId) instanceof AbstractContraptionEntity ace) {
                    if (ace.getContraption() instanceof ElevatorContraption ec) {
                        AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> sender), new ElevatorFloorListPacket(ace, ec.namesList));
                    }
                }
            });
            return true;
        }
    }
}