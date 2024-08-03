package net.minecraft.network.protocol.game;

import java.util.BitSet;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.lighting.LevelLightEngine;

public class ClientboundLightUpdatePacket implements Packet<ClientGamePacketListener> {

    private final int x;

    private final int z;

    private final ClientboundLightUpdatePacketData lightData;

    public ClientboundLightUpdatePacket(ChunkPos chunkPos0, LevelLightEngine levelLightEngine1, @Nullable BitSet bitSet2, @Nullable BitSet bitSet3) {
        this.x = chunkPos0.x;
        this.z = chunkPos0.z;
        this.lightData = new ClientboundLightUpdatePacketData(chunkPos0, levelLightEngine1, bitSet2, bitSet3);
    }

    public ClientboundLightUpdatePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.x = friendlyByteBuf0.readVarInt();
        this.z = friendlyByteBuf0.readVarInt();
        this.lightData = new ClientboundLightUpdatePacketData(friendlyByteBuf0, this.x, this.z);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.x);
        friendlyByteBuf0.writeVarInt(this.z);
        this.lightData.write(friendlyByteBuf0);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleLightUpdatePacket(this);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public ClientboundLightUpdatePacketData getLightData() {
        return this.lightData;
    }
}