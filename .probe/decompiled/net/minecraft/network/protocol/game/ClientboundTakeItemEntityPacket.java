package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundTakeItemEntityPacket implements Packet<ClientGamePacketListener> {

    private final int itemId;

    private final int playerId;

    private final int amount;

    public ClientboundTakeItemEntityPacket(int int0, int int1, int int2) {
        this.itemId = int0;
        this.playerId = int1;
        this.amount = int2;
    }

    public ClientboundTakeItemEntityPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.itemId = friendlyByteBuf0.readVarInt();
        this.playerId = friendlyByteBuf0.readVarInt();
        this.amount = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.itemId);
        friendlyByteBuf0.writeVarInt(this.playerId);
        friendlyByteBuf0.writeVarInt(this.amount);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleTakeItemEntity(this);
    }

    public int getItemId() {
        return this.itemId;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public int getAmount() {
        return this.amount;
    }
}