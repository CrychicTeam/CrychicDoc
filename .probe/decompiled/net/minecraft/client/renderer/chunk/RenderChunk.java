package net.minecraft.client.renderer.chunk;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.DebugLevelSource;

class RenderChunk {

    private final Map<BlockPos, BlockEntity> blockEntities;

    @Nullable
    private final List<PalettedContainer<BlockState>> sections;

    private final boolean debug;

    private final LevelChunk wrapped;

    RenderChunk(LevelChunk levelChunk0) {
        this.wrapped = levelChunk0;
        this.debug = levelChunk0.getLevel().isDebug();
        this.blockEntities = ImmutableMap.copyOf(levelChunk0.getBlockEntities());
        if (levelChunk0 instanceof EmptyLevelChunk) {
            this.sections = null;
        } else {
            LevelChunkSection[] $$1 = levelChunk0.m_7103_();
            this.sections = new ArrayList($$1.length);
            for (LevelChunkSection $$2 : $$1) {
                this.sections.add($$2.hasOnlyAir() ? null : $$2.getStates().copy());
            }
        }
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockPos blockPos0) {
        return (BlockEntity) this.blockEntities.get(blockPos0);
    }

    public BlockState getBlockState(BlockPos blockPos0) {
        int $$1 = blockPos0.m_123341_();
        int $$2 = blockPos0.m_123342_();
        int $$3 = blockPos0.m_123343_();
        if (this.debug) {
            BlockState $$4 = null;
            if ($$2 == 60) {
                $$4 = Blocks.BARRIER.defaultBlockState();
            }
            if ($$2 == 70) {
                $$4 = DebugLevelSource.getBlockStateFor($$1, $$3);
            }
            return $$4 == null ? Blocks.AIR.defaultBlockState() : $$4;
        } else if (this.sections == null) {
            return Blocks.AIR.defaultBlockState();
        } else {
            try {
                int $$5 = this.wrapped.m_151564_($$2);
                if ($$5 >= 0 && $$5 < this.sections.size()) {
                    PalettedContainer<BlockState> $$6 = (PalettedContainer<BlockState>) this.sections.get($$5);
                    if ($$6 != null) {
                        return $$6.get($$1 & 15, $$2 & 15, $$3 & 15);
                    }
                }
                return Blocks.AIR.defaultBlockState();
            } catch (Throwable var8) {
                CrashReport $$8 = CrashReport.forThrowable(var8, "Getting block state");
                CrashReportCategory $$9 = $$8.addCategory("Block being got");
                $$9.setDetail("Location", (CrashReportDetail<String>) (() -> CrashReportCategory.formatLocation(this.wrapped, $$1, $$2, $$3)));
                throw new ReportedException($$8);
            }
        }
    }
}