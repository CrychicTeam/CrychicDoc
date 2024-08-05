package me.jellysquid.mods.lithium.common.util.tuples;

import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public record WorldSectionBox(Level world, int chunkX1, int chunkY1, int chunkZ1, int chunkX2, int chunkY2, int chunkZ2) {

    public static WorldSectionBox entityAccessBox(Level world, AABB box) {
        int minX = SectionPos.posToSectionCoord(box.minX - 2.0);
        int minY = SectionPos.posToSectionCoord(box.minY - 4.0);
        int minZ = SectionPos.posToSectionCoord(box.minZ - 2.0);
        int maxX = SectionPos.posToSectionCoord(box.maxX + 2.0) + 1;
        int maxY = SectionPos.posToSectionCoord(box.maxY) + 1;
        int maxZ = SectionPos.posToSectionCoord(box.maxZ + 2.0) + 1;
        return new WorldSectionBox(world, minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static WorldSectionBox relevantExpandedBlocksBox(Level world, AABB box) {
        int minX = SectionPos.blockToSectionCoord(Mth.floor(box.minX) - 1);
        int minY = SectionPos.blockToSectionCoord(Mth.floor(box.minY) - 1);
        int minZ = SectionPos.blockToSectionCoord(Mth.floor(box.minZ) - 1);
        int maxX = SectionPos.blockToSectionCoord(Mth.floor(box.maxX) + 1) + 1;
        int maxY = SectionPos.blockToSectionCoord(Mth.floor(box.maxY) + 1) + 1;
        int maxZ = SectionPos.blockToSectionCoord(Mth.floor(box.maxZ) + 1) + 1;
        return new WorldSectionBox(world, minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static WorldSectionBox relevantFluidBox(Level world, AABB box) {
        int minX = SectionPos.blockToSectionCoord(Mth.floor(box.minX));
        int minY = SectionPos.blockToSectionCoord(Mth.floor(box.minY));
        int minZ = SectionPos.blockToSectionCoord(Mth.floor(box.minZ));
        int maxX = SectionPos.blockToSectionCoord(Mth.floor(box.maxX)) + 1;
        int maxY = SectionPos.blockToSectionCoord(Mth.floor(box.maxY)) + 1;
        int maxZ = SectionPos.blockToSectionCoord(Mth.floor(box.maxZ)) + 1;
        return new WorldSectionBox(world, minX, minY, minZ, maxX, maxY, maxZ);
    }

    public int numSections() {
        return (this.chunkX2 - this.chunkX1) * (this.chunkY2 - this.chunkY1) * (this.chunkZ2 - this.chunkZ1);
    }

    public boolean matchesRelevantBlocksBox(AABB box) {
        return SectionPos.blockToSectionCoord(Mth.floor(box.minX) - 1) == this.chunkX1 && SectionPos.blockToSectionCoord(Mth.floor(box.minY) - 1) == this.chunkY1 && SectionPos.blockToSectionCoord(Mth.floor(box.minZ) - 1) == this.chunkZ1 && SectionPos.blockToSectionCoord(Mth.ceil(box.maxX) + 1) + 1 == this.chunkX2 && SectionPos.blockToSectionCoord(Mth.ceil(box.maxY) + 1) + 1 == this.chunkY2 && SectionPos.blockToSectionCoord(Mth.ceil(box.maxZ) + 1) + 1 == this.chunkZ2;
    }
}