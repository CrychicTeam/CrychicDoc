package net.minecraft.world.level.chunk.storage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.OptionalDynamic;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import org.slf4j.Logger;

public class SectionStorage<R> implements AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String SECTIONS_TAG = "Sections";

    private final IOWorker worker;

    private final Long2ObjectMap<Optional<R>> storage = new Long2ObjectOpenHashMap();

    private final LongLinkedOpenHashSet dirty = new LongLinkedOpenHashSet();

    private final Function<Runnable, Codec<R>> codec;

    private final Function<Runnable, R> factory;

    private final DataFixer fixerUpper;

    private final DataFixTypes type;

    private final RegistryAccess registryAccess;

    protected final LevelHeightAccessor levelHeightAccessor;

    public SectionStorage(Path path0, Function<Runnable, Codec<R>> functionRunnableCodecR1, Function<Runnable, R> functionRunnableR2, DataFixer dataFixer3, DataFixTypes dataFixTypes4, boolean boolean5, RegistryAccess registryAccess6, LevelHeightAccessor levelHeightAccessor7) {
        this.codec = functionRunnableCodecR1;
        this.factory = functionRunnableR2;
        this.fixerUpper = dataFixer3;
        this.type = dataFixTypes4;
        this.registryAccess = registryAccess6;
        this.levelHeightAccessor = levelHeightAccessor7;
        this.worker = new IOWorker(path0, boolean5, path0.getFileName().toString());
    }

    protected void tick(BooleanSupplier booleanSupplier0) {
        while (this.hasWork() && booleanSupplier0.getAsBoolean()) {
            ChunkPos $$1 = SectionPos.of(this.dirty.firstLong()).chunk();
            this.writeColumn($$1);
        }
    }

    public boolean hasWork() {
        return !this.dirty.isEmpty();
    }

    @Nullable
    protected Optional<R> get(long long0) {
        return (Optional<R>) this.storage.get(long0);
    }

    protected Optional<R> getOrLoad(long long0) {
        if (this.outsideStoredRange(long0)) {
            return Optional.empty();
        } else {
            Optional<R> $$1 = this.get(long0);
            if ($$1 != null) {
                return $$1;
            } else {
                this.readColumn(SectionPos.of(long0).chunk());
                $$1 = this.get(long0);
                if ($$1 == null) {
                    throw (IllegalStateException) Util.pauseInIde(new IllegalStateException());
                } else {
                    return $$1;
                }
            }
        }
    }

    protected boolean outsideStoredRange(long long0) {
        int $$1 = SectionPos.sectionToBlockCoord(SectionPos.y(long0));
        return this.levelHeightAccessor.isOutsideBuildHeight($$1);
    }

    protected R getOrCreate(long long0) {
        if (this.outsideStoredRange(long0)) {
            throw (IllegalArgumentException) Util.pauseInIde(new IllegalArgumentException("sectionPos out of bounds"));
        } else {
            Optional<R> $$1 = this.getOrLoad(long0);
            if ($$1.isPresent()) {
                return (R) $$1.get();
            } else {
                R $$2 = (R) this.factory.apply((Runnable) () -> this.setDirty(long0));
                this.storage.put(long0, Optional.of($$2));
                return $$2;
            }
        }
    }

    private void readColumn(ChunkPos chunkPos0) {
        Optional<CompoundTag> $$1 = (Optional<CompoundTag>) this.tryRead(chunkPos0).join();
        RegistryOps<Tag> $$2 = RegistryOps.create(NbtOps.INSTANCE, this.registryAccess);
        this.readColumn(chunkPos0, $$2, (Tag) $$1.orElse(null));
    }

    private CompletableFuture<Optional<CompoundTag>> tryRead(ChunkPos chunkPos0) {
        return this.worker.loadAsync(chunkPos0).exceptionally(p_223526_ -> {
            if (p_223526_ instanceof IOException $$2) {
                LOGGER.error("Error reading chunk {} data from disk", chunkPos0, $$2);
                return Optional.empty();
            } else {
                throw new CompletionException(p_223526_);
            }
        });
    }

    private <T> void readColumn(ChunkPos chunkPos0, DynamicOps<T> dynamicOpsT1, @Nullable T t2) {
        if (t2 == null) {
            for (int $$3 = this.levelHeightAccessor.getMinSection(); $$3 < this.levelHeightAccessor.getMaxSection(); $$3++) {
                this.storage.put(getKey(chunkPos0, $$3), Optional.empty());
            }
        } else {
            Dynamic<T> $$4 = new Dynamic(dynamicOpsT1, t2);
            int $$5 = getVersion($$4);
            int $$6 = SharedConstants.getCurrentVersion().getDataVersion().getVersion();
            boolean $$7 = $$5 != $$6;
            Dynamic<T> $$8 = this.type.update(this.fixerUpper, $$4, $$5, $$6);
            OptionalDynamic<T> $$9 = $$8.get("Sections");
            for (int $$10 = this.levelHeightAccessor.getMinSection(); $$10 < this.levelHeightAccessor.getMaxSection(); $$10++) {
                long $$11 = getKey(chunkPos0, $$10);
                Optional<R> $$12 = $$9.get(Integer.toString($$10)).result().flatMap(p_223519_ -> ((Codec) this.codec.apply((Runnable) () -> this.setDirty($$11))).parse(p_223519_).resultOrPartial(LOGGER::error));
                this.storage.put($$11, $$12);
                $$12.ifPresent(p_223523_ -> {
                    this.onSectionLoad($$11);
                    if ($$7) {
                        this.setDirty($$11);
                    }
                });
            }
        }
    }

    private void writeColumn(ChunkPos chunkPos0) {
        RegistryOps<Tag> $$1 = RegistryOps.create(NbtOps.INSTANCE, this.registryAccess);
        Dynamic<Tag> $$2 = this.writeColumn(chunkPos0, $$1);
        Tag $$3 = (Tag) $$2.getValue();
        if ($$3 instanceof CompoundTag) {
            this.worker.store(chunkPos0, (CompoundTag) $$3);
        } else {
            LOGGER.error("Expected compound tag, got {}", $$3);
        }
    }

    private <T> Dynamic<T> writeColumn(ChunkPos chunkPos0, DynamicOps<T> dynamicOpsT1) {
        Map<T, T> $$2 = Maps.newHashMap();
        for (int $$3 = this.levelHeightAccessor.getMinSection(); $$3 < this.levelHeightAccessor.getMaxSection(); $$3++) {
            long $$4 = getKey(chunkPos0, $$3);
            this.dirty.remove($$4);
            Optional<R> $$5 = (Optional<R>) this.storage.get($$4);
            if ($$5 != null && $$5.isPresent()) {
                DataResult<T> $$6 = ((Codec) this.codec.apply((Runnable) () -> this.setDirty($$4))).encodeStart(dynamicOpsT1, $$5.get());
                String $$7 = Integer.toString($$3);
                $$6.resultOrPartial(LOGGER::error).ifPresent(p_223531_ -> $$2.put(dynamicOpsT1.createString($$7), p_223531_));
            }
        }
        return new Dynamic(dynamicOpsT1, dynamicOpsT1.createMap(ImmutableMap.of(dynamicOpsT1.createString("Sections"), dynamicOpsT1.createMap($$2), dynamicOpsT1.createString("DataVersion"), dynamicOpsT1.createInt(SharedConstants.getCurrentVersion().getDataVersion().getVersion()))));
    }

    private static long getKey(ChunkPos chunkPos0, int int1) {
        return SectionPos.asLong(chunkPos0.x, int1, chunkPos0.z);
    }

    protected void onSectionLoad(long long0) {
    }

    protected void setDirty(long long0) {
        Optional<R> $$1 = (Optional<R>) this.storage.get(long0);
        if ($$1 != null && $$1.isPresent()) {
            this.dirty.add(long0);
        } else {
            LOGGER.warn("No data for position: {}", SectionPos.of(long0));
        }
    }

    private static int getVersion(Dynamic<?> dynamic0) {
        return dynamic0.get("DataVersion").asInt(1945);
    }

    public void flush(ChunkPos chunkPos0) {
        if (this.hasWork()) {
            for (int $$1 = this.levelHeightAccessor.getMinSection(); $$1 < this.levelHeightAccessor.getMaxSection(); $$1++) {
                long $$2 = getKey(chunkPos0, $$1);
                if (this.dirty.contains($$2)) {
                    this.writeColumn(chunkPos0);
                    return;
                }
            }
        }
    }

    public void close() throws IOException {
        this.worker.close();
    }
}