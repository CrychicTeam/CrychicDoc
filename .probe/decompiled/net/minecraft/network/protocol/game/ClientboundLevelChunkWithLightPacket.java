package net.minecraft.network.protocol.game;

import java.util.BitSet;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.lighting.LevelLightEngine;

public class ClientboundLevelChunkWithLightPacket implements Packet<ClientGamePacketListener> {

    private final int x;

    private final int z;

    private final ClientboundLevelChunkPacketData chunkData;

    private final ClientboundLightUpdatePacketData lightData;

    public ClientboundLevelChunkWithLightPacket(LevelChunk levelChunk0, LevelLightEngine levelLightEngine1, @Nullable BitSet bitSet2, @Nullable BitSet bitSet3) {
        ChunkPos $$4 = levelChunk0.m_7697_();
        this.x = $$4.x;
        this.z = $$4.z;
        this.chunkData = new ClientboundLevelChunkPacketData(levelChunk0);
        this.lightData = new ClientboundLightUpdatePacketData($$4, levelLightEngine1, bitSet2, bitSet3);
    }

    public ClientboundLevelChunkWithLightPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.x = friendlyByteBuf0.readInt();
        this.z = friendlyByteBuf0.readInt();
        this.chunkData = new ClientboundLevelChunkPacketData(friendlyByteBuf0, this.x, this.z);
        this.lightData = new ClientboundLightUpdatePacketData(friendlyByteBuf0, this.x, this.z);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeInt(this.x);
        friendlyByteBuf0.writeInt(this.z);
        this.chunkData.write(friendlyByteBuf0);
        this.lightData.write(friendlyByteBuf0);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleLevelChunkWithLight(this);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public ClientboundLevelChunkPacketData getChunkData() {
        return this.chunkData;
    }

    public ClientboundLightUpdatePacketData getLightData() {
        return this.lightData;
    }
}