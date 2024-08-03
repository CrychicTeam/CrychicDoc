package me.lucko.spark.forge;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import me.lucko.spark.common.platform.world.AbstractChunkInfo;
import me.lucko.spark.common.platform.world.CountMap;
import me.lucko.spark.common.platform.world.WorldInfoProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.entity.TransientEntitySectionManager;

public abstract class ForgeWorldInfoProvider implements WorldInfoProvider {

    protected List<ForgeWorldInfoProvider.ForgeChunkInfo> getChunksFromCache(EntitySectionStorage<Entity> cache) {
        LongSet loadedChunks = cache.getAllChunksWithExistingSections();
        List<ForgeWorldInfoProvider.ForgeChunkInfo> list = new ArrayList(loadedChunks.size());
        LongIterator iterator = loadedChunks.iterator();
        while (iterator.hasNext()) {
            long chunkPos = iterator.nextLong();
            Stream<EntitySection<Entity>> sections = cache.getExistingSectionsInChunk(chunkPos);
            list.add(new ForgeWorldInfoProvider.ForgeChunkInfo(chunkPos, sections));
        }
        return list;
    }

    public static final class Client extends ForgeWorldInfoProvider {

        private final Minecraft client;

        public Client(Minecraft client) {
            this.client = client;
        }

        @Override
        public WorldInfoProvider.CountsResult pollCounts() {
            ClientLevel level = this.client.level;
            if (level == null) {
                return null;
            } else {
                TransientEntitySectionManager<Entity> entityManager = level.entityStorage;
                EntityLookup<Entity> entityIndex = entityManager.entityStorage;
                int entities = entityIndex.count();
                int chunks = level.getChunkSource().getLoadedChunksCount();
                return new WorldInfoProvider.CountsResult(-1, entities, -1, chunks);
            }
        }

        @Override
        public WorldInfoProvider.ChunksResult<ForgeWorldInfoProvider.ForgeChunkInfo> pollChunks() {
            WorldInfoProvider.ChunksResult<ForgeWorldInfoProvider.ForgeChunkInfo> data = new WorldInfoProvider.ChunksResult<>();
            ClientLevel level = this.client.level;
            if (level == null) {
                return null;
            } else {
                TransientEntitySectionManager<Entity> entityManager = level.entityStorage;
                EntitySectionStorage<Entity> cache = entityManager.sectionStorage;
                List<ForgeWorldInfoProvider.ForgeChunkInfo> list = this.getChunksFromCache(cache);
                data.put(level.m_46472_().location().getPath(), list);
                return data;
            }
        }
    }

    static final class ForgeChunkInfo extends AbstractChunkInfo<EntityType<?>> {

        private final CountMap<EntityType<?>> entityCounts = new CountMap.Simple<>(new HashMap());

        ForgeChunkInfo(long chunkPos, Stream<EntitySection<Entity>> entities) {
            super(ChunkPos.getX(chunkPos), ChunkPos.getZ(chunkPos));
            entities.forEach(section -> {
                if (section.getStatus().isAccessible()) {
                    section.getEntities().forEach(entity -> this.entityCounts.increment(entity.getType()));
                }
            });
        }

        @Override
        public CountMap<EntityType<?>> getEntityCounts() {
            return this.entityCounts;
        }

        public String entityTypeName(EntityType<?> type) {
            return EntityType.getKey(type).toString();
        }
    }

    public static final class Server extends ForgeWorldInfoProvider {

        private final MinecraftServer server;

        public Server(MinecraftServer server) {
            this.server = server;
        }

        @Override
        public WorldInfoProvider.CountsResult pollCounts() {
            int players = this.server.getPlayerCount();
            int entities = 0;
            int chunks = 0;
            for (ServerLevel level : this.server.getAllLevels()) {
                PersistentEntitySectionManager<Entity> entityManager = level.entityManager;
                EntityLookup<Entity> entityIndex = entityManager.visibleEntityStorage;
                entities += entityIndex.count();
                chunks += level.getChunkSource().getLoadedChunksCount();
            }
            return new WorldInfoProvider.CountsResult(players, entities, -1, chunks);
        }

        @Override
        public WorldInfoProvider.ChunksResult<ForgeWorldInfoProvider.ForgeChunkInfo> pollChunks() {
            WorldInfoProvider.ChunksResult<ForgeWorldInfoProvider.ForgeChunkInfo> data = new WorldInfoProvider.ChunksResult<>();
            for (ServerLevel level : this.server.getAllLevels()) {
                PersistentEntitySectionManager<Entity> entityManager = level.entityManager;
                EntitySectionStorage<Entity> cache = entityManager.sectionStorage;
                List<ForgeWorldInfoProvider.ForgeChunkInfo> list = this.getChunksFromCache(cache);
                data.put(level.m_46472_().location().getPath(), list);
            }
            return data;
        }
    }
}