package me.lucko.spark.common.platform.world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface WorldInfoProvider {

    WorldInfoProvider NO_OP = new WorldInfoProvider() {

        @Override
        public WorldInfoProvider.CountsResult pollCounts() {
            return null;
        }

        @Override
        public WorldInfoProvider.ChunksResult<? extends ChunkInfo<?>> pollChunks() {
            return null;
        }
    };

    WorldInfoProvider.CountsResult pollCounts();

    WorldInfoProvider.ChunksResult<? extends ChunkInfo<?>> pollChunks();

    default boolean mustCallSync() {
        return true;
    }

    public static final class ChunksResult<T extends ChunkInfo<?>> {

        private final Map<String, List<T>> worlds = new HashMap();

        public void put(String worldName, List<T> chunks) {
            this.worlds.put(worldName, chunks);
        }

        public Map<String, List<T>> getWorlds() {
            return this.worlds;
        }
    }

    public static final class CountsResult {

        private final int players;

        private final int entities;

        private final int tileEntities;

        private final int chunks;

        public CountsResult(int players, int entities, int tileEntities, int chunks) {
            this.players = players;
            this.entities = entities;
            this.tileEntities = tileEntities;
            this.chunks = chunks;
        }

        public int players() {
            return this.players;
        }

        public int entities() {
            return this.entities;
        }

        public int tileEntities() {
            return this.tileEntities;
        }

        public int chunks() {
            return this.chunks;
        }
    }
}