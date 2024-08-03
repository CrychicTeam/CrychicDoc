package fuzs.puzzleslib.api.network.v3.serialization;

import net.minecraft.network.FriendlyByteBuf;

public interface MessageSerializer<T> {

    void write(FriendlyByteBuf var1, T var2);

    T read(FriendlyByteBuf var1);
}