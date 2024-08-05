package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundHorseScreenOpenPacket implements Packet<ClientGamePacketListener> {

    private final int containerId;

    private final int size;

    private final int entityId;

    public ClientboundHorseScreenOpenPacket(int int0, int int1, int int2) {
        this.containerId = int0;
        this.size = int1;
        this.entityId = int2;
    }

    public ClientboundHorseScreenOpenPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readUnsignedByte();
        this.size = friendlyByteBuf0.readVarInt();
        this.entityId = friendlyByteBuf0.readInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.containerId);
        friendlyByteBuf0.writeVarInt(this.size);
        friendlyByteBuf0.writeInt(this.entityId);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleHorseScreenOpen(this);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public int getSize() {
        return this.size;
    }

    public int getEntityId() {
        return this.entityId;
    }
}