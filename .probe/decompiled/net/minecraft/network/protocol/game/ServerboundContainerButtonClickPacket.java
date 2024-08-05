package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundContainerButtonClickPacket implements Packet<ServerGamePacketListener> {

    private final int containerId;

    private final int buttonId;

    public ServerboundContainerButtonClickPacket(int int0, int int1) {
        this.containerId = int0;
        this.buttonId = int1;
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleContainerButtonClick(this);
    }

    public ServerboundContainerButtonClickPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readByte();
        this.buttonId = friendlyByteBuf0.readByte();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.containerId);
        friendlyByteBuf0.writeByte(this.buttonId);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public int getButtonId() {
        return this.buttonId;
    }
}