package net.minecraftforge.entity;

import net.minecraft.network.FriendlyByteBuf;

public interface IEntityAdditionalSpawnData {

    void writeSpawnData(FriendlyByteBuf var1);

    void readSpawnData(FriendlyByteBuf var1);
}