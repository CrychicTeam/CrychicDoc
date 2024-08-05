package me.jellysquid.mods.lithium.common.entity.movement_tracker;

public interface MovementTrackerCache {

    void remove(SectionedEntityMovementTracker<?, ?> var1);

    <S extends SectionedEntityMovementTracker<?, ?>> S deduplicate(S var1);
}