package net.minecraft.world.level.chunk;

import java.io.IOException;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.lighting.LevelLightEngine;

public abstract class ChunkSource implements LightChunkGetter, AutoCloseable {

    @Nullable
    public LevelChunk getChunk(int int0, int int1, boolean boolean2) {
        return (LevelChunk) this.getChunk(int0, int1, ChunkStatus.FULL, boolean2);
    }

    @Nullable
    public LevelChunk getChunkNow(int int0, int int1) {
        return this.getChunk(int0, int1, false);
    }

    @Nullable
    @Override
    public LightChunk getChunkForLighting(int int0, int int1) {
        return this.getChunk(int0, int1, ChunkStatus.EMPTY, false);
    }

    public boolean hasChunk(int int0, int int1) {
        return this.getChunk(int0, int1, ChunkStatus.FULL, false) != null;
    }

    @Nullable
    public abstract ChunkAccess getChunk(int var1, int var2, ChunkStatus var3, boolean var4);

    public abstract void tick(BooleanSupplier var1, boolean var2);

    public abstract String gatherStats();

    public abstract int getLoadedChunksCount();

    public void close() throws IOException {
    }

    public abstract LevelLightEngine getLightEngine();

    public void setSpawnSettings(boolean boolean0, boolean boolean1) {
    }

    public void updateChunkForced(ChunkPos chunkPos0, boolean boolean1) {
    }
}