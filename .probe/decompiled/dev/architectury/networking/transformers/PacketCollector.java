package dev.architectury.networking.transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;

public class PacketCollector implements PacketSink {

    @Nullable
    private final Consumer<Packet<?>> consumer;

    private final List<Packet<?>> packets = new ArrayList();

    public PacketCollector(@Nullable Consumer<Packet<?>> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void accept(Packet<?> packet) {
        this.packets.add(packet);
        if (this.consumer != null) {
            this.consumer.accept(packet);
        }
    }

    public List<Packet<?>> collect() {
        return this.packets;
    }
}