package me.lucko.spark.common.platform.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import me.lucko.spark.proto.SparkProtos;

public class WorldStatisticsProvider {

    private final AsyncWorldInfoProvider provider;

    public WorldStatisticsProvider(AsyncWorldInfoProvider provider) {
        this.provider = provider;
    }

    public SparkProtos.WorldStatistics getWorldStatistics() {
        WorldInfoProvider.ChunksResult<? extends ChunkInfo<?>> result = this.provider.getChunks();
        if (result == null) {
            return null;
        } else {
            SparkProtos.WorldStatistics.Builder stats = SparkProtos.WorldStatistics.newBuilder();
            AtomicInteger combinedTotal = new AtomicInteger();
            CountMap<String> combined = new CountMap.Simple<>(new HashMap());
            result.getWorlds().forEach((worldName, chunks) -> {
                SparkProtos.WorldStatistics.World.Builder builder = SparkProtos.WorldStatistics.World.newBuilder();
                builder.setName(worldName);
                List<WorldStatisticsProvider.Region> regions = groupIntoRegions(chunks);
                int total = 0;
                for (WorldStatisticsProvider.Region region : regions) {
                    builder.addRegions(regionToProto(region, combined));
                    total += region.getTotalEntities().get();
                }
                builder.setTotalEntities(total);
                combinedTotal.addAndGet(total);
                stats.addWorlds(builder.build());
            });
            stats.setTotalEntities(combinedTotal.get());
            combined.asMap().forEach((key, value) -> stats.putEntityCounts(key, value.get()));
            return stats.build();
        }
    }

    private static SparkProtos.WorldStatistics.Region regionToProto(WorldStatisticsProvider.Region region, CountMap<String> combined) {
        SparkProtos.WorldStatistics.Region.Builder builder = SparkProtos.WorldStatistics.Region.newBuilder();
        builder.setTotalEntities(region.getTotalEntities().get());
        for (ChunkInfo<?> chunk : region.getChunks()) {
            builder.addChunks(chunkToProto(chunk, combined));
        }
        return builder.build();
    }

    private static <E> SparkProtos.WorldStatistics.Chunk chunkToProto(ChunkInfo<E> chunk, CountMap<String> combined) {
        SparkProtos.WorldStatistics.Chunk.Builder builder = SparkProtos.WorldStatistics.Chunk.newBuilder();
        builder.setX(chunk.getX());
        builder.setZ(chunk.getZ());
        builder.setTotalEntities(chunk.getEntityCounts().total().get());
        chunk.getEntityCounts().asMap().forEach((key, value) -> {
            String name = chunk.entityTypeName((E) key);
            int count = value.get();
            if (name == null) {
                name = "unknown[" + key.toString() + "]";
            }
            builder.putEntityCounts(name, count);
            combined.add(name, count);
        });
        return builder.build();
    }

    private static List<WorldStatisticsProvider.Region> groupIntoRegions(List<? extends ChunkInfo<?>> chunks) {
        List<WorldStatisticsProvider.Region> regions = new ArrayList();
        for (ChunkInfo<?> chunk : chunks) {
            CountMap<?> counts = chunk.getEntityCounts();
            if (counts.total().get() != 0) {
                boolean found = false;
                for (WorldStatisticsProvider.Region region : regions) {
                    if (region.isAdjacent(chunk)) {
                        found = true;
                        region.add(chunk);
                        Iterator<WorldStatisticsProvider.Region> iterator = regions.iterator();
                        while (iterator.hasNext()) {
                            WorldStatisticsProvider.Region otherRegion = (WorldStatisticsProvider.Region) iterator.next();
                            if (region != otherRegion && otherRegion.isAdjacent(chunk)) {
                                iterator.remove();
                                region.merge(otherRegion);
                            }
                        }
                        break;
                    }
                }
                if (!found) {
                    regions.add(new WorldStatisticsProvider.Region(chunk));
                }
            }
        }
        return regions;
    }

    private static final class Region {

        private static final int DISTANCE_THRESHOLD = 2;

        private final Set<ChunkInfo<?>> chunks = new HashSet();

        private final AtomicInteger totalEntities;

        private Region(ChunkInfo<?> initial) {
            this.chunks.add(initial);
            this.totalEntities = new AtomicInteger(initial.getEntityCounts().total().get());
        }

        public Set<ChunkInfo<?>> getChunks() {
            return this.chunks;
        }

        public AtomicInteger getTotalEntities() {
            return this.totalEntities;
        }

        public boolean isAdjacent(ChunkInfo<?> chunk) {
            for (ChunkInfo<?> el : this.chunks) {
                if (squaredEuclideanDistance(el, chunk) <= 2L) {
                    return true;
                }
            }
            return false;
        }

        public void add(ChunkInfo<?> chunk) {
            this.chunks.add(chunk);
            this.totalEntities.addAndGet(chunk.getEntityCounts().total().get());
        }

        public void merge(WorldStatisticsProvider.Region group) {
            this.chunks.addAll(group.getChunks());
            this.totalEntities.addAndGet(group.getTotalEntities().get());
        }

        private static long squaredEuclideanDistance(ChunkInfo<?> a, ChunkInfo<?> b) {
            long dx = (long) (a.getX() - b.getX());
            long dz = (long) (a.getZ() - b.getZ());
            return dx * dx + dz * dz;
        }
    }
}