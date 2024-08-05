package dev.shadowsoffire.placebo.network;

import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public interface MessageProvider<T> {

    Class<?> getMsgClass();

    void write(T var1, FriendlyByteBuf var2);

    T read(FriendlyByteBuf var1);

    void handle(T var1, Supplier<NetworkEvent.Context> var2);

    default Optional<NetworkDirection> getNetworkDirection() {
        return Optional.empty();
    }
}