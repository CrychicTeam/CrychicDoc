package dev.architectury.networking.simple;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;

@FunctionalInterface
public interface MessageDecoder<T extends Message> {

    T decode(FriendlyByteBuf var1);

    default NetworkManager.NetworkReceiver createReceiver() {
        return (buf, context) -> {
            Message packet = this.decode(buf);
            context.queue(() -> packet.handle(context));
        };
    }
}