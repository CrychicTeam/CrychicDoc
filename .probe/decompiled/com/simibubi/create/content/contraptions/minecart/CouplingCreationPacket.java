package com.simibubi.create.content.contraptions.minecart;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.network.NetworkEvent;

public class CouplingCreationPacket extends SimplePacketBase {

    int id1;

    int id2;

    public CouplingCreationPacket(AbstractMinecart cart1, AbstractMinecart cart2) {
        this.id1 = cart1.m_19879_();
        this.id2 = cart2.m_19879_();
    }

    public CouplingCreationPacket(FriendlyByteBuf buffer) {
        this.id1 = buffer.readInt();
        this.id2 = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.id1);
        buffer.writeInt(this.id2);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                CouplingHandler.tryToCoupleCarts(sender, sender.m_9236_(), this.id1, this.id2);
            }
        });
        return true;
    }
}