package me.jellysquid.mods.lithium.common.entity.movement_tracker;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.ArrayList;
import me.jellysquid.mods.lithium.common.util.tuples.WorldSectionBox;
import me.jellysquid.mods.lithium.mixin.util.entity_movement_tracking.ServerEntityManagerAccessor;
import me.jellysquid.mods.lithium.mixin.util.entity_movement_tracking.ServerWorldAccessor;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.EntitySectionStorage;

public abstract class SectionedEntityMovementTracker<E extends EntityAccess, S> {

    final WorldSectionBox trackedWorldSections;

    final Class<S> clazz;

    private final int trackedClass;

    ArrayList<EntitySection<E>> sortedSections;

    boolean[] sectionVisible;

    private int timesRegistered;

    private final ArrayList<EntityMovementTrackerSection> sectionsNotListeningTo;

    private long maxChangeTime;

    private ReferenceOpenHashSet<SectionedEntityMovementListener> sectionedEntityMovementListeners;

    public SectionedEntityMovementTracker(WorldSectionBox interactionChunks, Class<S> clazz) {
        this.clazz = clazz;
        this.trackedWorldSections = interactionChunks;
        this.trackedClass = MovementTrackerHelper.MOVEMENT_NOTIFYING_ENTITY_CLASSES.indexOf(clazz);
        assert this.trackedClass != -1;
        this.sectionedEntityMovementListeners = null;
        this.sectionsNotListeningTo = new ArrayList();
    }

    public int hashCode() {
        return HashCommon.mix(this.trackedWorldSections.hashCode()) ^ HashCommon.mix(this.trackedClass) ^ this.getClass().hashCode();
    }

    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && this.clazz == ((SectionedEntityMovementTracker) obj).clazz && this.trackedWorldSections.equals(((SectionedEntityMovementTracker) obj).trackedWorldSections);
    }

    public boolean isUnchangedSince(long lastCheckedTime) {
        if (lastCheckedTime <= this.maxChangeTime) {
            return false;
        } else if (!this.sectionsNotListeningTo.isEmpty()) {
            this.setChanged(this.listenToAllSectionsAndGetMaxChangeTime());
            return lastCheckedTime > this.maxChangeTime;
        } else {
            return true;
        }
    }

    private long listenToAllSectionsAndGetMaxChangeTime() {
        long maxChangeTime = Long.MIN_VALUE;
        ArrayList<EntityMovementTrackerSection> notListeningTo = this.sectionsNotListeningTo;
        for (int i = notListeningTo.size() - 1; i >= 0; i--) {
            EntityMovementTrackerSection entityMovementTrackerSection = (EntityMovementTrackerSection) notListeningTo.remove(i);
            entityMovementTrackerSection.listenToMovementOnce(this, this.trackedClass);
            maxChangeTime = Math.max(maxChangeTime, entityMovementTrackerSection.getChangeTime(this.trackedClass));
        }
        return maxChangeTime;
    }

    public void register(ServerLevel world) {
        assert world == this.trackedWorldSections.world();
        if (this.timesRegistered == 0) {
            EntitySectionStorage<E> cache = (EntitySectionStorage<E>) ((ServerEntityManagerAccessor) ((ServerWorldAccessor) world).getEntityManager()).getCache();
            WorldSectionBox trackedSections = this.trackedWorldSections;
            int size = trackedSections.numSections();
            assert size > 0;
            this.sortedSections = new ArrayList(size);
            this.sectionVisible = new boolean[size];
            for (int x = trackedSections.chunkX1(); x < trackedSections.chunkX2(); x++) {
                for (int z = trackedSections.chunkZ1(); z < trackedSections.chunkZ2(); z++) {
                    for (int y = trackedSections.chunkY1(); y < trackedSections.chunkY2(); y++) {
                        EntitySection<E> section = cache.getOrCreateSection(SectionPos.asLong(x, y, z));
                        EntityMovementTrackerSection sectionAccess = (EntityMovementTrackerSection) section;
                        this.sortedSections.add(section);
                        sectionAccess.addListener(this);
                    }
                }
            }
            this.setChanged(world.m_46467_());
        }
        this.timesRegistered++;
    }

    public void unRegister(ServerLevel world) {
        assert world == this.trackedWorldSections.world();
        if (--this.timesRegistered <= 0) {
            assert this.timesRegistered == 0;
            EntitySectionStorage<E> cache = (EntitySectionStorage<E>) ((ServerEntityManagerAccessor) ((ServerWorldAccessor) world).getEntityManager()).getCache();
            MovementTrackerCache storage = (MovementTrackerCache) cache;
            storage.remove(this);
            ArrayList<EntitySection<E>> sections = this.sortedSections;
            for (int i = sections.size() - 1; i >= 0; i--) {
                EntitySection<E> section = (EntitySection<E>) sections.get(i);
                EntityMovementTrackerSection sectionAccess = (EntityMovementTrackerSection) section;
                sectionAccess.removeListener(cache, this);
                if (!this.sectionsNotListeningTo.remove(section)) {
                    ((EntityMovementTrackerSection) section).removeListenToMovementOnce(this, this.trackedClass);
                }
            }
            this.setChanged(world.m_46467_());
        }
    }

    public void onSectionEnteredRange(EntityMovementTrackerSection section) {
        this.setChanged(this.trackedWorldSections.world().getGameTime());
        int sectionIndex = this.sortedSections.lastIndexOf(section);
        this.sectionVisible[sectionIndex] = true;
        this.sectionsNotListeningTo.add(section);
        this.notifyAllListeners();
    }

    public void onSectionLeftRange(EntityMovementTrackerSection section) {
        this.setChanged(this.trackedWorldSections.world().getGameTime());
        int sectionIndex = this.sortedSections.lastIndexOf(section);
        this.sectionVisible[sectionIndex] = false;
        if (!this.sectionsNotListeningTo.remove(section)) {
            section.removeListenToMovementOnce(this, this.trackedClass);
            this.notifyAllListeners();
        }
    }

    private void setChanged(long atTime) {
        if (atTime > this.maxChangeTime) {
            this.maxChangeTime = atTime;
        }
    }

    public void listenToEntityMovementOnce(SectionedEntityMovementListener listener) {
        if (this.sectionedEntityMovementListeners == null) {
            this.sectionedEntityMovementListeners = new ReferenceOpenHashSet();
        }
        this.sectionedEntityMovementListeners.add(listener);
        if (!this.sectionsNotListeningTo.isEmpty()) {
            this.setChanged(this.listenToAllSectionsAndGetMaxChangeTime());
        }
    }

    public void emitEntityMovement(int classMask, EntityMovementTrackerSection section) {
        if ((classMask & 1 << this.trackedClass) != 0) {
            this.notifyAllListeners();
            this.sectionsNotListeningTo.add(section);
        }
    }

    private void notifyAllListeners() {
        ReferenceOpenHashSet<SectionedEntityMovementListener> listeners = this.sectionedEntityMovementListeners;
        if (listeners != null && !listeners.isEmpty()) {
            ObjectIterator var2 = listeners.iterator();
            while (var2.hasNext()) {
                SectionedEntityMovementListener listener = (SectionedEntityMovementListener) var2.next();
                listener.handleEntityMovement(this.clazz);
            }
            listeners.clear();
        }
    }
}