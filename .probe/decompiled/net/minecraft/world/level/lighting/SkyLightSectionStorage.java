package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;

public class SkyLightSectionStorage extends LayerLightSectionStorage<SkyLightSectionStorage.SkyDataLayerStorageMap> {

    protected SkyLightSectionStorage(LightChunkGetter lightChunkGetter0) {
        super(LightLayer.SKY, lightChunkGetter0, new SkyLightSectionStorage.SkyDataLayerStorageMap(new Long2ObjectOpenHashMap(), new Long2IntOpenHashMap(), Integer.MAX_VALUE));
    }

    @Override
    protected int getLightValue(long long0) {
        return this.getLightValue(long0, false);
    }

    protected int getLightValue(long long0, boolean boolean1) {
        long $$2 = SectionPos.blockToSection(long0);
        int $$3 = SectionPos.y($$2);
        SkyLightSectionStorage.SkyDataLayerStorageMap $$4 = boolean1 ? (SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_ : (SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75731_;
        int $$5 = $$4.topSections.get(SectionPos.getZeroNode($$2));
        if ($$5 != $$4.currentLowestY && $$3 < $$5) {
            DataLayer $$6 = this.m_75761_($$4, $$2);
            if ($$6 == null) {
                for (long0 = BlockPos.getFlatIndex(long0); $$6 == null; $$6 = this.m_75761_($$4, $$2)) {
                    if (++$$3 >= $$5) {
                        return 15;
                    }
                    $$2 = SectionPos.offset($$2, Direction.UP);
                }
            }
            return $$6.get(SectionPos.sectionRelative(BlockPos.getX(long0)), SectionPos.sectionRelative(BlockPos.getY(long0)), SectionPos.sectionRelative(BlockPos.getZ(long0)));
        } else {
            return boolean1 && !this.m_284382_($$2) ? 0 : 15;
        }
    }

    @Override
    protected void onNodeAdded(long long0) {
        int $$1 = SectionPos.y(long0);
        if (((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).currentLowestY > $$1) {
            ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).currentLowestY = $$1;
            ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).topSections.defaultReturnValue(((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).currentLowestY);
        }
        long $$2 = SectionPos.getZeroNode(long0);
        int $$3 = ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).topSections.get($$2);
        if ($$3 < $$1 + 1) {
            ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).topSections.put($$2, $$1 + 1);
        }
    }

    @Override
    protected void onNodeRemoved(long long0) {
        long $$1 = SectionPos.getZeroNode(long0);
        int $$2 = SectionPos.y(long0);
        if (((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).topSections.get($$1) == $$2 + 1) {
            long $$3;
            for ($$3 = long0; !this.m_75791_($$3) && this.hasLightDataAtOrBelow($$2); $$3 = SectionPos.offset($$3, Direction.DOWN)) {
                $$2--;
            }
            if (this.m_75791_($$3)) {
                ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).topSections.put($$1, $$2 + 1);
            } else {
                ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).topSections.remove($$1);
            }
        }
    }

    @Override
    protected DataLayer createDataLayer(long long0) {
        DataLayer $$1 = (DataLayer) this.f_75735_.get(long0);
        if ($$1 != null) {
            return $$1;
        } else {
            int $$2 = ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).topSections.get(SectionPos.getZeroNode(long0));
            if ($$2 != ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).currentLowestY && SectionPos.y(long0) < $$2) {
                long $$3 = SectionPos.offset(long0, Direction.UP);
                DataLayer $$4;
                while (($$4 = this.m_75758_($$3, true)) == null) {
                    $$3 = SectionPos.offset($$3, Direction.UP);
                }
                return repeatFirstLayer($$4);
            } else {
                return this.m_284382_(long0) ? new DataLayer(15) : new DataLayer();
            }
        }
    }

    private static DataLayer repeatFirstLayer(DataLayer dataLayer0) {
        if (dataLayer0.isDefinitelyHomogenous()) {
            return dataLayer0.copy();
        } else {
            byte[] $$1 = dataLayer0.getData();
            byte[] $$2 = new byte[2048];
            for (int $$3 = 0; $$3 < 16; $$3++) {
                System.arraycopy($$1, 0, $$2, $$3 * 128, 128);
            }
            return new DataLayer($$2);
        }
    }

    protected boolean hasLightDataAtOrBelow(int int0) {
        return int0 >= ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).currentLowestY;
    }

    protected boolean isAboveData(long long0) {
        long $$1 = SectionPos.getZeroNode(long0);
        int $$2 = ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).topSections.get($$1);
        return $$2 == ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).currentLowestY || SectionPos.y(long0) >= $$2;
    }

    protected int getTopSectionY(long long0) {
        return ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).topSections.get(long0);
    }

    protected int getBottomSectionY() {
        return ((SkyLightSectionStorage.SkyDataLayerStorageMap) this.f_75732_).currentLowestY;
    }

    protected static final class SkyDataLayerStorageMap extends DataLayerStorageMap<SkyLightSectionStorage.SkyDataLayerStorageMap> {

        int currentLowestY;

        final Long2IntOpenHashMap topSections;

        public SkyDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> longObjectOpenHashMapDataLayer0, Long2IntOpenHashMap longIntOpenHashMap1, int int2) {
            super(longObjectOpenHashMapDataLayer0);
            this.topSections = longIntOpenHashMap1;
            longIntOpenHashMap1.defaultReturnValue(int2);
            this.currentLowestY = int2;
        }

        public SkyLightSectionStorage.SkyDataLayerStorageMap copy() {
            return new SkyLightSectionStorage.SkyDataLayerStorageMap(this.f_75518_.clone(), this.topSections.clone(), this.currentLowestY);
        }
    }
}