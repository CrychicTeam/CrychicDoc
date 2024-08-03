package net.minecraft.world.level.storage;

import net.minecraft.core.BlockPos;

public interface WritableLevelData extends LevelData {

    void setXSpawn(int var1);

    void setYSpawn(int var1);

    void setZSpawn(int var1);

    void setSpawnAngle(float var1);

    default void setSpawn(BlockPos blockPos0, float float1) {
        this.setXSpawn(blockPos0.m_123341_());
        this.setYSpawn(blockPos0.m_123342_());
        this.setZSpawn(blockPos0.m_123343_());
        this.setSpawnAngle(float1);
    }
}