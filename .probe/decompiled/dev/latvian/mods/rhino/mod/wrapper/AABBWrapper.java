package dev.latvian.mods.rhino.mod.wrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public interface AABBWrapper {

    AABB EMPTY = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    AABB CUBE = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

    static AABB of(double x0, double y0, double z0, double x1, double y1, double z1) {
        return new AABB(x0, y0, z0, x1, y1, z1);
    }

    static AABB ofBlocks(BlockPos pos1, BlockPos pos2) {
        return of((double) pos1.m_123341_(), (double) pos1.m_123342_(), (double) pos1.m_123343_(), (double) pos2.m_123341_() + 1.0, (double) pos2.m_123342_() + 1.0, (double) pos2.m_123343_() + 1.0);
    }

    static AABB ofBlock(BlockPos pos) {
        return ofBlocks(pos, pos);
    }

    static AABB ofSize(double x, double y, double z) {
        return ofSize(Vec3.ZERO, x, y, z);
    }

    static AABB ofSize(Vec3 vec3, double x, double y, double z) {
        return AABB.ofSize(vec3, x, y, z);
    }

    static AABB wrap(Object o) {
        if (o instanceof AABB) {
            return (AABB) o;
        } else if (o instanceof BlockPos) {
            return ofBlock((BlockPos) o);
        } else {
            if (o instanceof double[] d) {
                if (d.length == 0) {
                    return EMPTY;
                }
                if (d.length == 3) {
                    return ofSize(d[0], d[1], d[2]);
                }
                if (d.length == 6) {
                    return of(d[0], d[1], d[2], d[3], d[4], d[5]);
                }
            }
            return EMPTY;
        }
    }
}