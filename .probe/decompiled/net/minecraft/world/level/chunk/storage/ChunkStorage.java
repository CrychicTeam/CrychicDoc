package net.minecraft.world.level.chunk.storage;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Codec;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.LegacyStructureDataHandler;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class ChunkStorage implements AutoCloseable {

    public static final int LAST_MONOLYTH_STRUCTURE_DATA_VERSION = 1493;

    private final IOWorker worker;

    protected final DataFixer fixerUpper;

    @Nullable
    private volatile LegacyStructureDataHandler legacyStructureHandler;

    public ChunkStorage(Path path0, DataFixer dataFixer1, boolean boolean2) {
        this.fixerUpper = dataFixer1;
        this.worker = new IOWorker(path0, boolean2, "chunk");
    }

    public boolean isOldChunkAround(ChunkPos chunkPos0, int int1) {
        return this.worker.isOldChunkAround(chunkPos0, int1);
    }

    public CompoundTag upgradeChunkTag(ResourceKey<Level> resourceKeyLevel0, Supplier<DimensionDataStorage> supplierDimensionDataStorage1, CompoundTag compoundTag2, Optional<ResourceKey<Codec<? extends ChunkGenerator>>> optionalResourceKeyCodecExtendsChunkGenerator3) {
        int $$4 = getVersion(compoundTag2);
        if ($$4 < 1493) {
            compoundTag2 = DataFixTypes.CHUNK.update(this.fixerUpper, compoundTag2, $$4, 1493);
            if (compoundTag2.getCompound("Level").getBoolean("hasLegacyStructureData")) {
                LegacyStructureDataHandler $$5 = this.getLegacyStructureHandler(resourceKeyLevel0, supplierDimensionDataStorage1);
                compoundTag2 = $$5.updateFromLegacy(compoundTag2);
            }
        }
        injectDatafixingContext(compoundTag2, resourceKeyLevel0, optionalResourceKeyCodecExtendsChunkGenerator3);
        compoundTag2 = DataFixTypes.CHUNK.updateToCurrentVersion(this.fixerUpper, compoundTag2, Math.max(1493, $$4));
        if ($$4 < SharedConstants.getCurrentVersion().getDataVersion().getVersion()) {
            NbtUtils.addCurrentDataVersion(compoundTag2);
        }
        compoundTag2.remove("__context");
        return compoundTag2;
    }

    private LegacyStructureDataHandler getLegacyStructureHandler(ResourceKey<Level> resourceKeyLevel0, Supplier<DimensionDataStorage> supplierDimensionDataStorage1) {
        LegacyStructureDataHandler $$2 = this.legacyStructureHandler;
        if ($$2 == null) {
            synchronized (this) {
                $$2 = this.legacyStructureHandler;
                if ($$2 == null) {
                    this.legacyStructureHandler = $$2 = LegacyStructureDataHandler.getLegacyStructureHandler(resourceKeyLevel0, (DimensionDataStorage) supplierDimensionDataStorage1.get());
                }
            }
        }
        return $$2;
    }

    public static void injectDatafixingContext(CompoundTag compoundTag0, ResourceKey<Level> resourceKeyLevel1, Optional<ResourceKey<Codec<? extends ChunkGenerator>>> optionalResourceKeyCodecExtendsChunkGenerator2) {
        CompoundTag $$3 = new CompoundTag();
        $$3.putString("dimension", resourceKeyLevel1.location().toString());
        optionalResourceKeyCodecExtendsChunkGenerator2.ifPresent(p_196917_ -> $$3.putString("generator", p_196917_.location().toString()));
        compoundTag0.put("__context", $$3);
    }

    public static int getVersion(CompoundTag compoundTag0) {
        return NbtUtils.getDataVersion(compoundTag0, -1);
    }

    public CompletableFuture<Optional<CompoundTag>> read(ChunkPos chunkPos0) {
        return this.worker.loadAsync(chunkPos0);
    }

    public void write(ChunkPos chunkPos0, CompoundTag compoundTag1) {
        this.worker.store(chunkPos0, compoundTag1);
        if (this.legacyStructureHandler != null) {
            this.legacyStructureHandler.removeIndex(chunkPos0.toLong());
        }
    }

    public void flushWorker() {
        this.worker.synchronize(true).join();
    }

    public void close() throws IOException {
        this.worker.close();
    }

    public ChunkScanAccess chunkScanner() {
        return this.worker;
    }
}