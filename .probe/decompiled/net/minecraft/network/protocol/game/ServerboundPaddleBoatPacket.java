package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPaddleBoatPacket implements Packet<ServerGamePacketListener> {

    private final boolean left;

    private final boolean right;

    public ServerboundPaddleBoatPacket(boolean boolean0, boolean boolean1) {
        this.left = boolean0;
        this.right = boolean1;
    }

    public ServerboundPaddleBoatPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.left = friendlyByteBuf0.readBoolean();
        this.right = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBoolean(this.left);
        friendlyByteBuf0.writeBoolean(this.right);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handlePaddleBoat(this);
    }

    public boolean getLeft() {
        return this.left;
    }

    public boolean getRight() {
        return this.right;
    }
}