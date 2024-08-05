package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundTagQueryPacket implements Packet<ClientGamePacketListener> {

    private final int transactionId;

    @Nullable
    private final CompoundTag tag;

    public ClientboundTagQueryPacket(int int0, @Nullable CompoundTag compoundTag1) {
        this.transactionId = int0;
        this.tag = compoundTag1;
    }

    public ClientboundTagQueryPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.transactionId = friendlyByteBuf0.readVarInt();
        this.tag = friendlyByteBuf0.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.transactionId);
        friendlyByteBuf0.writeNbt(this.tag);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleTagQueryPacket(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    @Nullable
    public CompoundTag getTag() {
        return this.tag;
    }

    @Override
    public boolean isSkippable() {
        return true;
    }
}