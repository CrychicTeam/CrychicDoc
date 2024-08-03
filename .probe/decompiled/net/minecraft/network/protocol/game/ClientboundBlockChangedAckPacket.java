package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundBlockChangedAckPacket(int f_237578_) implements Packet<ClientGamePacketListener> {

    private final int sequence;

    public ClientboundBlockChangedAckPacket(FriendlyByteBuf p_237582_) {
        this(p_237582_.readVarInt());
    }

    public ClientboundBlockChangedAckPacket(int f_237578_) {
        this.sequence = f_237578_;
    }

    @Override
    public void write(FriendlyByteBuf p_237584_) {
        p_237584_.writeVarInt(this.sequence);
    }

    public void handle(ClientGamePacketListener p_237588_) {
        p_237588_.handleBlockChangedAck(this);
    }
}