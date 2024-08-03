package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

public interface LevelHeightAccessor {

    int getHeight();

    int getMinBuildHeight();

    default int getMaxBuildHeight() {
        return this.getMinBuildHeight() + this.getHeight();
    }

    default int getSectionsCount() {
        return this.getMaxSection() - this.getMinSection();
    }

    default int getMinSection() {
        return SectionPos.blockToSectionCoord(this.getMinBuildHeight());
    }

    default int getMaxSection() {
        return SectionPos.blockToSectionCoord(this.getMaxBuildHeight() - 1) + 1;
    }

    default boolean isOutsideBuildHeight(BlockPos blockPos0) {
        return this.isOutsideBuildHeight(blockPos0.m_123342_());
    }

    default boolean isOutsideBuildHeight(int int0) {
        return int0 < this.getMinBuildHeight() || int0 >= this.getMaxBuildHeight();
    }

    default int getSectionIndex(int int0) {
        return this.getSectionIndexFromSectionY(SectionPos.blockToSectionCoord(int0));
    }

    default int getSectionIndexFromSectionY(int int0) {
        return int0 - this.getMinSection();
    }

    default int getSectionYFromSectionIndex(int int0) {
        return int0 + this.getMinSection();
    }

    static LevelHeightAccessor create(final int int0, final int int1) {
        return new LevelHeightAccessor() {

            @Override
            public int getHeight() {
                return int1;
            }

            @Override
            public int getMinBuildHeight() {
                return int0;
            }
        };
    }
}