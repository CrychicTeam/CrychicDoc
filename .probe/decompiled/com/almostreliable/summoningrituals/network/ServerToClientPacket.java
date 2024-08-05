package com.almostreliable.summoningrituals.network;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public abstract class ServerToClientPacket<T> implements Packet<T> {

    @Override
    public void handle(T packet, Supplier<? extends NetworkEvent.Context> context) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            ((NetworkEvent.Context) context.get()).enqueueWork(() -> this.handlePacket(packet, level));
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract void handlePacket(T var1, ClientLevel var2);
}