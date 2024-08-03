package me.jellysquid.mods.lithium.common.world.interests.iterator;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import me.jellysquid.mods.lithium.common.util.Distances;
import me.jellysquid.mods.lithium.common.util.tuples.SortedPointOfInterest;
import me.jellysquid.mods.lithium.common.world.interests.PointOfInterestSetExtended;
import me.jellysquid.mods.lithium.common.world.interests.RegionBasedStorageSectionExtended;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiSection;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

public class NearbyPointOfInterestStream extends AbstractSpliterator<PoiRecord> {

    private final RegionBasedStorageSectionExtended<PoiSection> storage;

    private final Predicate<Holder<PoiType>> typeSelector;

    private final PoiManager.Occupancy occupationStatus;

    private final LongArrayList chunksSortedByMinDistance;

    private final ArrayList<SortedPointOfInterest> points;

    private final Predicate<PoiRecord> afterSortingPredicate;

    private final Consumer<PoiRecord> collector;

    private final BlockPos origin;

    private int chunkIndex;

    private double currChunkMinDistanceSq;

    private int pointIndex;

    private final Comparator<? super SortedPointOfInterest> pointComparator;

    public NearbyPointOfInterestStream(Predicate<Holder<PoiType>> typeSelector, PoiManager.Occupancy status, boolean useSquareDistanceLimit, boolean preferNegativeY, @Nullable Predicate<PoiRecord> afterSortingPredicate, BlockPos origin, int radius, RegionBasedStorageSectionExtended<PoiSection> storage) {
        super(Long.MAX_VALUE, 16);
        this.storage = storage;
        this.chunkIndex = 0;
        this.pointIndex = 0;
        this.points = new ArrayList();
        this.occupationStatus = status;
        this.typeSelector = typeSelector;
        this.origin = origin;
        if (useSquareDistanceLimit) {
            this.collector = point -> {
                if (Distances.isWithinSquareRadius(this.origin, radius, point.getPos())) {
                    this.points.add(new SortedPointOfInterest(point, this.origin));
                }
            };
        } else {
            double radiusSq = (double) (radius * radius);
            this.collector = point -> {
                if (Distances.isWithinCircleRadius(this.origin, radiusSq, point.getPos())) {
                    this.points.add(new SortedPointOfInterest(point, this.origin));
                }
            };
        }
        double distanceLimitL2Sq = useSquareDistanceLimit ? (double) (radius * radius * 2) : (double) (radius * radius);
        this.chunksSortedByMinDistance = initChunkPositions(origin, radius, distanceLimitL2Sq);
        this.afterSortingPredicate = afterSortingPredicate;
        this.pointComparator = preferNegativeY ? (o1, o2) -> {
            int cmp = Double.compare(o1.distanceSq(), o2.distanceSq());
            if (cmp != 0) {
                return cmp;
            } else {
                int negativeY = Integer.compare(o1.getY(), o2.getY());
                if (negativeY != 0) {
                    return negativeY;
                } else {
                    int cmp3 = Integer.compare(SectionPos.blockToSectionCoord(o1.getX()), SectionPos.blockToSectionCoord(o2.getX()));
                    return cmp3 != 0 ? cmp3 : Integer.compare(SectionPos.blockToSectionCoord(o1.getZ()), SectionPos.blockToSectionCoord(o2.getZ()));
                }
            }
        } : (o1, o2) -> {
            int cmp = Double.compare(o1.distanceSq(), o2.distanceSq());
            if (cmp != 0) {
                return cmp;
            } else {
                int cmp2 = Integer.compare(SectionPos.blockToSectionCoord(o1.getX()), SectionPos.blockToSectionCoord(o2.getX()));
                if (cmp2 != 0) {
                    return cmp2;
                } else {
                    int cmp3 = Integer.compare(SectionPos.blockToSectionCoord(o1.getZ()), SectionPos.blockToSectionCoord(o2.getZ()));
                    return cmp3 != 0 ? cmp3 : Integer.compare(SectionPos.blockToSectionCoord(o1.getY()), SectionPos.blockToSectionCoord(o2.getY()));
                }
            }
        };
    }

    private static LongArrayList initChunkPositions(BlockPos origin, int radius, double distanceLimitL2Sq) {
        int minChunkX = origin.m_123341_() - radius - 1 >> 4;
        int minChunkZ = origin.m_123343_() - radius - 1 >> 4;
        int maxChunkX = origin.m_123341_() + radius + 1 >> 4;
        int maxChunkZ = origin.m_123343_() + radius + 1 >> 4;
        LongArrayList chunkPositions = new LongArrayList();
        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                if (distanceLimitL2Sq >= Distances.getMinChunkToBlockDistanceL2Sq(origin, chunkX, chunkZ)) {
                    chunkPositions.add(ChunkPos.asLong(chunkX, chunkZ));
                }
            }
        }
        chunkPositions.sort((c1, c2) -> Double.compare(Distances.getMinChunkToBlockDistanceL2Sq(origin, ChunkPos.getX(c1), ChunkPos.getZ(c1)), Distances.getMinChunkToBlockDistanceL2Sq(origin, ChunkPos.getX(c2), ChunkPos.getZ(c2))));
        return chunkPositions;
    }

    public boolean tryAdvance(Consumer<? super PoiRecord> action) {
        if (this.pointIndex < this.points.size() && this.tryAdvancePoint(action)) {
            return true;
        } else {
            while (this.chunkIndex < this.chunksSortedByMinDistance.size()) {
                long chunkPos = this.chunksSortedByMinDistance.getLong(this.chunkIndex);
                int chunkPosX = ChunkPos.getX(chunkPos);
                int chunkPosZ = ChunkPos.getZ(chunkPos);
                this.currChunkMinDistanceSq = Distances.getMinChunkToBlockDistanceL2Sq(this.origin, chunkPosX, chunkPosZ);
                this.chunkIndex++;
                if (this.chunkIndex == this.chunksSortedByMinDistance.size()) {
                    this.currChunkMinDistanceSq = Double.POSITIVE_INFINITY;
                }
                int previousSize = this.points.size();
                for (PoiSection set : this.storage.getInChunkColumn(chunkPosX, chunkPosZ)) {
                    ((PointOfInterestSetExtended) set).collectMatchingPoints(this.typeSelector, this.occupationStatus, this.collector);
                }
                if (this.points.size() != previousSize) {
                    this.points.subList(this.pointIndex, this.points.size()).sort(this.pointComparator);
                    if (this.tryAdvancePoint(action)) {
                        return true;
                    }
                }
            }
            return this.tryAdvancePoint(action);
        }
    }

    private boolean tryAdvancePoint(Consumer<? super PoiRecord> action) {
        while (this.pointIndex < this.points.size()) {
            SortedPointOfInterest next = (SortedPointOfInterest) this.points.get(this.pointIndex);
            if (next.distanceSq() >= this.currChunkMinDistanceSq) {
                return false;
            }
            this.pointIndex++;
            if (this.afterSortingPredicate == null || this.afterSortingPredicate.test(next.poi())) {
                action.accept(next.poi());
                return true;
            }
        }
        return false;
    }
}