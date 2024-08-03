package dev.architectury.networking.simple;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public abstract class Message {

    Message() {
    }

    public abstract MessageType getType();

    public abstract void write(FriendlyByteBuf var1);

    public abstract void handle(NetworkManager.PacketContext var1);

    public final Packet<?> toPacket() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.write(buf);
        return NetworkManager.toPacket(this.getType().getSide(), this.getType().getId(), buf);
    }
}