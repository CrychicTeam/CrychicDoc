package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundJigsawGeneratePacket implements Packet<ServerGamePacketListener> {

    private final BlockPos pos;

    private final int levels;

    private final boolean keepJigsaws;

    public ServerboundJigsawGeneratePacket(BlockPos blockPos0, int int1, boolean boolean2) {
        this.pos = blockPos0;
        this.levels = int1;
        this.keepJigsaws = boolean2;
    }

    public ServerboundJigsawGeneratePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.pos = friendlyByteBuf0.readBlockPos();
        this.levels = friendlyByteBuf0.readVarInt();
        this.keepJigsaws = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeVarInt(this.levels);
        friendlyByteBuf0.writeBoolean(this.keepJigsaws);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleJigsawGenerate(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int levels() {
        return this.levels;
    }

    public boolean keepJigsaws() {
        return this.keepJigsaws;
    }
}