package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundContainerSetDataPacket implements Packet<ClientGamePacketListener> {

    private final int containerId;

    private final int id;

    private final int value;

    public ClientboundContainerSetDataPacket(int int0, int int1, int int2) {
        this.containerId = int0;
        this.id = int1;
        this.value = int2;
    }

    public ClientboundContainerSetDataPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readUnsignedByte();
        this.id = friendlyByteBuf0.readShort();
        this.value = friendlyByteBuf0.readShort();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.containerId);
        friendlyByteBuf0.writeShort(this.id);
        friendlyByteBuf0.writeShort(this.value);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleContainerSetData(this);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public int getId() {
        return this.id;
    }

    public int getValue() {
        return this.value;
    }
}