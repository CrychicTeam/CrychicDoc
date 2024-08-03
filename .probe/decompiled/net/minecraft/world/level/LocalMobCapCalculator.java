package net.minecraft.world.level;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobCategory;

public class LocalMobCapCalculator {

    private final Long2ObjectMap<List<ServerPlayer>> playersNearChunk = new Long2ObjectOpenHashMap();

    private final Map<ServerPlayer, LocalMobCapCalculator.MobCounts> playerMobCounts = Maps.newHashMap();

    private final ChunkMap chunkMap;

    public LocalMobCapCalculator(ChunkMap chunkMap0) {
        this.chunkMap = chunkMap0;
    }

    private List<ServerPlayer> getPlayersNear(ChunkPos chunkPos0) {
        return (List<ServerPlayer>) this.playersNearChunk.computeIfAbsent(chunkPos0.toLong(), p_186511_ -> this.chunkMap.getPlayersCloseForSpawning(chunkPos0));
    }

    public void addMob(ChunkPos chunkPos0, MobCategory mobCategory1) {
        for (ServerPlayer $$2 : this.getPlayersNear(chunkPos0)) {
            ((LocalMobCapCalculator.MobCounts) this.playerMobCounts.computeIfAbsent($$2, p_186503_ -> new LocalMobCapCalculator.MobCounts())).add(mobCategory1);
        }
    }

    public boolean canSpawn(MobCategory mobCategory0, ChunkPos chunkPos1) {
        for (ServerPlayer $$2 : this.getPlayersNear(chunkPos1)) {
            LocalMobCapCalculator.MobCounts $$3 = (LocalMobCapCalculator.MobCounts) this.playerMobCounts.get($$2);
            if ($$3 == null || $$3.canSpawn(mobCategory0)) {
                return true;
            }
        }
        return false;
    }

    static class MobCounts {

        private final Object2IntMap<MobCategory> counts = new Object2IntOpenHashMap(MobCategory.values().length);

        public void add(MobCategory mobCategory0) {
            this.counts.computeInt(mobCategory0, (p_186520_, p_186521_) -> p_186521_ == null ? 1 : p_186521_ + 1);
        }

        public boolean canSpawn(MobCategory mobCategory0) {
            return this.counts.getOrDefault(mobCategory0, 0) < mobCategory0.getMaxInstancesPerChunk();
        }
    }
}