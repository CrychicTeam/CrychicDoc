package net.minecraft.world.level.lighting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;

public interface LightEventListener {

    void checkBlock(BlockPos var1);

    boolean hasLightWork();

    int runLightUpdates();

    default void updateSectionStatus(BlockPos blockPos0, boolean boolean1) {
        this.updateSectionStatus(SectionPos.of(blockPos0), boolean1);
    }

    void updateSectionStatus(SectionPos var1, boolean var2);

    void setLightEnabled(ChunkPos var1, boolean var2);

    void propagateLightSources(ChunkPos var1);
}