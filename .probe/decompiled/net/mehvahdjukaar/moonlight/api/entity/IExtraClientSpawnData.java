package net.mehvahdjukaar.moonlight.api.entity;

import net.minecraft.network.FriendlyByteBuf;

public interface IExtraClientSpawnData {

    void writeSpawnData(FriendlyByteBuf var1);

    void readSpawnData(FriendlyByteBuf var1);
}