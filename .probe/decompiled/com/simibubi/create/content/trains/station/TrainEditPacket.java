package com.simibubi.create.content.trains.station;

import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainIconType;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.Components;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class TrainEditPacket extends SimplePacketBase {

    private String name;

    private UUID id;

    private ResourceLocation iconType;

    public TrainEditPacket(UUID id, String name, ResourceLocation iconType) {
        this.name = name;
        this.id = id;
        this.iconType = iconType;
    }

    public TrainEditPacket(FriendlyByteBuf buffer) {
        this.id = buffer.readUUID();
        this.name = buffer.readUtf(256);
        this.iconType = buffer.readResourceLocation();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.id);
        buffer.writeUtf(this.name);
        buffer.writeResourceLocation(this.iconType);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            Level level = sender == null ? null : sender.m_9236_();
            Train train = (Train) Create.RAILWAYS.sided(level).trains.get(this.id);
            if (train != null) {
                if (!this.name.isBlank()) {
                    train.name = Components.literal(this.name);
                }
                train.icon = TrainIconType.byId(this.iconType);
                if (sender != null) {
                    AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new TrainEditPacket.TrainEditReturnPacket(this.id, this.name, this.iconType));
                }
            }
        });
        return true;
    }

    public static class TrainEditReturnPacket extends TrainEditPacket {

        public TrainEditReturnPacket(FriendlyByteBuf buffer) {
            super(buffer);
        }

        public TrainEditReturnPacket(UUID id, String name, ResourceLocation iconType) {
            super(id, name, iconType);
        }
    }
}