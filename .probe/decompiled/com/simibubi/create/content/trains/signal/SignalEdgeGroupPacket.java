package com.simibubi.create.content.trains.signal;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SignalEdgeGroupPacket extends SimplePacketBase {

    List<UUID> ids;

    List<EdgeGroupColor> colors;

    boolean add;

    public SignalEdgeGroupPacket(UUID id, EdgeGroupColor color) {
        this(ImmutableList.of(id), ImmutableList.of(color), true);
    }

    public SignalEdgeGroupPacket(List<UUID> ids, List<EdgeGroupColor> colors, boolean add) {
        this.ids = ids;
        this.colors = colors;
        this.add = add;
    }

    public SignalEdgeGroupPacket(FriendlyByteBuf buffer) {
        this.ids = new ArrayList();
        this.colors = new ArrayList();
        this.add = buffer.readBoolean();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            this.ids.add(buffer.readUUID());
        }
        size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            this.colors.add(EdgeGroupColor.values()[buffer.readVarInt()]);
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.add);
        buffer.writeVarInt(this.ids.size());
        this.ids.forEach(buffer::m_130077_);
        buffer.writeVarInt(this.colors.size());
        this.colors.forEach(c -> buffer.writeVarInt(c.ordinal()));
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Map<UUID, SignalEdgeGroup> signalEdgeGroups = CreateClient.RAILWAYS.signalEdgeGroups;
            int i = 0;
            for (UUID id : this.ids) {
                if (!this.add) {
                    signalEdgeGroups.remove(id);
                } else {
                    SignalEdgeGroup group = new SignalEdgeGroup(id);
                    signalEdgeGroups.put(id, group);
                    if (this.colors.size() > i) {
                        group.color = (EdgeGroupColor) this.colors.get(i);
                    }
                    i++;
                }
            }
        });
        return true;
    }
}