package me.jellysquid.mods.lithium.common.entity.nearby_tracker;

import java.util.ArrayList;
import java.util.List;
import me.jellysquid.mods.lithium.common.util.tuples.Range6Int;
import me.jellysquid.mods.lithium.mixin.ai.nearby_entity_tracking.ServerEntityManagerAccessor;
import me.jellysquid.mods.lithium.mixin.ai.nearby_entity_tracking.ServerWorldAccessor;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySectionStorage;

public class NearbyEntityListenerMulti implements NearbyEntityListener {

    private final List<NearbyEntityListener> listeners = new ArrayList(4);

    private Range6Int range = null;

    public <T extends LivingEntity> void addListener(NearbyEntityTracker<T> tracker) {
        this.listeners.add(tracker);
        this.updateRange(tracker);
    }

    public <T extends LivingEntity> void removeListener(NearbyEntityTracker<T> tracker) {
        this.listeners.remove(tracker);
        this.updateRange(tracker);
    }

    private <S extends EntityAccess, T extends LivingEntity> void updateRange(NearbyEntityTracker<T> tracker) {
        if (this.range != null) {
            Range6Int updatedRange = this.calculateRange();
            if (!this.range.equals(updatedRange)) {
                this.range = updatedRange;
                EntitySectionStorage<S> entityCache = (EntitySectionStorage<S>) ((ServerEntityManagerAccessor) ((ServerWorldAccessor) tracker.getEntity().m_9236_()).getEntityManager()).getCache();
                SectionPos chunkPos = SectionPos.of(tracker.getEntity().m_20183_());
                this.updateChunkRegistrations(entityCache, chunkPos, this.range, chunkPos, updatedRange);
            }
        }
    }

    @Override
    public Range6Int getChunkRange() {
        return this.range != null ? this.range : (this.range = this.calculateRange());
    }

    private Range6Int calculateRange() {
        if (this.listeners.isEmpty()) {
            return EMPTY_RANGE;
        } else {
            int positiveX = -1;
            int positiveY = -1;
            int positiveZ = -1;
            int negativeX = 0;
            int negativeY = 0;
            int negativeZ = 0;
            for (NearbyEntityListener listener : this.listeners) {
                Range6Int chunkRange = listener.getChunkRange();
                positiveX = Math.max(chunkRange.positiveX(), positiveX);
                positiveY = Math.max(chunkRange.positiveY(), positiveY);
                positiveZ = Math.max(chunkRange.positiveZ(), positiveZ);
                negativeX = Math.max(chunkRange.negativeX(), negativeX);
                negativeY = Math.max(chunkRange.negativeY(), negativeY);
                negativeZ = Math.max(chunkRange.negativeZ(), negativeZ);
            }
            return new Range6Int(positiveX, positiveY, positiveZ, negativeX, negativeY, negativeZ);
        }
    }

    @Override
    public void onEntityEnteredRange(Entity entity) {
        for (NearbyEntityListener listener : this.listeners) {
            listener.onEntityEnteredRange(entity);
        }
    }

    @Override
    public void onEntityLeftRange(Entity entity) {
        for (NearbyEntityListener listener : this.listeners) {
            listener.onEntityLeftRange(entity);
        }
    }

    public String toString() {
        StringBuilder sublisteners = new StringBuilder();
        String comma = "";
        for (NearbyEntityListener listener : this.listeners) {
            sublisteners.append(comma).append(listener.toString());
            comma = ",";
        }
        return super.toString() + " with sublisteners: [" + sublisteners + "]";
    }
}