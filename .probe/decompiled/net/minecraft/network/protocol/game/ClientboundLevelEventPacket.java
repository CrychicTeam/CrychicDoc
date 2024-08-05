package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundLevelEventPacket implements Packet<ClientGamePacketListener> {

    private final int type;

    private final BlockPos pos;

    private final int data;

    private final boolean globalEvent;

    public ClientboundLevelEventPacket(int int0, BlockPos blockPos1, int int2, boolean boolean3) {
        this.type = int0;
        this.pos = blockPos1.immutable();
        this.data = int2;
        this.globalEvent = boolean3;
    }

    public ClientboundLevelEventPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.type = friendlyByteBuf0.readInt();
        this.pos = friendlyByteBuf0.readBlockPos();
        this.data = friendlyByteBuf0.readInt();
        this.globalEvent = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeInt(this.type);
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeInt(this.data);
        friendlyByteBuf0.writeBoolean(this.globalEvent);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleLevelEvent(this);
    }

    public boolean isGlobalEvent() {
        return this.globalEvent;
    }

    public int getType() {
        return this.type;
    }

    public int getData() {
        return this.data;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}