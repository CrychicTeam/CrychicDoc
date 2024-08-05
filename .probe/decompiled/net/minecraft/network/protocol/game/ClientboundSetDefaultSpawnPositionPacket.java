package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetDefaultSpawnPositionPacket implements Packet<ClientGamePacketListener> {

    private final BlockPos pos;

    private final float angle;

    public ClientboundSetDefaultSpawnPositionPacket(BlockPos blockPos0, float float1) {
        this.pos = blockPos0;
        this.angle = float1;
    }

    public ClientboundSetDefaultSpawnPositionPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.pos = friendlyByteBuf0.readBlockPos();
        this.angle = friendlyByteBuf0.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeFloat(this.angle);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetSpawn(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public float getAngle() {
        return this.angle;
    }
}