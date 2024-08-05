package net.minecraft.client.renderer.chunk;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;

public class RenderChunkRegion implements BlockAndTintGetter {

    private final int centerX;

    private final int centerZ;

    protected final RenderChunk[][] chunks;

    protected final Level level;

    RenderChunkRegion(Level level0, int int1, int int2, RenderChunk[][] renderChunk3) {
        this.level = level0;
        this.centerX = int1;
        this.centerZ = int2;
        this.chunks = renderChunk3;
    }

    @Override
    public BlockState getBlockState(BlockPos blockPos0) {
        int $$1 = SectionPos.blockToSectionCoord(blockPos0.m_123341_()) - this.centerX;
        int $$2 = SectionPos.blockToSectionCoord(blockPos0.m_123343_()) - this.centerZ;
        return this.chunks[$$1][$$2].getBlockState(blockPos0);
    }

    @Override
    public FluidState getFluidState(BlockPos blockPos0) {
        int $$1 = SectionPos.blockToSectionCoord(blockPos0.m_123341_()) - this.centerX;
        int $$2 = SectionPos.blockToSectionCoord(blockPos0.m_123343_()) - this.centerZ;
        return this.chunks[$$1][$$2].getBlockState(blockPos0).m_60819_();
    }

    @Override
    public float getShade(Direction direction0, boolean boolean1) {
        return this.level.m_7717_(direction0, boolean1);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.level.getLightEngine();
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos blockPos0) {
        int $$1 = SectionPos.blockToSectionCoord(blockPos0.m_123341_()) - this.centerX;
        int $$2 = SectionPos.blockToSectionCoord(blockPos0.m_123343_()) - this.centerZ;
        return this.chunks[$$1][$$2].getBlockEntity(blockPos0);
    }

    @Override
    public int getBlockTint(BlockPos blockPos0, ColorResolver colorResolver1) {
        return this.level.m_6171_(blockPos0, colorResolver1);
    }

    @Override
    public int getMinBuildHeight() {
        return this.level.m_141937_();
    }

    @Override
    public int getHeight() {
        return this.level.m_141928_();
    }
}