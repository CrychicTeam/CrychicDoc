package net.minecraft.server.level;

import net.minecraft.world.level.chunk.ChunkStatus;

public class ChunkLevel {

    private static final int FULL_CHUNK_LEVEL = 33;

    private static final int BLOCK_TICKING_LEVEL = 32;

    private static final int ENTITY_TICKING_LEVEL = 31;

    public static final int MAX_LEVEL = 33 + ChunkStatus.maxDistance();

    public static ChunkStatus generationStatus(int int0) {
        return int0 < 33 ? ChunkStatus.FULL : ChunkStatus.getStatusAroundFullChunk(int0 - 33);
    }

    public static int byStatus(ChunkStatus chunkStatus0) {
        return 33 + ChunkStatus.getDistance(chunkStatus0);
    }

    public static FullChunkStatus fullStatus(int int0) {
        if (int0 <= 31) {
            return FullChunkStatus.ENTITY_TICKING;
        } else if (int0 <= 32) {
            return FullChunkStatus.BLOCK_TICKING;
        } else {
            return int0 <= 33 ? FullChunkStatus.FULL : FullChunkStatus.INACCESSIBLE;
        }
    }

    public static int byStatus(FullChunkStatus fullChunkStatus0) {
        return switch(fullChunkStatus0) {
            case INACCESSIBLE ->
                MAX_LEVEL;
            case FULL ->
                33;
            case BLOCK_TICKING ->
                32;
            case ENTITY_TICKING ->
                31;
        };
    }

    public static boolean isEntityTicking(int int0) {
        return int0 <= 31;
    }

    public static boolean isBlockTicking(int int0) {
        return int0 <= 32;
    }

    public static boolean isLoaded(int int0) {
        return int0 <= MAX_LEVEL;
    }
}