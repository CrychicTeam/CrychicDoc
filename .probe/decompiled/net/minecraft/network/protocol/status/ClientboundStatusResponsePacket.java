package net.minecraft.network.protocol.status;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundStatusResponsePacket(ServerStatus f_134886_) implements Packet<ClientStatusPacketListener> {

    private final ServerStatus status;

    public ClientboundStatusResponsePacket(FriendlyByteBuf p_179834_) {
        this(p_179834_.readJsonWithCodec(ServerStatus.CODEC));
    }

    public ClientboundStatusResponsePacket(ServerStatus f_134886_) {
        this.status = f_134886_;
    }

    @Override
    public void write(FriendlyByteBuf p_134899_) {
        p_134899_.writeJsonWithCodec(ServerStatus.CODEC, this.status);
    }

    public void handle(ClientStatusPacketListener p_134896_) {
        p_134896_.handleStatusResponse(this);
    }
}