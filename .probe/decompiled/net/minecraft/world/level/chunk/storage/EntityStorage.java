package net.minecraft.world.level.chunk.storage;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.entity.ChunkEntities;
import net.minecraft.world.level.entity.EntityPersistentStorage;
import org.slf4j.Logger;

public class EntityStorage implements EntityPersistentStorage<Entity> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String ENTITIES_TAG = "Entities";

    private static final String POSITION_TAG = "Position";

    private final ServerLevel level;

    private final IOWorker worker;

    private final LongSet emptyChunks = new LongOpenHashSet();

    private final ProcessorMailbox<Runnable> entityDeserializerQueue;

    protected final DataFixer fixerUpper;

    public EntityStorage(ServerLevel serverLevel0, Path path1, DataFixer dataFixer2, boolean boolean3, Executor executor4) {
        this.level = serverLevel0;
        this.fixerUpper = dataFixer2;
        this.entityDeserializerQueue = ProcessorMailbox.create(executor4, "entity-deserializer");
        this.worker = new IOWorker(path1, boolean3, "entities");
    }

    @Override
    public CompletableFuture<ChunkEntities<Entity>> loadEntities(ChunkPos chunkPos0) {
        return this.emptyChunks.contains(chunkPos0.toLong()) ? CompletableFuture.completedFuture(emptyChunk(chunkPos0)) : this.worker.loadAsync(chunkPos0).thenApplyAsync(p_223458_ -> {
            if (p_223458_.isEmpty()) {
                this.emptyChunks.add(chunkPos0.toLong());
                return emptyChunk(chunkPos0);
            } else {
                try {
                    ChunkPos $$2 = readChunkPos((CompoundTag) p_223458_.get());
                    if (!Objects.equals(chunkPos0, $$2)) {
                        LOGGER.error("Chunk file at {} is in the wrong location. (Expected {}, got {})", new Object[] { chunkPos0, chunkPos0, $$2 });
                    }
                } catch (Exception var6) {
                    LOGGER.warn("Failed to parse chunk {} position info", chunkPos0, var6);
                }
                CompoundTag $$4 = this.upgradeChunkTag((CompoundTag) p_223458_.get());
                ListTag $$5 = $$4.getList("Entities", 10);
                List<Entity> $$6 = (List<Entity>) EntityType.loadEntitiesRecursive($$5, this.level).collect(ImmutableList.toImmutableList());
                return new ChunkEntities(chunkPos0, $$6);
            }
        }, this.entityDeserializerQueue::m_6937_);
    }

    private static ChunkPos readChunkPos(CompoundTag compoundTag0) {
        int[] $$1 = compoundTag0.getIntArray("Position");
        return new ChunkPos($$1[0], $$1[1]);
    }

    private static void writeChunkPos(CompoundTag compoundTag0, ChunkPos chunkPos1) {
        compoundTag0.put("Position", new IntArrayTag(new int[] { chunkPos1.x, chunkPos1.z }));
    }

    private static ChunkEntities<Entity> emptyChunk(ChunkPos chunkPos0) {
        return new ChunkEntities<>(chunkPos0, ImmutableList.of());
    }

    @Override
    public void storeEntities(ChunkEntities<Entity> chunkEntitiesEntity0) {
        ChunkPos $$1 = chunkEntitiesEntity0.getPos();
        if (chunkEntitiesEntity0.isEmpty()) {
            if (this.emptyChunks.add($$1.toLong())) {
                this.worker.store($$1, null);
            }
        } else {
            ListTag $$2 = new ListTag();
            chunkEntitiesEntity0.getEntities().forEach(p_156567_ -> {
                CompoundTag $$2x = new CompoundTag();
                if (p_156567_.save($$2x)) {
                    $$2.add($$2x);
                }
            });
            CompoundTag $$3 = NbtUtils.addCurrentDataVersion(new CompoundTag());
            $$3.put("Entities", $$2);
            writeChunkPos($$3, $$1);
            this.worker.store($$1, $$3).exceptionally(p_156554_ -> {
                LOGGER.error("Failed to store chunk {}", $$1, p_156554_);
                return null;
            });
            this.emptyChunks.remove($$1.toLong());
        }
    }

    @Override
    public void flush(boolean boolean0) {
        this.worker.synchronize(boolean0).join();
        this.entityDeserializerQueue.runAll();
    }

    private CompoundTag upgradeChunkTag(CompoundTag compoundTag0) {
        int $$1 = NbtUtils.getDataVersion(compoundTag0, -1);
        return DataFixTypes.ENTITY_CHUNK.updateToCurrentVersion(this.fixerUpper, compoundTag0, $$1);
    }

    @Override
    public void close() throws IOException {
        this.worker.close();
    }
}