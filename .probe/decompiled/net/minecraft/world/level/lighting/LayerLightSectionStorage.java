package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;

public abstract class LayerLightSectionStorage<M extends DataLayerStorageMap<M>> {

    private final LightLayer layer;

    protected final LightChunkGetter chunkSource;

    protected final Long2ByteMap sectionStates = new Long2ByteOpenHashMap();

    private final LongSet columnsWithSources = new LongOpenHashSet();

    protected volatile M visibleSectionData;

    protected final M updatingSectionData;

    protected final LongSet changedSections = new LongOpenHashSet();

    protected final LongSet sectionsAffectedByLightUpdates = new LongOpenHashSet();

    protected final Long2ObjectMap<DataLayer> queuedSections = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap());

    private final LongSet columnsToRetainQueuedDataFor = new LongOpenHashSet();

    private final LongSet toRemove = new LongOpenHashSet();

    protected volatile boolean hasInconsistencies;

    protected LayerLightSectionStorage(LightLayer lightLayer0, LightChunkGetter lightChunkGetter1, M m2) {
        this.layer = lightLayer0;
        this.chunkSource = lightChunkGetter1;
        this.updatingSectionData = m2;
        this.visibleSectionData = m2.copy();
        this.visibleSectionData.disableCache();
        this.sectionStates.defaultReturnValue((byte) 0);
    }

    protected boolean storingLightForSection(long long0) {
        return this.getDataLayer(long0, true) != null;
    }

    @Nullable
    protected DataLayer getDataLayer(long long0, boolean boolean1) {
        return this.getDataLayer(boolean1 ? this.updatingSectionData : this.visibleSectionData, long0);
    }

    @Nullable
    protected DataLayer getDataLayer(M m0, long long1) {
        return m0.getLayer(long1);
    }

    @Nullable
    protected DataLayer getDataLayerToWrite(long long0) {
        DataLayer $$1 = this.updatingSectionData.getLayer(long0);
        if ($$1 == null) {
            return null;
        } else {
            if (this.changedSections.add(long0)) {
                $$1 = $$1.copy();
                this.updatingSectionData.setLayer(long0, $$1);
                this.updatingSectionData.clearCache();
            }
            return $$1;
        }
    }

    @Nullable
    public DataLayer getDataLayerData(long long0) {
        DataLayer $$1 = (DataLayer) this.queuedSections.get(long0);
        return $$1 != null ? $$1 : this.getDataLayer(long0, false);
    }

    protected abstract int getLightValue(long var1);

    protected int getStoredLevel(long long0) {
        long $$1 = SectionPos.blockToSection(long0);
        DataLayer $$2 = this.getDataLayer($$1, true);
        return $$2.get(SectionPos.sectionRelative(BlockPos.getX(long0)), SectionPos.sectionRelative(BlockPos.getY(long0)), SectionPos.sectionRelative(BlockPos.getZ(long0)));
    }

    protected void setStoredLevel(long long0, int int1) {
        long $$2 = SectionPos.blockToSection(long0);
        DataLayer $$3;
        if (this.changedSections.add($$2)) {
            $$3 = this.updatingSectionData.copyDataLayer($$2);
        } else {
            $$3 = this.getDataLayer($$2, true);
        }
        $$3.set(SectionPos.sectionRelative(BlockPos.getX(long0)), SectionPos.sectionRelative(BlockPos.getY(long0)), SectionPos.sectionRelative(BlockPos.getZ(long0)), int1);
        SectionPos.aroundAndAtBlockPos(long0, this.sectionsAffectedByLightUpdates::add);
    }

    protected void markSectionAndNeighborsAsAffected(long long0) {
        int $$1 = SectionPos.x(long0);
        int $$2 = SectionPos.y(long0);
        int $$3 = SectionPos.z(long0);
        for (int $$4 = -1; $$4 <= 1; $$4++) {
            for (int $$5 = -1; $$5 <= 1; $$5++) {
                for (int $$6 = -1; $$6 <= 1; $$6++) {
                    this.sectionsAffectedByLightUpdates.add(SectionPos.asLong($$1 + $$5, $$2 + $$6, $$3 + $$4));
                }
            }
        }
    }

    protected DataLayer createDataLayer(long long0) {
        DataLayer $$1 = (DataLayer) this.queuedSections.get(long0);
        return $$1 != null ? $$1 : new DataLayer();
    }

    protected boolean hasInconsistencies() {
        return this.hasInconsistencies;
    }

    protected void markNewInconsistencies(LightEngine<M, ?> lightEngineM0) {
        if (this.hasInconsistencies) {
            this.hasInconsistencies = false;
            LongIterator $$5 = this.toRemove.iterator();
            while ($$5.hasNext()) {
                long $$1 = (Long) $$5.next();
                DataLayer $$2 = (DataLayer) this.queuedSections.remove($$1);
                DataLayer $$3 = this.updatingSectionData.removeLayer($$1);
                if (this.columnsToRetainQueuedDataFor.contains(SectionPos.getZeroNode($$1))) {
                    if ($$2 != null) {
                        this.queuedSections.put($$1, $$2);
                    } else if ($$3 != null) {
                        this.queuedSections.put($$1, $$3);
                    }
                }
            }
            this.updatingSectionData.clearCache();
            $$5 = this.toRemove.iterator();
            while ($$5.hasNext()) {
                long $$4 = (Long) $$5.next();
                this.onNodeRemoved($$4);
                this.changedSections.add($$4);
            }
            this.toRemove.clear();
            ObjectIterator<Entry<DataLayer>> $$5x = Long2ObjectMaps.fastIterator(this.queuedSections);
            while ($$5x.hasNext()) {
                Entry<DataLayer> $$6 = (Entry<DataLayer>) $$5x.next();
                long $$7 = $$6.getLongKey();
                if (this.storingLightForSection($$7)) {
                    DataLayer $$8 = (DataLayer) $$6.getValue();
                    if (this.updatingSectionData.getLayer($$7) != $$8) {
                        this.updatingSectionData.setLayer($$7, $$8);
                        this.changedSections.add($$7);
                    }
                    $$5x.remove();
                }
            }
            this.updatingSectionData.clearCache();
        }
    }

    protected void onNodeAdded(long long0) {
    }

    protected void onNodeRemoved(long long0) {
    }

    protected void setLightEnabled(long long0, boolean boolean1) {
        if (boolean1) {
            this.columnsWithSources.add(long0);
        } else {
            this.columnsWithSources.remove(long0);
        }
    }

    protected boolean lightOnInSection(long long0) {
        long $$1 = SectionPos.getZeroNode(long0);
        return this.columnsWithSources.contains($$1);
    }

    public void retainData(long long0, boolean boolean1) {
        if (boolean1) {
            this.columnsToRetainQueuedDataFor.add(long0);
        } else {
            this.columnsToRetainQueuedDataFor.remove(long0);
        }
    }

    protected void queueSectionData(long long0, @Nullable DataLayer dataLayer1) {
        if (dataLayer1 != null) {
            this.queuedSections.put(long0, dataLayer1);
            this.hasInconsistencies = true;
        } else {
            this.queuedSections.remove(long0);
        }
    }

    protected void updateSectionStatus(long long0, boolean boolean1) {
        byte $$2 = this.sectionStates.get(long0);
        byte $$3 = LayerLightSectionStorage.SectionState.hasData($$2, !boolean1);
        if ($$2 != $$3) {
            this.putSectionState(long0, $$3);
            int $$4 = boolean1 ? -1 : 1;
            for (int $$5 = -1; $$5 <= 1; $$5++) {
                for (int $$6 = -1; $$6 <= 1; $$6++) {
                    for (int $$7 = -1; $$7 <= 1; $$7++) {
                        if ($$5 != 0 || $$6 != 0 || $$7 != 0) {
                            long $$8 = SectionPos.offset(long0, $$5, $$6, $$7);
                            byte $$9 = this.sectionStates.get($$8);
                            this.putSectionState($$8, LayerLightSectionStorage.SectionState.neighborCount($$9, LayerLightSectionStorage.SectionState.neighborCount($$9) + $$4));
                        }
                    }
                }
            }
        }
    }

    protected void putSectionState(long long0, byte byte1) {
        if (byte1 != 0) {
            if (this.sectionStates.put(long0, byte1) == 0) {
                this.initializeSection(long0);
            }
        } else if (this.sectionStates.remove(long0) != 0) {
            this.removeSection(long0);
        }
    }

    private void initializeSection(long long0) {
        if (!this.toRemove.remove(long0)) {
            this.updatingSectionData.setLayer(long0, this.createDataLayer(long0));
            this.changedSections.add(long0);
            this.onNodeAdded(long0);
            this.markSectionAndNeighborsAsAffected(long0);
            this.hasInconsistencies = true;
        }
    }

    private void removeSection(long long0) {
        this.toRemove.add(long0);
        this.hasInconsistencies = true;
    }

    protected void swapSectionMap() {
        if (!this.changedSections.isEmpty()) {
            M $$0 = this.updatingSectionData.copy();
            $$0.disableCache();
            this.visibleSectionData = $$0;
            this.changedSections.clear();
        }
        if (!this.sectionsAffectedByLightUpdates.isEmpty()) {
            LongIterator $$1 = this.sectionsAffectedByLightUpdates.iterator();
            while ($$1.hasNext()) {
                long $$2 = $$1.nextLong();
                this.chunkSource.onLightUpdate(this.layer, SectionPos.of($$2));
            }
            this.sectionsAffectedByLightUpdates.clear();
        }
    }

    public LayerLightSectionStorage.SectionType getDebugSectionType(long long0) {
        return LayerLightSectionStorage.SectionState.type(this.sectionStates.get(long0));
    }

    protected static class SectionState {

        public static final byte EMPTY = 0;

        private static final int MIN_NEIGHBORS = 0;

        private static final int MAX_NEIGHBORS = 26;

        private static final byte HAS_DATA_BIT = 32;

        private static final byte NEIGHBOR_COUNT_BITS = 31;

        public static byte hasData(byte byte0, boolean boolean1) {
            return (byte) (boolean1 ? byte0 | 32 : byte0 & -33);
        }

        public static byte neighborCount(byte byte0, int int1) {
            if (int1 >= 0 && int1 <= 26) {
                return (byte) (byte0 & -32 | int1 & 31);
            } else {
                throw new IllegalArgumentException("Neighbor count was not within range [0; 26]");
            }
        }

        public static boolean hasData(byte byte0) {
            return (byte0 & 32) != 0;
        }

        public static int neighborCount(byte byte0) {
            return byte0 & 31;
        }

        public static LayerLightSectionStorage.SectionType type(byte byte0) {
            if (byte0 == 0) {
                return LayerLightSectionStorage.SectionType.EMPTY;
            } else {
                return hasData(byte0) ? LayerLightSectionStorage.SectionType.LIGHT_AND_DATA : LayerLightSectionStorage.SectionType.LIGHT_ONLY;
            }
        }
    }

    public static enum SectionType {

        EMPTY("2"), LIGHT_ONLY("1"), LIGHT_AND_DATA("0");

        private final String display;

        private SectionType(String p_285063_) {
            this.display = p_285063_;
        }

        public String display() {
            return this.display;
        }
    }
}