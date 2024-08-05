package net.minecraft.client.renderer;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public class ViewArea {

    protected final LevelRenderer levelRenderer;

    protected final Level level;

    protected int chunkGridSizeY;

    protected int chunkGridSizeX;

    protected int chunkGridSizeZ;

    public ChunkRenderDispatcher.RenderChunk[] chunks;

    public ViewArea(ChunkRenderDispatcher chunkRenderDispatcher0, Level level1, int int2, LevelRenderer levelRenderer3) {
        this.levelRenderer = levelRenderer3;
        this.level = level1;
        this.setViewDistance(int2);
        this.createChunks(chunkRenderDispatcher0);
    }

    protected void createChunks(ChunkRenderDispatcher chunkRenderDispatcher0) {
        if (!Minecraft.getInstance().m_18695_()) {
            throw new IllegalStateException("createChunks called from wrong thread: " + Thread.currentThread().getName());
        } else {
            int $$1 = this.chunkGridSizeX * this.chunkGridSizeY * this.chunkGridSizeZ;
            this.chunks = new ChunkRenderDispatcher.RenderChunk[$$1];
            for (int $$2 = 0; $$2 < this.chunkGridSizeX; $$2++) {
                for (int $$3 = 0; $$3 < this.chunkGridSizeY; $$3++) {
                    for (int $$4 = 0; $$4 < this.chunkGridSizeZ; $$4++) {
                        int $$5 = this.getChunkIndex($$2, $$3, $$4);
                        this.chunks[$$5] = chunkRenderDispatcher0.new RenderChunk($$5, $$2 * 16, $$3 * 16, $$4 * 16);
                    }
                }
            }
        }
    }

    public void releaseAllBuffers() {
        for (ChunkRenderDispatcher.RenderChunk $$0 : this.chunks) {
            $$0.releaseBuffers();
        }
    }

    private int getChunkIndex(int int0, int int1, int int2) {
        return (int2 * this.chunkGridSizeY + int1) * this.chunkGridSizeX + int0;
    }

    protected void setViewDistance(int int0) {
        int $$1 = int0 * 2 + 1;
        this.chunkGridSizeX = $$1;
        this.chunkGridSizeY = this.level.m_151559_();
        this.chunkGridSizeZ = $$1;
    }

    public void repositionCamera(double double0, double double1) {
        int $$2 = Mth.ceil(double0);
        int $$3 = Mth.ceil(double1);
        for (int $$4 = 0; $$4 < this.chunkGridSizeX; $$4++) {
            int $$5 = this.chunkGridSizeX * 16;
            int $$6 = $$2 - 8 - $$5 / 2;
            int $$7 = $$6 + Math.floorMod($$4 * 16 - $$6, $$5);
            for (int $$8 = 0; $$8 < this.chunkGridSizeZ; $$8++) {
                int $$9 = this.chunkGridSizeZ * 16;
                int $$10 = $$3 - 8 - $$9 / 2;
                int $$11 = $$10 + Math.floorMod($$8 * 16 - $$10, $$9);
                for (int $$12 = 0; $$12 < this.chunkGridSizeY; $$12++) {
                    int $$13 = this.level.m_141937_() + $$12 * 16;
                    ChunkRenderDispatcher.RenderChunk $$14 = this.chunks[this.getChunkIndex($$4, $$12, $$8)];
                    BlockPos $$15 = $$14.getOrigin();
                    if ($$7 != $$15.m_123341_() || $$13 != $$15.m_123342_() || $$11 != $$15.m_123343_()) {
                        $$14.setOrigin($$7, $$13, $$11);
                    }
                }
            }
        }
    }

    public void setDirty(int int0, int int1, int int2, boolean boolean3) {
        int $$4 = Math.floorMod(int0, this.chunkGridSizeX);
        int $$5 = Math.floorMod(int1 - this.level.m_151560_(), this.chunkGridSizeY);
        int $$6 = Math.floorMod(int2, this.chunkGridSizeZ);
        ChunkRenderDispatcher.RenderChunk $$7 = this.chunks[this.getChunkIndex($$4, $$5, $$6)];
        $$7.setDirty(boolean3);
    }

    @Nullable
    protected ChunkRenderDispatcher.RenderChunk getRenderChunkAt(BlockPos blockPos0) {
        int $$1 = Mth.floorDiv(blockPos0.m_123341_(), 16);
        int $$2 = Mth.floorDiv(blockPos0.m_123342_() - this.level.m_141937_(), 16);
        int $$3 = Mth.floorDiv(blockPos0.m_123343_(), 16);
        if ($$2 >= 0 && $$2 < this.chunkGridSizeY) {
            $$1 = Mth.positiveModulo($$1, this.chunkGridSizeX);
            $$3 = Mth.positiveModulo($$3, this.chunkGridSizeZ);
            return this.chunks[this.getChunkIndex($$1, $$2, $$3)];
        } else {
            return null;
        }
    }
}