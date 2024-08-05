package com.simibubi.create.foundation.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class BBHelper {

    public static BoundingBox encapsulate(BoundingBox bb, BlockPos pos) {
        return new BoundingBox(Math.min(bb.minX(), pos.m_123341_()), Math.min(bb.minY(), pos.m_123342_()), Math.min(bb.minZ(), pos.m_123343_()), Math.max(bb.maxX(), pos.m_123341_()), Math.max(bb.maxY(), pos.m_123342_()), Math.max(bb.maxZ(), pos.m_123343_()));
    }

    public static BoundingBox encapsulate(BoundingBox bb, BoundingBox bb2) {
        return new BoundingBox(Math.min(bb.minX(), bb2.minX()), Math.min(bb.minY(), bb2.minY()), Math.min(bb.minZ(), bb2.minZ()), Math.max(bb.maxX(), bb2.maxX()), Math.max(bb.maxY(), bb2.maxY()), Math.max(bb.maxZ(), bb2.maxZ()));
    }
}