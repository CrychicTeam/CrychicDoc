package me.jellysquid.mods.lithium.mixin.world.inline_height;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ LevelChunk.class })
public abstract class WorldChunkMixin implements LevelHeightAccessor {

    @Shadow
    @Final
    Level level;

    @Override
    public int getMaxBuildHeight() {
        return this.level.m_151558_();
    }

    @Override
    public int getSectionsCount() {
        return this.level.m_151559_();
    }

    @Override
    public int getMinSection() {
        return this.level.m_151560_();
    }

    @Override
    public int getMaxSection() {
        return this.level.m_151561_();
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPos pos) {
        return this.level.m_151570_(pos);
    }

    @Override
    public boolean isOutsideBuildHeight(int y) {
        return this.level.m_151562_(y);
    }

    @Override
    public int getSectionIndex(int y) {
        return this.level.m_151564_(y);
    }

    @Override
    public int getSectionIndexFromSectionY(int coord) {
        return this.level.m_151566_(coord);
    }

    @Override
    public int getSectionYFromSectionIndex(int index) {
        return this.level.m_151568_(index);
    }
}