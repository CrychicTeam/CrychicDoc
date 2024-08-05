package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundSelectTradePacket implements Packet<ServerGamePacketListener> {

    private final int item;

    public ServerboundSelectTradePacket(int int0) {
        this.item = int0;
    }

    public ServerboundSelectTradePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.item = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.item);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSelectTrade(this);
    }

    public int getItem() {
        return this.item;
    }
}