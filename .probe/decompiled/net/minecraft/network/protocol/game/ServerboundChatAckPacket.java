package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ServerboundChatAckPacket(int f_244085_) implements Packet<ServerGamePacketListener> {

    private final int offset;

    public ServerboundChatAckPacket(FriendlyByteBuf p_242339_) {
        this(p_242339_.readVarInt());
    }

    public ServerboundChatAckPacket(int f_244085_) {
        this.offset = f_244085_;
    }

    @Override
    public void write(FriendlyByteBuf p_242345_) {
        p_242345_.writeVarInt(this.offset);
    }

    public void handle(ServerGamePacketListener p_242391_) {
        p_242391_.handleChatAck(this);
    }
}