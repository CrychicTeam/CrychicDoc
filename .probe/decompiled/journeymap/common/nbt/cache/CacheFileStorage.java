package journeymap.common.nbt.cache;

import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import journeymap.common.Journeymap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.ExceptionCollector;
import net.minecraft.world.level.ChunkPos;

public class CacheFileStorage implements AutoCloseable {

    public static final String EXTENSION = ".mca";

    private static final int MAX_CACHE_SIZE = 256;

    private final Long2ObjectLinkedOpenHashMap<CacheFile> cache = new Long2ObjectLinkedOpenHashMap();

    private final Path folderPath;

    private final boolean sync;

    CacheFileStorage(Path folderPath, boolean sync) {
        this.folderPath = folderPath;
        this.sync = sync;
    }

    private CacheFile getCacheFile(ChunkPos chunkPos) throws IOException {
        long i = ChunkPos.asLong(chunkPos.getRegionX(), chunkPos.getRegionZ());
        CacheFile cacheFile = (CacheFile) this.cache.getAndMoveToFirst(i);
        if (cacheFile == null) {
            if (this.cache.size() >= 256) {
                ((CacheFile) this.cache.removeLast()).close();
            }
            Files.createDirectories(this.folderPath);
            Path path = this.folderPath.resolve("r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ() + ".mca");
            cacheFile = new CacheFile(path, this.folderPath, this.sync);
            this.cache.putAndMoveToFirst(i, cacheFile);
        }
        return cacheFile;
    }

    @Nullable
    public CompoundTag read(ChunkPos chunkPos) throws IOException {
        CacheFile cacheFile = this.getCacheFile(chunkPos);
        DataInputStream chunkDataInputStream = cacheFile.getChunkDataInputStream(chunkPos);
        CompoundTag tag;
        try {
            if (chunkDataInputStream == null) {
                return null;
            }
            tag = NbtIo.read(chunkDataInputStream);
        } catch (Throwable var8) {
            Journeymap.getLogger().error("Error reading chunk for ChunkPos {}", chunkPos);
            try {
                chunkDataInputStream.close();
            } catch (Throwable var7) {
                Journeymap.getLogger().error("Error closing stream on chunk error {}", chunkPos);
                var8.addSuppressed(var7);
            }
            return null;
        }
        chunkDataInputStream.close();
        return tag;
    }

    protected void write(ChunkPos chunkPos, @Nullable CompoundTag tag) throws IOException {
        CacheFile cacheFile = this.getCacheFile(chunkPos);
        if (tag == null) {
            cacheFile.clear(chunkPos);
        } else {
            DataOutputStream chunkDataInputStream = cacheFile.getChunkDataOutputStream(chunkPos);
            try {
                NbtIo.write(tag, chunkDataInputStream);
            } catch (Throwable var8) {
                try {
                    chunkDataInputStream.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            chunkDataInputStream.close();
        }
    }

    public void close() throws IOException {
        ExceptionCollector<IOException> collector = new ExceptionCollector();
        ObjectIterator var2 = this.cache.values().iterator();
        while (var2.hasNext()) {
            CacheFile cacheFile = (CacheFile) var2.next();
            try {
                cacheFile.close();
            } catch (IOException var5) {
                collector.add(var5);
            }
        }
        collector.throwIfPresent();
    }

    public void flush() throws IOException {
        ObjectIterator var1 = this.cache.values().iterator();
        while (var1.hasNext()) {
            CacheFile cacheFile = (CacheFile) var1.next();
            cacheFile.flush();
        }
    }
}