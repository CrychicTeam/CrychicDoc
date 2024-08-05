package net.minecraft.world.level.chunk.storage;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import javax.annotation.Nullable;
import net.minecraft.FileUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.util.ExceptionCollector;
import net.minecraft.world.level.ChunkPos;

public final class RegionFileStorage implements AutoCloseable {

    public static final String ANVIL_EXTENSION = ".mca";

    private static final int MAX_CACHE_SIZE = 256;

    private final Long2ObjectLinkedOpenHashMap<RegionFile> regionCache = new Long2ObjectLinkedOpenHashMap();

    private final Path folder;

    private final boolean sync;

    RegionFileStorage(Path path0, boolean boolean1) {
        this.folder = path0;
        this.sync = boolean1;
    }

    private RegionFile getRegionFile(ChunkPos chunkPos0) throws IOException {
        long $$1 = ChunkPos.asLong(chunkPos0.getRegionX(), chunkPos0.getRegionZ());
        RegionFile $$2 = (RegionFile) this.regionCache.getAndMoveToFirst($$1);
        if ($$2 != null) {
            return $$2;
        } else {
            if (this.regionCache.size() >= 256) {
                ((RegionFile) this.regionCache.removeLast()).close();
            }
            FileUtil.createDirectoriesSafe(this.folder);
            Path $$3 = this.folder.resolve("r." + chunkPos0.getRegionX() + "." + chunkPos0.getRegionZ() + ".mca");
            RegionFile $$4 = new RegionFile($$3, this.folder, this.sync);
            this.regionCache.putAndMoveToFirst($$1, $$4);
            return $$4;
        }
    }

    @Nullable
    public CompoundTag read(ChunkPos chunkPos0) throws IOException {
        RegionFile $$1 = this.getRegionFile(chunkPos0);
        DataInputStream $$2 = $$1.getChunkDataInputStream(chunkPos0);
        CompoundTag var8;
        label43: {
            try {
                if ($$2 == null) {
                    var8 = null;
                    break label43;
                }
                var8 = NbtIo.read($$2);
            } catch (Throwable var7) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if ($$2 != null) {
                $$2.close();
            }
            return var8;
        }
        if ($$2 != null) {
            $$2.close();
        }
        return var8;
    }

    public void scanChunk(ChunkPos chunkPos0, StreamTagVisitor streamTagVisitor1) throws IOException {
        RegionFile $$2 = this.getRegionFile(chunkPos0);
        DataInputStream $$3 = $$2.getChunkDataInputStream(chunkPos0);
        try {
            if ($$3 != null) {
                NbtIo.parse($$3, streamTagVisitor1);
            }
        } catch (Throwable var8) {
            if ($$3 != null) {
                try {
                    $$3.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }
            throw var8;
        }
        if ($$3 != null) {
            $$3.close();
        }
    }

    protected void write(ChunkPos chunkPos0, @Nullable CompoundTag compoundTag1) throws IOException {
        RegionFile $$2 = this.getRegionFile(chunkPos0);
        if (compoundTag1 == null) {
            $$2.clear(chunkPos0);
        } else {
            DataOutputStream $$3 = $$2.getChunkDataOutputStream(chunkPos0);
            try {
                NbtIo.write(compoundTag1, $$3);
            } catch (Throwable var8) {
                if ($$3 != null) {
                    try {
                        $$3.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            if ($$3 != null) {
                $$3.close();
            }
        }
    }

    public void close() throws IOException {
        ExceptionCollector<IOException> $$0 = new ExceptionCollector();
        ObjectIterator var2 = this.regionCache.values().iterator();
        while (var2.hasNext()) {
            RegionFile $$1 = (RegionFile) var2.next();
            try {
                $$1.close();
            } catch (IOException var5) {
                $$0.add(var5);
            }
        }
        $$0.throwIfPresent();
    }

    public void flush() throws IOException {
        ObjectIterator var1 = this.regionCache.values().iterator();
        while (var1.hasNext()) {
            RegionFile $$0 = (RegionFile) var1.next();
            $$0.flush();
        }
    }
}