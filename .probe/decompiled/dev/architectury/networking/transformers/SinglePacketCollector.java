package dev.architectury.networking.transformers;

import java.util.function.Consumer;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;

public class SinglePacketCollector implements PacketSink {

    @Nullable
    private final Consumer<Packet<?>> consumer;

    private Packet<?> packet;

    public SinglePacketCollector(@Nullable Consumer<Packet<?>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void accept(Packet<?> packet) {
        if (this.packet == null) {
            this.packet = packet;
            if (this.consumer != null) {
                this.consumer.accept(packet);
            }
        } else {
            throw new IllegalStateException("Already accepted one packet!");
        }
    }

    public Packet<?> getPacket() {
        return this.packet;
    }
}