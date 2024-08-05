package dev.architectury.extensions.network;

import net.minecraft.network.FriendlyByteBuf;

public interface EntitySpawnExtension {

    void saveAdditionalSpawnData(FriendlyByteBuf var1);

    void loadAdditionalSpawnData(FriendlyByteBuf var1);
}