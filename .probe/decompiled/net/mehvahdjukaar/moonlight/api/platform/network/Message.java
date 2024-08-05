package net.mehvahdjukaar.moonlight.api.platform.network;

import net.minecraft.network.FriendlyByteBuf;

public interface Message {

    void writeToBuffer(FriendlyByteBuf var1);

    void handle(ChannelHandler.Context var1);
}