package com.almostreliable.summoningrituals.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface Packet<T> {

    void encode(T var1, FriendlyByteBuf var2);

    T decode(FriendlyByteBuf var1);

    void handle(T var1, Supplier<? extends NetworkEvent.Context> var2);
}