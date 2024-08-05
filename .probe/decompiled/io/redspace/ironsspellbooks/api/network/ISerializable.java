package io.redspace.ironsspellbooks.api.network;

import net.minecraft.network.FriendlyByteBuf;

public interface ISerializable {

    void writeToBuffer(FriendlyByteBuf var1);

    void readFromBuffer(FriendlyByteBuf var1);
}