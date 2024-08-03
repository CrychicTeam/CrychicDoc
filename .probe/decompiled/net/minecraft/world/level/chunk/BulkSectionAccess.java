package net.minecraft.world.level.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BulkSectionAccess implements AutoCloseable {

    private final LevelAccessor level;

    private final Long2ObjectMap<LevelChunkSection> acquiredSections = new Long2ObjectOpenHashMap();

    @Nullable
    private LevelChunkSection lastSection;

    private long lastSectionKey;

    public BulkSectionAccess(LevelAccessor levelAccessor0) {
        this.level = levelAccessor0;
    }

    @Nullable
    public LevelChunkSection getSection(BlockPos blockPos0) {
        int $$1 = this.level.m_151564_(blockPos0.m_123342_());
        if ($$1 >= 0 && $$1 < this.level.m_151559_()) {
            long $$2 = SectionPos.asLong(blockPos0);
            if (this.lastSection == null || this.lastSectionKey != $$2) {
                this.lastSection = (LevelChunkSection) this.acquiredSections.computeIfAbsent($$2, p_156109_ -> {
                    ChunkAccess $$3 = this.level.m_6325_(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()));
                    LevelChunkSection $$4 = $$3.getSection($$1);
                    $$4.acquire();
                    return $$4;
                });
                this.lastSectionKey = $$2;
            }
            return this.lastSection;
        } else {
            return null;
        }
    }

    public BlockState getBlockState(BlockPos blockPos0) {
        LevelChunkSection $$1 = this.getSection(blockPos0);
        if ($$1 == null) {
            return Blocks.AIR.defaultBlockState();
        } else {
            int $$2 = SectionPos.sectionRelative(blockPos0.m_123341_());
            int $$3 = SectionPos.sectionRelative(blockPos0.m_123342_());
            int $$4 = SectionPos.sectionRelative(blockPos0.m_123343_());
            return $$1.getBlockState($$2, $$3, $$4);
        }
    }

    public void close() {
        ObjectIterator var1 = this.acquiredSections.values().iterator();
        while (var1.hasNext()) {
            LevelChunkSection $$0 = (LevelChunkSection) var1.next();
            $$0.release();
        }
    }
}