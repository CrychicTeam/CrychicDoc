package net.minecraft.network.protocol.status;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundStatusRequestPacket implements Packet<ServerStatusPacketListener> {

    public ServerboundStatusRequestPacket() {
    }

    public ServerboundStatusRequestPacket(FriendlyByteBuf friendlyByteBuf0) {
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
    }

    public void handle(ServerStatusPacketListener serverStatusPacketListener0) {
        serverStatusPacketListener0.handleStatusRequest(this);
    }
}