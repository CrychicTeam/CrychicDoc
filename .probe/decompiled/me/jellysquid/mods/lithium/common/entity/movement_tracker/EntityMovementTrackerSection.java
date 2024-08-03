package me.jellysquid.mods.lithium.common.entity.movement_tracker;

import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySectionStorage;

public interface EntityMovementTrackerSection {

    void addListener(SectionedEntityMovementTracker<?, ?> var1);

    void removeListener(EntitySectionStorage<?> var1, SectionedEntityMovementTracker<?, ?> var2);

    void trackEntityMovement(int var1, long var2);

    long getChangeTime(int var1);

    <S, E extends EntityAccess> void listenToMovementOnce(SectionedEntityMovementTracker<E, S> var1, int var2);

    <S, E extends EntityAccess> void removeListenToMovementOnce(SectionedEntityMovementTracker<E, S> var1, int var2);
}