package me.jellysquid.mods.lithium.mixin.ai.poi;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Codec;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import me.jellysquid.mods.lithium.common.util.Distances;
import me.jellysquid.mods.lithium.common.world.interests.PointOfInterestSetExtended;
import me.jellysquid.mods.lithium.common.world.interests.PointOfInterestStorageExtended;
import me.jellysquid.mods.lithium.common.world.interests.RegionBasedStorageSectionExtended;
import me.jellysquid.mods.lithium.common.world.interests.iterator.NearbyPointOfInterestStream;
import me.jellysquid.mods.lithium.common.world.interests.iterator.SinglePointOfInterestTypeFilter;
import me.jellysquid.mods.lithium.common.world.interests.iterator.SphereChunkOrderedPoiSetSpliterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiSection;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.storage.SectionStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ PoiManager.class })
public abstract class PointOfInterestStorageMixin extends SectionStorage<PoiSection> implements PointOfInterestStorageExtended {

    public PointOfInterestStorageMixin(Path path, Function<Runnable, Codec<PoiSection>> codecFactory, Function<Runnable, PoiSection> factory, DataFixer dataFixer, DataFixTypes dataFixTypes, boolean dsync, RegistryAccess dynamicRegistryManager, LevelHeightAccessor world) {
        super(path, codecFactory, factory, dataFixer, dataFixTypes, dsync, dynamicRegistryManager, world);
    }

    @Overwrite
    @VisibleForDebug
    public Stream<PoiRecord> getInChunk(Predicate<Holder<PoiType>> predicate, ChunkPos pos, PoiManager.Occupancy status) {
        return ((RegionBasedStorageSectionExtended) this).getWithinChunkColumn(pos.x, pos.z).flatMap(set -> set.getRecords(predicate, status));
    }

    @Overwrite
    public Optional<BlockPos> getRandom(Predicate<Holder<PoiType>> typePredicate, Predicate<BlockPos> posPredicate, PoiManager.Occupancy status, BlockPos pos, int radius, RandomSource rand) {
        ArrayList<PoiRecord> list = this.withinSphereChunkSectionSorted(typePredicate, pos, radius, status);
        for (int i = list.size() - 1; i >= 0; i--) {
            PoiRecord currentPOI = (PoiRecord) list.set(rand.nextInt(i + 1), (PoiRecord) list.get(i));
            list.set(i, currentPOI);
            if (posPredicate.test(currentPOI.getPos())) {
                return Optional.of(currentPOI.getPos());
            }
        }
        return Optional.empty();
    }

    @Overwrite
    public Optional<BlockPos> findClosest(Predicate<Holder<PoiType>> predicate, BlockPos pos, int radius, PoiManager.Occupancy status) {
        return this.findClosest(predicate, null, pos, radius, status);
    }

    @Overwrite
    public Optional<BlockPos> findClosest(Predicate<Holder<PoiType>> predicate, Predicate<BlockPos> posPredicate, BlockPos pos, int radius, PoiManager.Occupancy status) {
        Stream<PoiRecord> pointOfInterestStream = this.streamOutwards(pos, radius, status, true, false, predicate, posPredicate == null ? null : poi -> posPredicate.test(poi.getPos()));
        return pointOfInterestStream.map(PoiRecord::m_27257_).findFirst();
    }

    @Overwrite
    public long getCountInRange(Predicate<Holder<PoiType>> predicate, BlockPos pos, int radius, PoiManager.Occupancy status) {
        return (long) this.withinSphereChunkSectionSorted(predicate, pos, radius, status).size();
    }

    @Overwrite
    public Stream<PoiRecord> getInRange(Predicate<Holder<PoiType>> predicate, BlockPos sphereOrigin, int radius, PoiManager.Occupancy status) {
        return this.withinSphereChunkSectionSortedStream(predicate, sphereOrigin, radius, status);
    }

    @Override
    public Optional<PoiRecord> findNearestForPortalLogic(BlockPos origin, int radius, Holder<PoiType> type, PoiManager.Occupancy status, Predicate<PoiRecord> afterSortPredicate, WorldBorder worldBorder) {
        boolean worldBorderIsFarAway = worldBorder == null || worldBorder.getDistanceToBorder((double) origin.m_123341_(), (double) origin.m_123343_()) > (double) (radius + 3);
        Predicate<PoiRecord> poiPredicateAfterSorting;
        if (worldBorderIsFarAway) {
            poiPredicateAfterSorting = afterSortPredicate;
        } else {
            poiPredicateAfterSorting = poi -> worldBorder.isWithinBounds(poi.getPos()) && afterSortPredicate.test(poi);
        }
        return this.streamOutwards(origin, radius, status, true, true, new SinglePointOfInterestTypeFilter(type), poiPredicateAfterSorting).findFirst();
    }

    private Stream<PoiRecord> withinSphereChunkSectionSortedStream(Predicate<Holder<PoiType>> predicate, BlockPos origin, int radius, PoiManager.Occupancy status) {
        double radiusSq = (double) (radius * radius);
        RegionBasedStorageSectionExtended<PoiSection> storage = (RegionBasedStorageSectionExtended<PoiSection>) this;
        Stream<Stream<PoiSection>> stream = StreamSupport.stream(new SphereChunkOrderedPoiSetSpliterator(radius, origin, storage), false);
        return stream.flatMap(setStream -> setStream.flatMap(set -> set.getRecords(predicate, status).filter(point -> Distances.isWithinCircleRadius(origin, radiusSq, point.getPos()))));
    }

    private ArrayList<PoiRecord> withinSphereChunkSectionSorted(Predicate<Holder<PoiType>> predicate, BlockPos origin, int radius, PoiManager.Occupancy status) {
        double radiusSq = (double) (radius * radius);
        int minChunkX = origin.m_123341_() - radius - 1 >> 4;
        int minChunkZ = origin.m_123343_() - radius - 1 >> 4;
        int maxChunkX = origin.m_123341_() + radius + 1 >> 4;
        int maxChunkZ = origin.m_123343_() + radius + 1 >> 4;
        RegionBasedStorageSectionExtended<PoiSection> storage = (RegionBasedStorageSectionExtended<PoiSection>) this;
        ArrayList<PoiRecord> points = new ArrayList();
        Consumer<PoiRecord> collector = point -> {
            if (Distances.isWithinCircleRadius(origin, radiusSq, point.getPos())) {
                points.add(point);
            }
        };
        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                for (PoiSection set : storage.getInChunkColumn(x, z)) {
                    ((PointOfInterestSetExtended) set).collectMatchingPoints(predicate, status, collector);
                }
            }
        }
        return points;
    }

    private Stream<PoiRecord> streamOutwards(BlockPos origin, int radius, PoiManager.Occupancy status, boolean useSquareDistanceLimit, boolean preferNegativeY, Predicate<Holder<PoiType>> typePredicate, @Nullable Predicate<PoiRecord> afterSortingPredicate) {
        RegionBasedStorageSectionExtended<PoiSection> storage = (RegionBasedStorageSectionExtended<PoiSection>) this;
        return StreamSupport.stream(new NearbyPointOfInterestStream(typePredicate, status, useSquareDistanceLimit, preferNegativeY, afterSortingPredicate, origin, radius, storage), false);
    }

    @Shadow
    protected abstract void updateFromSection(LevelChunkSection var1, SectionPos var2, BiConsumer<BlockPos, PoiType> var3);
}