package net.minecraft.client.multiplayer;

import com.mojang.logging.LogUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.slf4j.Logger;

public class ClientChunkCache extends ChunkSource {

    static final Logger LOGGER = LogUtils.getLogger();

    private final LevelChunk emptyChunk;

    private final LevelLightEngine lightEngine;

    volatile ClientChunkCache.Storage storage;

    final ClientLevel level;

    public ClientChunkCache(ClientLevel clientLevel0, int int1) {
        this.level = clientLevel0;
        this.emptyChunk = new EmptyLevelChunk(clientLevel0, new ChunkPos(0, 0), clientLevel0.m_9598_().registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.PLAINS));
        this.lightEngine = new LevelLightEngine(this, true, clientLevel0.m_6042_().hasSkyLight());
        this.storage = new ClientChunkCache.Storage(calculateStorageRange(int1));
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.lightEngine;
    }

    private static boolean isValidChunk(@Nullable LevelChunk levelChunk0, int int1, int int2) {
        if (levelChunk0 == null) {
            return false;
        } else {
            ChunkPos $$3 = levelChunk0.m_7697_();
            return $$3.x == int1 && $$3.z == int2;
        }
    }

    public void drop(int int0, int int1) {
        if (this.storage.inRange(int0, int1)) {
            int $$2 = this.storage.getIndex(int0, int1);
            LevelChunk $$3 = this.storage.getChunk($$2);
            if (isValidChunk($$3, int0, int1)) {
                this.storage.replace($$2, $$3, null);
            }
        }
    }

    @Nullable
    public LevelChunk getChunk(int int0, int int1, ChunkStatus chunkStatus2, boolean boolean3) {
        if (this.storage.inRange(int0, int1)) {
            LevelChunk $$4 = this.storage.getChunk(this.storage.getIndex(int0, int1));
            if (isValidChunk($$4, int0, int1)) {
                return $$4;
            }
        }
        return boolean3 ? this.emptyChunk : null;
    }

    @Override
    public BlockGetter getLevel() {
        return this.level;
    }

    public void replaceBiomes(int int0, int int1, FriendlyByteBuf friendlyByteBuf2) {
        if (!this.storage.inRange(int0, int1)) {
            LOGGER.warn("Ignoring chunk since it's not in the view range: {}, {}", int0, int1);
        } else {
            int $$3 = this.storage.getIndex(int0, int1);
            LevelChunk $$4 = (LevelChunk) this.storage.chunks.get($$3);
            if (!isValidChunk($$4, int0, int1)) {
                LOGGER.warn("Ignoring chunk since it's not present: {}, {}", int0, int1);
            } else {
                $$4.replaceBiomes(friendlyByteBuf2);
            }
        }
    }

    @Nullable
    public LevelChunk replaceWithPacketData(int int0, int int1, FriendlyByteBuf friendlyByteBuf2, CompoundTag compoundTag3, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> consumerClientboundLevelChunkPacketDataBlockEntityTagOutput4) {
        if (!this.storage.inRange(int0, int1)) {
            LOGGER.warn("Ignoring chunk since it's not in the view range: {}, {}", int0, int1);
            return null;
        } else {
            int $$5 = this.storage.getIndex(int0, int1);
            LevelChunk $$6 = (LevelChunk) this.storage.chunks.get($$5);
            ChunkPos $$7 = new ChunkPos(int0, int1);
            if (!isValidChunk($$6, int0, int1)) {
                $$6 = new LevelChunk(this.level, $$7);
                $$6.replaceWithPacketData(friendlyByteBuf2, compoundTag3, consumerClientboundLevelChunkPacketDataBlockEntityTagOutput4);
                this.storage.replace($$5, $$6);
            } else {
                $$6.replaceWithPacketData(friendlyByteBuf2, compoundTag3, consumerClientboundLevelChunkPacketDataBlockEntityTagOutput4);
            }
            this.level.onChunkLoaded($$7);
            return $$6;
        }
    }

    @Override
    public void tick(BooleanSupplier booleanSupplier0, boolean boolean1) {
    }

    public void updateViewCenter(int int0, int int1) {
        this.storage.viewCenterX = int0;
        this.storage.viewCenterZ = int1;
    }

    public void updateViewRadius(int int0) {
        int $$1 = this.storage.chunkRadius;
        int $$2 = calculateStorageRange(int0);
        if ($$1 != $$2) {
            ClientChunkCache.Storage $$3 = new ClientChunkCache.Storage($$2);
            $$3.viewCenterX = this.storage.viewCenterX;
            $$3.viewCenterZ = this.storage.viewCenterZ;
            for (int $$4 = 0; $$4 < this.storage.chunks.length(); $$4++) {
                LevelChunk $$5 = (LevelChunk) this.storage.chunks.get($$4);
                if ($$5 != null) {
                    ChunkPos $$6 = $$5.m_7697_();
                    if ($$3.inRange($$6.x, $$6.z)) {
                        $$3.replace($$3.getIndex($$6.x, $$6.z), $$5);
                    }
                }
            }
            this.storage = $$3;
        }
    }

    private static int calculateStorageRange(int int0) {
        return Math.max(2, int0) + 3;
    }

    @Override
    public String gatherStats() {
        return this.storage.chunks.length() + ", " + this.getLoadedChunksCount();
    }

    @Override
    public int getLoadedChunksCount() {
        return this.storage.chunkCount;
    }

    @Override
    public void onLightUpdate(LightLayer lightLayer0, SectionPos sectionPos1) {
        Minecraft.getInstance().levelRenderer.setSectionDirty(sectionPos1.x(), sectionPos1.y(), sectionPos1.z());
    }

    final class Storage {

        final AtomicReferenceArray<LevelChunk> chunks;

        final int chunkRadius;

        private final int viewRange;

        volatile int viewCenterX;

        volatile int viewCenterZ;

        int chunkCount;

        Storage(int int0) {
            this.chunkRadius = int0;
            this.viewRange = int0 * 2 + 1;
            this.chunks = new AtomicReferenceArray(this.viewRange * this.viewRange);
        }

        int getIndex(int int0, int int1) {
            return Math.floorMod(int1, this.viewRange) * this.viewRange + Math.floorMod(int0, this.viewRange);
        }

        protected void replace(int int0, @Nullable LevelChunk levelChunk1) {
            LevelChunk $$2 = (LevelChunk) this.chunks.getAndSet(int0, levelChunk1);
            if ($$2 != null) {
                this.chunkCount--;
                ClientChunkCache.this.level.unload($$2);
            }
            if (levelChunk1 != null) {
                this.chunkCount++;
            }
        }

        protected LevelChunk replace(int int0, LevelChunk levelChunk1, @Nullable LevelChunk levelChunk2) {
            if (this.chunks.compareAndSet(int0, levelChunk1, levelChunk2) && levelChunk2 == null) {
                this.chunkCount--;
            }
            ClientChunkCache.this.level.unload(levelChunk1);
            return levelChunk1;
        }

        boolean inRange(int int0, int int1) {
            return Math.abs(int0 - this.viewCenterX) <= this.chunkRadius && Math.abs(int1 - this.viewCenterZ) <= this.chunkRadius;
        }

        @Nullable
        protected LevelChunk getChunk(int int0) {
            return (LevelChunk) this.chunks.get(int0);
        }

        private void dumpChunks(String string0) {
            try {
                FileOutputStream $$1 = new FileOutputStream(string0);
                try {
                    int $$2 = ClientChunkCache.this.storage.chunkRadius;
                    for (int $$3 = this.viewCenterZ - $$2; $$3 <= this.viewCenterZ + $$2; $$3++) {
                        for (int $$4 = this.viewCenterX - $$2; $$4 <= this.viewCenterX + $$2; $$4++) {
                            LevelChunk $$5 = (LevelChunk) ClientChunkCache.this.storage.chunks.get(ClientChunkCache.this.storage.getIndex($$4, $$3));
                            if ($$5 != null) {
                                ChunkPos $$6 = $$5.m_7697_();
                                $$1.write(($$6.x + "\t" + $$6.z + "\t" + $$5.isEmpty() + "\n").getBytes(StandardCharsets.UTF_8));
                            }
                        }
                    }
                } catch (Throwable var9) {
                    try {
                        $$1.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                    throw var9;
                }
                $$1.close();
            } catch (IOException var10) {
                ClientChunkCache.LOGGER.error("Failed to dump chunks to file {}", string0, var10);
            }
        }
    }
}