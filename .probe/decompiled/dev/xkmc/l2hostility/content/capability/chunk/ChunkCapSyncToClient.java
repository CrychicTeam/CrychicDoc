package dev.xkmc.l2hostility.content.capability.chunk;

import dev.xkmc.l2hostility.init.network.ClientSyncHandler;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class ChunkCapSyncToClient extends SerialPacketBase {

    @SerialField
    public CompoundTag tag;

    @SerialField
    public ResourceLocation level;

    @SerialField
    public int x;

    @SerialField
    public int z;

    @Deprecated
    public ChunkCapSyncToClient() {
    }

    public ChunkCapSyncToClient(ChunkDifficulty chunk) {
        this.level = chunk.chunk.getLevel().dimension().location();
        this.x = chunk.chunk.m_7697_().x;
        this.z = chunk.chunk.m_7697_().z;
        this.tag = TagCodec.toTag(new CompoundTag(), chunk);
    }

    public void handle(NetworkEvent.Context context) {
        ClientSyncHandler.handleChunkUpdate(this.level, this.x, this.z, this.tag);
    }
}