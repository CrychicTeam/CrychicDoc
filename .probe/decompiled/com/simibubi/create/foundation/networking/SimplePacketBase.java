package com.simibubi.create.foundation.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public abstract class SimplePacketBase {

    public abstract void write(FriendlyByteBuf var1);

    public abstract boolean handle(NetworkEvent.Context var1);
}