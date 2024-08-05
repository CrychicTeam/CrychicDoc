package noobanidus.mods.lootr.block.entities;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.LockCode;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.server.ServerLifecycleHooks;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.event.HandleChunk;

@EventBusSubscriber(modid = "lootr")
public class TileTicker {

    private static final Object listLock = new Object();

    private static final Object worldLock = new Object();

    private static final Set<TileTicker.Entry> tileEntries = new ObjectLinkedOpenHashSet();

    private static final Set<TileTicker.Entry> pendingEntries = new ObjectLinkedOpenHashSet();

    private static boolean tickingList = false;

    public static void addEntry(Level level, BlockPos position) {
        if (!ConfigManager.DISABLE.get()) {
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                ResourceKey<Level> dimension = level.dimension();
                if (!ConfigManager.isDimensionBlocked(dimension)) {
                    ChunkPos chunkPos = new ChunkPos(position);
                    WorldBorder border = level.getWorldBorder();
                    Set<ChunkPos> chunks = new ObjectLinkedOpenHashSet();
                    chunks.add(chunkPos);
                    int oX = chunkPos.x;
                    int oZ = chunkPos.z;
                    chunks.add(chunkPos);
                    for (int x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {
                            ChunkPos newPos = new ChunkPos(oX + x, oZ + z);
                            if (!ConfigManager.CHECK_WORLD_BORDER.get() || border.isWithinBounds(newPos)) {
                                chunks.add(newPos);
                            }
                        }
                    }
                    TileTicker.Entry newEntry = new TileTicker.Entry(dimension, position, chunks, (long) ServerLifecycleHooks.getCurrentServer().getTickCount());
                    synchronized (listLock) {
                        if (tickingList) {
                            pendingEntries.add(newEntry);
                        } else {
                            tileEntries.add(newEntry);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!ConfigManager.DISABLE.get()) {
                Set<TileTicker.Entry> toRemove = new ObjectLinkedOpenHashSet();
                Set<TileTicker.Entry> copy;
                synchronized (listLock) {
                    tickingList = true;
                    copy = new ObjectLinkedOpenHashSet(tileEntries);
                    tickingList = false;
                }
                synchronized (worldLock) {
                    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                    for (TileTicker.Entry entry : copy) {
                        ServerLevel level = server.getLevel(entry.getDimension());
                        if (level != null && entry.age(server) <= (long) ConfigManager.MAXIMUM_AGE.get().intValue() && (!ConfigManager.CHECK_WORLD_BORDER.get() || level.m_6857_().isWithinBounds(entry.getPosition()))) {
                            if (level.getChunkSource().hasChunk(entry.getPosition().m_123341_() >> 4, entry.getPosition().m_123343_() >> 4)) {
                                boolean skip = false;
                                for (ChunkPos chunkPos : entry.getChunkPositions()) {
                                    if (!level.getChunkSource().hasChunk(chunkPos.x, chunkPos.z)) {
                                        skip = true;
                                        break;
                                    }
                                }
                                synchronized (HandleChunk.LOADED_CHUNKS) {
                                    Set<ChunkPos> loadedChunks = (Set<ChunkPos>) HandleChunk.LOADED_CHUNKS.get(entry.dimension);
                                    if (loadedChunks != null) {
                                        for (ChunkPos chunkPosx : entry.getChunkPositions()) {
                                            if (!loadedChunks.contains(chunkPosx)) {
                                                skip = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (!skip) {
                                    BlockEntity blockEntity = level.m_7702_(entry.getPosition());
                                    if (blockEntity instanceof RandomizableContainerBlockEntity) {
                                        RandomizableContainerBlockEntity be = (RandomizableContainerBlockEntity) blockEntity;
                                        if (!(blockEntity instanceof ILootBlockEntity)) {
                                            if (be.lootTable != null && !ConfigManager.isBlacklisted(be.lootTable)) {
                                                BlockState stateAt = level.m_8055_(entry.getPosition());
                                                BlockState replacement = ConfigManager.replacement(stateAt);
                                                if (replacement == null) {
                                                    toRemove.add(entry);
                                                } else {
                                                    ResourceLocation table = be.lootTable;
                                                    long seed = be.lootTableSeed;
                                                    be.lootTable = null;
                                                    CompoundTag oldData = be.getPersistentData();
                                                    LockCode oldCode = be.f_58621_;
                                                    level.m_46961_(entry.getPosition(), false);
                                                    level.m_7731_(entry.getPosition(), replacement, 2);
                                                    blockEntity = level.m_7702_(entry.getPosition());
                                                    if (blockEntity instanceof RandomizableContainerBlockEntity) {
                                                        RandomizableContainerBlockEntity baseEntity = (RandomizableContainerBlockEntity) blockEntity;
                                                        blockEntity.getPersistentData().merge(oldData);
                                                        baseEntity.f_58621_ = oldCode;
                                                        if (blockEntity instanceof ILootBlockEntity) {
                                                            baseEntity.setLootTable(table, seed);
                                                        } else {
                                                            LootrAPI.LOG.error("replacement " + replacement + " is not an ILootTile " + entry.getDimension() + " at " + entry.getPosition());
                                                        }
                                                    }
                                                    toRemove.add(entry);
                                                }
                                                continue;
                                            }
                                            toRemove.add(entry);
                                            continue;
                                        }
                                    }
                                    toRemove.add(entry);
                                }
                            }
                        } else {
                            toRemove.add(entry);
                        }
                    }
                }
                synchronized (listLock) {
                    tickingList = true;
                    tileEntries.removeAll(toRemove);
                    tileEntries.addAll(pendingEntries);
                    tickingList = false;
                    pendingEntries.clear();
                }
            }
        }
    }

    public static class Entry {

        private final ResourceKey<Level> dimension;

        private final BlockPos position;

        private final Set<ChunkPos> chunks;

        private final long addedAt;

        public Entry(ResourceKey<Level> dimension, BlockPos position, Set<ChunkPos> chunks, long addedAt) {
            this.dimension = dimension;
            this.position = position;
            this.chunks = chunks;
            this.addedAt = addedAt;
        }

        public ResourceKey<Level> getDimension() {
            return this.dimension;
        }

        public BlockPos getPosition() {
            return this.position;
        }

        public Set<ChunkPos> getChunkPositions() {
            return this.chunks;
        }

        public long age(MinecraftServer server) {
            return (long) server.getTickCount() - this.addedAt;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                TileTicker.Entry entry = (TileTicker.Entry) o;
                return !this.dimension.equals(entry.dimension) ? false : this.position.equals(entry.position);
            } else {
                return false;
            }
        }

        public int hashCode() {
            int result = this.dimension.hashCode();
            return 31 * result + this.position.hashCode();
        }
    }
}