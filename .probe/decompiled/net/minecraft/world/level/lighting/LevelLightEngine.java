package net.minecraft.world.level.lighting;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;

public class LevelLightEngine implements LightEventListener {

    public static final int LIGHT_SECTION_PADDING = 1;

    protected final LevelHeightAccessor levelHeightAccessor;

    @Nullable
    private final LightEngine<?, ?> blockEngine;

    @Nullable
    private final LightEngine<?, ?> skyEngine;

    public LevelLightEngine(LightChunkGetter lightChunkGetter0, boolean boolean1, boolean boolean2) {
        this.levelHeightAccessor = lightChunkGetter0.getLevel();
        this.blockEngine = boolean1 ? new BlockLightEngine(lightChunkGetter0) : null;
        this.skyEngine = boolean2 ? new SkyLightEngine(lightChunkGetter0) : null;
    }

    @Override
    public void checkBlock(BlockPos blockPos0) {
        if (this.blockEngine != null) {
            this.blockEngine.checkBlock(blockPos0);
        }
        if (this.skyEngine != null) {
            this.skyEngine.checkBlock(blockPos0);
        }
    }

    @Override
    public boolean hasLightWork() {
        return this.skyEngine != null && this.skyEngine.hasLightWork() ? true : this.blockEngine != null && this.blockEngine.hasLightWork();
    }

    @Override
    public int runLightUpdates() {
        int $$0 = 0;
        if (this.blockEngine != null) {
            $$0 += this.blockEngine.runLightUpdates();
        }
        if (this.skyEngine != null) {
            $$0 += this.skyEngine.runLightUpdates();
        }
        return $$0;
    }

    @Override
    public void updateSectionStatus(SectionPos sectionPos0, boolean boolean1) {
        if (this.blockEngine != null) {
            this.blockEngine.updateSectionStatus(sectionPos0, boolean1);
        }
        if (this.skyEngine != null) {
            this.skyEngine.updateSectionStatus(sectionPos0, boolean1);
        }
    }

    @Override
    public void setLightEnabled(ChunkPos chunkPos0, boolean boolean1) {
        if (this.blockEngine != null) {
            this.blockEngine.setLightEnabled(chunkPos0, boolean1);
        }
        if (this.skyEngine != null) {
            this.skyEngine.setLightEnabled(chunkPos0, boolean1);
        }
    }

    @Override
    public void propagateLightSources(ChunkPos chunkPos0) {
        if (this.blockEngine != null) {
            this.blockEngine.m_142519_(chunkPos0);
        }
        if (this.skyEngine != null) {
            this.skyEngine.m_142519_(chunkPos0);
        }
    }

    public LayerLightEventListener getLayerListener(LightLayer lightLayer0) {
        if (lightLayer0 == LightLayer.BLOCK) {
            return (LayerLightEventListener) (this.blockEngine == null ? LayerLightEventListener.DummyLightLayerEventListener.INSTANCE : this.blockEngine);
        } else {
            return (LayerLightEventListener) (this.skyEngine == null ? LayerLightEventListener.DummyLightLayerEventListener.INSTANCE : this.skyEngine);
        }
    }

    public String getDebugData(LightLayer lightLayer0, SectionPos sectionPos1) {
        if (lightLayer0 == LightLayer.BLOCK) {
            if (this.blockEngine != null) {
                return this.blockEngine.getDebugData(sectionPos1.asLong());
            }
        } else if (this.skyEngine != null) {
            return this.skyEngine.getDebugData(sectionPos1.asLong());
        }
        return "n/a";
    }

    public LayerLightSectionStorage.SectionType getDebugSectionType(LightLayer lightLayer0, SectionPos sectionPos1) {
        if (lightLayer0 == LightLayer.BLOCK) {
            if (this.blockEngine != null) {
                return this.blockEngine.getDebugSectionType(sectionPos1.asLong());
            }
        } else if (this.skyEngine != null) {
            return this.skyEngine.getDebugSectionType(sectionPos1.asLong());
        }
        return LayerLightSectionStorage.SectionType.EMPTY;
    }

    public void queueSectionData(LightLayer lightLayer0, SectionPos sectionPos1, @Nullable DataLayer dataLayer2) {
        if (lightLayer0 == LightLayer.BLOCK) {
            if (this.blockEngine != null) {
                this.blockEngine.queueSectionData(sectionPos1.asLong(), dataLayer2);
            }
        } else if (this.skyEngine != null) {
            this.skyEngine.queueSectionData(sectionPos1.asLong(), dataLayer2);
        }
    }

    public void retainData(ChunkPos chunkPos0, boolean boolean1) {
        if (this.blockEngine != null) {
            this.blockEngine.retainData(chunkPos0, boolean1);
        }
        if (this.skyEngine != null) {
            this.skyEngine.retainData(chunkPos0, boolean1);
        }
    }

    public int getRawBrightness(BlockPos blockPos0, int int1) {
        int $$2 = this.skyEngine == null ? 0 : this.skyEngine.getLightValue(blockPos0) - int1;
        int $$3 = this.blockEngine == null ? 0 : this.blockEngine.getLightValue(blockPos0);
        return Math.max($$3, $$2);
    }

    public boolean lightOnInSection(SectionPos sectionPos0) {
        long $$1 = sectionPos0.asLong();
        return this.blockEngine == null || this.blockEngine.storage.lightOnInSection($$1) && (this.skyEngine == null || this.skyEngine.storage.lightOnInSection($$1));
    }

    public int getLightSectionCount() {
        return this.levelHeightAccessor.getSectionsCount() + 2;
    }

    public int getMinLightSection() {
        return this.levelHeightAccessor.getMinSection() - 1;
    }

    public int getMaxLightSection() {
        return this.getMinLightSection() + this.getLightSectionCount();
    }
}