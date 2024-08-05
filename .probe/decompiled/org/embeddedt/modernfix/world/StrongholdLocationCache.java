package org.embeddedt.modernfix.world;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.saveddata.SavedData;

public class StrongholdLocationCache extends SavedData {

    private List<ChunkPos> chunkPosList = new ArrayList();

    public List<ChunkPos> getChunkPosList() {
        return new ArrayList(this.chunkPosList);
    }

    public void setChunkPosList(List<ChunkPos> positions) {
        this.chunkPosList = new ArrayList(positions);
        this.m_77762_();
    }

    public static StrongholdLocationCache load(CompoundTag arg) {
        StrongholdLocationCache cache = new StrongholdLocationCache();
        if (arg.contains("Positions", 12)) {
            long[] positions = arg.getLongArray("Positions");
            for (long position : positions) {
                cache.chunkPosList.add(new ChunkPos(position));
            }
        }
        return cache;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        long[] serialized = new long[this.chunkPosList.size()];
        for (int i = 0; i < this.chunkPosList.size(); i++) {
            ChunkPos thePos = (ChunkPos) this.chunkPosList.get(i);
            serialized[i] = thePos.toLong();
        }
        compoundTag.putLongArray("Positions", serialized);
        return compoundTag;
    }

    public static String getFileId(Holder<DimensionType> dimensionType) {
        return "mfix_strongholds";
    }
}