package journeymap.common.nbt.cache;

import java.io.IOException;
import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

public class CacheStorage implements AutoCloseable {

    private final CacheWorker worker;

    public CacheStorage(Path path, boolean sync) {
        this.worker = new CacheWorker(path, sync);
    }

    @Nullable
    public CompoundTag read(ChunkPos chunkPos) {
        try {
            return this.worker.load(chunkPos);
        } catch (Exception var3) {
            return null;
        }
    }

    public void write(ChunkPos chunkPos, CompoundTag compoundTag) {
        this.worker.store(chunkPos, compoundTag);
    }

    public void flushWorker() {
        this.worker.synchronize(true).join();
    }

    public void close() throws IOException {
        this.worker.close();
    }
}