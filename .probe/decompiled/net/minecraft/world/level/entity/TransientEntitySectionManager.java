package net.minecraft.world.level.entity;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import org.slf4j.Logger;

public class TransientEntitySectionManager<T extends EntityAccess> {

    static final Logger LOGGER = LogUtils.getLogger();

    final LevelCallback<T> callbacks;

    final EntityLookup<T> entityStorage;

    final EntitySectionStorage<T> sectionStorage;

    private final LongSet tickingChunks = new LongOpenHashSet();

    private final LevelEntityGetter<T> entityGetter;

    public TransientEntitySectionManager(Class<T> classT0, LevelCallback<T> levelCallbackT1) {
        this.entityStorage = new EntityLookup<>();
        this.sectionStorage = new EntitySectionStorage<>(classT0, p_157647_ -> this.tickingChunks.contains(p_157647_) ? Visibility.TICKING : Visibility.TRACKED);
        this.callbacks = levelCallbackT1;
        this.entityGetter = new LevelEntityGetterAdapter<>(this.entityStorage, this.sectionStorage);
    }

    public void startTicking(ChunkPos chunkPos0) {
        long $$1 = chunkPos0.toLong();
        this.tickingChunks.add($$1);
        this.sectionStorage.getExistingSectionsInChunk($$1).forEach(p_157663_ -> {
            Visibility $$1x = p_157663_.updateChunkStatus(Visibility.TICKING);
            if (!$$1x.isTicking()) {
                p_157663_.getEntities().filter(p_157666_ -> !p_157666_.isAlwaysTicking()).forEach(this.callbacks::m_141987_);
            }
        });
    }

    public void stopTicking(ChunkPos chunkPos0) {
        long $$1 = chunkPos0.toLong();
        this.tickingChunks.remove($$1);
        this.sectionStorage.getExistingSectionsInChunk($$1).forEach(p_157656_ -> {
            Visibility $$1x = p_157656_.updateChunkStatus(Visibility.TRACKED);
            if ($$1x.isTicking()) {
                p_157656_.getEntities().filter(p_157661_ -> !p_157661_.isAlwaysTicking()).forEach(this.callbacks::m_141983_);
            }
        });
    }

    public LevelEntityGetter<T> getEntityGetter() {
        return this.entityGetter;
    }

    public void addEntity(T t0) {
        this.entityStorage.add(t0);
        long $$1 = SectionPos.asLong(t0.blockPosition());
        EntitySection<T> $$2 = this.sectionStorage.getOrCreateSection($$1);
        $$2.add(t0);
        t0.setLevelCallback(new TransientEntitySectionManager.Callback(t0, $$1, $$2));
        this.callbacks.onCreated(t0);
        this.callbacks.onTrackingStart(t0);
        if (t0.isAlwaysTicking() || $$2.getStatus().isTicking()) {
            this.callbacks.onTickingStart(t0);
        }
    }

    @VisibleForDebug
    public int count() {
        return this.entityStorage.count();
    }

    void removeSectionIfEmpty(long long0, EntitySection<T> entitySectionT1) {
        if (entitySectionT1.isEmpty()) {
            this.sectionStorage.remove(long0);
        }
    }

    @VisibleForDebug
    public String gatherStats() {
        return this.entityStorage.count() + "," + this.sectionStorage.count() + "," + this.tickingChunks.size();
    }

    class Callback implements EntityInLevelCallback {

        private final T entity;

        private long currentSectionKey;

        private EntitySection<T> currentSection;

        Callback(T t0, long long1, EntitySection<T> entitySectionT2) {
            this.entity = t0;
            this.currentSectionKey = long1;
            this.currentSection = entitySectionT2;
        }

        @Override
        public void onMove() {
            BlockPos $$0 = this.entity.blockPosition();
            long $$1 = SectionPos.asLong($$0);
            if ($$1 != this.currentSectionKey) {
                Visibility $$2 = this.currentSection.getStatus();
                if (!this.currentSection.remove(this.entity)) {
                    TransientEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (moving to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), $$1 });
                }
                TransientEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
                EntitySection<T> $$3 = TransientEntitySectionManager.this.sectionStorage.getOrCreateSection($$1);
                $$3.add(this.entity);
                this.currentSection = $$3;
                this.currentSectionKey = $$1;
                TransientEntitySectionManager.this.callbacks.onSectionChange(this.entity);
                if (!this.entity.isAlwaysTicking()) {
                    boolean $$4 = $$2.isTicking();
                    boolean $$5 = $$3.getStatus().isTicking();
                    if ($$4 && !$$5) {
                        TransientEntitySectionManager.this.callbacks.onTickingEnd(this.entity);
                    } else if (!$$4 && $$5) {
                        TransientEntitySectionManager.this.callbacks.onTickingStart(this.entity);
                    }
                }
            }
        }

        @Override
        public void onRemove(Entity.RemovalReason entityRemovalReason0) {
            if (!this.currentSection.remove(this.entity)) {
                TransientEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (destroying due to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), entityRemovalReason0 });
            }
            Visibility $$1 = this.currentSection.getStatus();
            if ($$1.isTicking() || this.entity.isAlwaysTicking()) {
                TransientEntitySectionManager.this.callbacks.onTickingEnd(this.entity);
            }
            TransientEntitySectionManager.this.callbacks.onTrackingEnd(this.entity);
            TransientEntitySectionManager.this.callbacks.onDestroyed(this.entity);
            TransientEntitySectionManager.this.entityStorage.remove(this.entity);
            this.entity.setLevelCallback(f_156799_);
            TransientEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
        }
    }
}