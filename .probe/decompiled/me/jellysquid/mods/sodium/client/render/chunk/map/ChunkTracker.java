package me.jellysquid.mods.sodium.client.render.chunk.map;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongCollection;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import net.minecraft.world.level.ChunkPos;

public class ChunkTracker implements ClientChunkEventListener {

    private final Long2IntOpenHashMap chunkStatus = new Long2IntOpenHashMap();

    private final LongOpenHashSet chunkReady = new LongOpenHashSet();

    private final LongSet unloadQueue = new LongOpenHashSet();

    private final LongSet loadQueue = new LongOpenHashSet();

    @Override
    public void updateMapCenter(int chunkX, int chunkZ) {
    }

    @Override
    public void updateLoadDistance(int loadDistance) {
    }

    @Override
    public void onChunkStatusAdded(int x, int z, int flags) {
        long key = ChunkPos.asLong(x, z);
        int prev = this.chunkStatus.get(key);
        int cur = prev | flags;
        if (prev != cur) {
            this.chunkStatus.put(key, cur);
            this.updateNeighbors(x, z);
        }
    }

    @Override
    public void onChunkStatusRemoved(int x, int z, int flags) {
        long key = ChunkPos.asLong(x, z);
        int prev = this.chunkStatus.get(key);
        int cur = prev & ~flags;
        if (prev != cur) {
            if (cur == this.chunkStatus.defaultReturnValue()) {
                this.chunkStatus.remove(key);
            } else {
                this.chunkStatus.put(key, cur);
            }
            this.updateNeighbors(x, z);
        }
    }

    private void updateNeighbors(int x, int z) {
        for (int ox = -1; ox <= 1; ox++) {
            for (int oz = -1; oz <= 1; oz++) {
                this.updateMerged(ox + x, oz + z);
            }
        }
    }

    private void updateMerged(int x, int z) {
        long key = ChunkPos.asLong(x, z);
        int flags = this.chunkStatus.get(key);
        for (int ox = -1; ox <= 1; ox++) {
            for (int oz = -1; oz <= 1; oz++) {
                flags &= this.chunkStatus.get(ChunkPos.asLong(ox + x, oz + z));
            }
        }
        if (flags == 3) {
            if (this.chunkReady.add(key) && !this.unloadQueue.remove(key)) {
                this.loadQueue.add(key);
            }
        } else if (this.chunkReady.remove(key) && !this.loadQueue.remove(key)) {
            this.unloadQueue.add(key);
        }
    }

    public LongCollection getReadyChunks() {
        return LongSets.unmodifiable(this.chunkReady);
    }

    public void forEachEvent(ChunkTracker.ChunkEventHandler loadEventHandler, ChunkTracker.ChunkEventHandler unloadEventHandler) {
        forEachChunk(this.unloadQueue, unloadEventHandler);
        this.unloadQueue.clear();
        forEachChunk(this.loadQueue, loadEventHandler);
        this.loadQueue.clear();
    }

    public static void forEachChunk(LongCollection queue, ChunkTracker.ChunkEventHandler handler) {
        LongIterator iterator = queue.iterator();
        while (iterator.hasNext()) {
            long pos = iterator.nextLong();
            int x = ChunkPos.getX(pos);
            int z = ChunkPos.getZ(pos);
            handler.apply(x, z);
        }
    }

    public interface ChunkEventHandler {

        void apply(int var1, int var2);
    }
}