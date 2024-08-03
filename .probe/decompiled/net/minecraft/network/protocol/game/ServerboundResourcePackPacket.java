package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundResourcePackPacket implements Packet<ServerGamePacketListener> {

    private final ServerboundResourcePackPacket.Action action;

    public ServerboundResourcePackPacket(ServerboundResourcePackPacket.Action serverboundResourcePackPacketAction0) {
        this.action = serverboundResourcePackPacketAction0;
    }

    public ServerboundResourcePackPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.action = friendlyByteBuf0.readEnum(ServerboundResourcePackPacket.Action.class);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.action);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleResourcePackResponse(this);
    }

    public ServerboundResourcePackPacket.Action getAction() {
        return this.action;
    }

    public static enum Action {

        SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED
    }
}