package me.jellysquid.mods.lithium.common.entity.nearby_tracker;

import me.jellysquid.mods.lithium.common.util.tuples.Range6Int;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public interface NearbyEntityListener {

    Range6Int EMPTY_RANGE = new Range6Int(0, 0, 0, -1, -1, -1);

    default void updateChunkRegistrations(EntitySectionStorage<? extends EntityAccess> entityCache, SectionPos prevCenterPos, Range6Int prevChunkRange, SectionPos newCenterPos, Range6Int newChunkRange) {
        if (newChunkRange != EMPTY_RANGE || prevChunkRange != EMPTY_RANGE) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            BoundingBox after = newCenterPos == null ? null : new BoundingBox(newCenterPos.m_123341_() - newChunkRange.negativeX(), newCenterPos.m_123342_() - newChunkRange.negativeY(), newCenterPos.m_123343_() - newChunkRange.negativeZ(), newCenterPos.m_123341_() + newChunkRange.positiveX(), newCenterPos.m_123342_() + newChunkRange.positiveY(), newCenterPos.m_123343_() + newChunkRange.positiveZ());
            BoundingBox before = prevCenterPos == null ? null : new BoundingBox(prevCenterPos.m_123341_() - prevChunkRange.negativeX(), prevCenterPos.m_123342_() - prevChunkRange.negativeY(), prevCenterPos.m_123343_() - prevChunkRange.negativeZ(), prevCenterPos.m_123341_() + prevChunkRange.positiveX(), prevCenterPos.m_123342_() + prevChunkRange.positiveY(), prevCenterPos.m_123343_() + prevChunkRange.positiveZ());
            if (before != null) {
                for (int x = before.minX(); x <= before.maxX(); x++) {
                    for (int y = before.minY(); y <= before.maxY(); y++) {
                        for (int z = before.minZ(); z <= before.maxZ(); z++) {
                            if (after == null || !after.isInside(pos.set(x, y, z))) {
                                long sectionPos = SectionPos.asLong(x, y, z);
                                EntitySection<? extends EntityAccess> trackingSection = entityCache.getOrCreateSection(sectionPos);
                                ((NearbyEntityListenerSection) trackingSection).removeListener(entityCache, this);
                                if (trackingSection.isEmpty()) {
                                    entityCache.remove(sectionPos);
                                }
                            }
                        }
                    }
                }
            }
            if (after != null) {
                for (int x = after.minX(); x <= after.maxX(); x++) {
                    for (int y = after.minY(); y <= after.maxY(); y++) {
                        for (int zx = after.minZ(); zx <= after.maxZ(); zx++) {
                            if (before == null || !before.isInside(pos.set(x, y, zx))) {
                                ((NearbyEntityListenerSection) entityCache.getOrCreateSection(SectionPos.asLong(x, y, zx))).addListener(this);
                            }
                        }
                    }
                }
            }
        }
    }

    default void removeFromAllChunksInRange(EntitySectionStorage<? extends EntityAccess> entityCache, SectionPos prevCenterPos) {
        this.updateChunkRegistrations(entityCache, prevCenterPos, this.getChunkRange(), null, EMPTY_RANGE);
    }

    default void addToAllChunksInRange(EntitySectionStorage<? extends EntityAccess> entityCache, SectionPos newCenterPos) {
        this.updateChunkRegistrations(entityCache, null, EMPTY_RANGE, newCenterPos, this.getChunkRange());
    }

    Range6Int getChunkRange();

    void onEntityEnteredRange(Entity var1);

    void onEntityLeftRange(Entity var1);

    default Class<? extends Entity> getEntityClass() {
        return Entity.class;
    }

    default <T> void onSectionEnteredRange(Object entityTrackingSection, ClassInstanceMultiMap<T> collection) {
        for (Entity entity : collection.find(this.getEntityClass())) {
            this.onEntityEnteredRange(entity);
        }
    }

    default <T> void onSectionLeftRange(Object entityTrackingSection, ClassInstanceMultiMap<T> collection) {
        for (Entity entity : collection.find(this.getEntityClass())) {
            this.onEntityLeftRange(entity);
        }
    }
}