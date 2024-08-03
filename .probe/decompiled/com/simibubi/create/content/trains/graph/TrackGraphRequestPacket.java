package com.simibubi.create.content.trains.graph;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class TrackGraphRequestPacket extends SimplePacketBase {

    private int netId;

    public TrackGraphRequestPacket(int netId) {
        this.netId = netId;
    }

    public TrackGraphRequestPacket(FriendlyByteBuf buffer) {
        this.netId = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.netId);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            for (TrackGraph trackGraph : Create.RAILWAYS.trackNetworks.values()) {
                if (trackGraph.netId == this.netId) {
                    Create.RAILWAYS.sync.sendFullGraphTo(trackGraph, context.getSender());
                    break;
                }
            }
        });
        return true;
    }
}