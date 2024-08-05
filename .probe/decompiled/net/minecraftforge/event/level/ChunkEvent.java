package net.minecraftforge.event.level;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ChunkEvent extends LevelEvent {

    private final ChunkAccess chunk;

    public ChunkEvent(ChunkAccess chunk) {
        super(chunk.getWorldForge());
        this.chunk = chunk;
    }

    public ChunkEvent(ChunkAccess chunk, LevelAccessor level) {
        super(level);
        this.chunk = chunk;
    }

    public ChunkAccess getChunk() {
        return this.chunk;
    }

    public static class Load extends ChunkEvent {

        private final boolean newChunk;

        @Internal
        public Load(ChunkAccess chunk, boolean newChunk) {
            super(chunk);
            this.newChunk = newChunk;
        }

        public boolean isNewChunk() {
            return this.newChunk;
        }
    }

    public static class Unload extends ChunkEvent {

        public Unload(ChunkAccess chunk) {
            super(chunk);
        }
    }
}