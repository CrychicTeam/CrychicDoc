package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundEntityTagQuery implements Packet<ServerGamePacketListener> {

    private final int transactionId;

    private final int entityId;

    public ServerboundEntityTagQuery(int int0, int int1) {
        this.transactionId = int0;
        this.entityId = int1;
    }

    public ServerboundEntityTagQuery(FriendlyByteBuf friendlyByteBuf0) {
        this.transactionId = friendlyByteBuf0.readVarInt();
        this.entityId = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.transactionId);
        friendlyByteBuf0.writeVarInt(this.entityId);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleEntityTagQuery(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public int getEntityId() {
        return this.entityId;
    }
}