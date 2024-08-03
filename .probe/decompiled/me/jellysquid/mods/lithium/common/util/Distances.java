package me.jellysquid.mods.lithium.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;

public class Distances {

    public static double getMinChunkToBlockDistanceL2Sq(BlockPos origin, int chunkX, int chunkZ) {
        int chunkMinX = SectionPos.sectionToBlockCoord(chunkX);
        int chunkMinZ = SectionPos.sectionToBlockCoord(chunkZ);
        int xDistance = origin.m_123341_() - chunkMinX;
        if (xDistance > 0) {
            xDistance = Math.max(0, xDistance - 15);
        }
        int zDistance = origin.m_123343_() - chunkMinZ;
        if (zDistance > 0) {
            zDistance = Math.max(0, zDistance - 15);
        }
        return (double) (xDistance * xDistance + zDistance * zDistance);
    }

    public static boolean isWithinSquareRadius(BlockPos origin, int radius, BlockPos pos) {
        return Math.abs(pos.m_123341_() - origin.m_123341_()) <= radius && Math.abs(pos.m_123343_() - origin.m_123343_()) <= radius;
    }

    public static boolean isWithinCircleRadius(BlockPos origin, double radiusSq, BlockPos pos) {
        return origin.m_123331_(pos) <= radiusSq;
    }
}