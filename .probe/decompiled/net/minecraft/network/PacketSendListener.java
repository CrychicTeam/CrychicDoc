package net.minecraft.network;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.Packet;

public interface PacketSendListener {

    static PacketSendListener thenRun(final Runnable runnable0) {
        return new PacketSendListener() {

            @Override
            public void onSuccess() {
                runnable0.run();
            }

            @Nullable
            @Override
            public Packet<?> onFailure() {
                runnable0.run();
                return null;
            }
        };
    }

    static PacketSendListener exceptionallySend(final Supplier<Packet<?>> supplierPacket0) {
        return new PacketSendListener() {

            @Nullable
            @Override
            public Packet<?> onFailure() {
                return (Packet<?>) supplierPacket0.get();
            }
        };
    }

    default void onSuccess() {
    }

    @Nullable
    default Packet<?> onFailure() {
        return null;
    }
}