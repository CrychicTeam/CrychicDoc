package com.simibubi.create.content.trains.graph;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.GlobalRailwayManager;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.UUID;
import net.minecraftforge.network.NetworkEvent;

public abstract class TrackGraphPacket extends SimplePacketBase {

    public UUID graphId;

    public int netId;

    public boolean packetDeletesGraph;

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> this.handle(CreateClient.RAILWAYS, CreateClient.RAILWAYS.getOrCreateGraph(this.graphId, this.netId)));
        return true;
    }

    protected abstract void handle(GlobalRailwayManager var1, TrackGraph var2);
}