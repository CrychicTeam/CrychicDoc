package net.minecraft.world.level.lighting;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;

public class BlockLightSectionStorage extends LayerLightSectionStorage<BlockLightSectionStorage.BlockDataLayerStorageMap> {

    protected BlockLightSectionStorage(LightChunkGetter lightChunkGetter0) {
        super(LightLayer.BLOCK, lightChunkGetter0, new BlockLightSectionStorage.BlockDataLayerStorageMap(new Long2ObjectOpenHashMap()));
    }

    @Override
    protected int getLightValue(long long0) {
        long $$1 = SectionPos.blockToSection(long0);
        DataLayer $$2 = this.m_75758_($$1, false);
        return $$2 == null ? 0 : $$2.get(SectionPos.sectionRelative(BlockPos.getX(long0)), SectionPos.sectionRelative(BlockPos.getY(long0)), SectionPos.sectionRelative(BlockPos.getZ(long0)));
    }

    protected static final class BlockDataLayerStorageMap extends DataLayerStorageMap<BlockLightSectionStorage.BlockDataLayerStorageMap> {

        public BlockDataLayerStorageMap(Long2ObjectOpenHashMap<DataLayer> longObjectOpenHashMapDataLayer0) {
            super(longObjectOpenHashMapDataLayer0);
        }

        public BlockLightSectionStorage.BlockDataLayerStorageMap copy() {
            return new BlockLightSectionStorage.BlockDataLayerStorageMap(this.f_75518_.clone());
        }
    }
}