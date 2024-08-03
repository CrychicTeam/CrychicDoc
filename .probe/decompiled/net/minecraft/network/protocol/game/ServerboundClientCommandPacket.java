package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundClientCommandPacket implements Packet<ServerGamePacketListener> {

    private final ServerboundClientCommandPacket.Action action;

    public ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action serverboundClientCommandPacketAction0) {
        this.action = serverboundClientCommandPacketAction0;
    }

    public ServerboundClientCommandPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.action = friendlyByteBuf0.readEnum(ServerboundClientCommandPacket.Action.class);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.action);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleClientCommand(this);
    }

    public ServerboundClientCommandPacket.Action getAction() {
        return this.action;
    }

    public static enum Action {

        PERFORM_RESPAWN, REQUEST_STATS
    }
}