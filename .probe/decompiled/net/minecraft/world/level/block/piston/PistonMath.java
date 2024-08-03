package net.minecraft.world.level.block.piston;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public class PistonMath {

    public static AABB getMovementArea(AABB aABB0, Direction direction1, double double2) {
        double $$3 = double2 * (double) direction1.getAxisDirection().getStep();
        double $$4 = Math.min($$3, 0.0);
        double $$5 = Math.max($$3, 0.0);
        switch(direction1) {
            case WEST:
                return new AABB(aABB0.minX + $$4, aABB0.minY, aABB0.minZ, aABB0.minX + $$5, aABB0.maxY, aABB0.maxZ);
            case EAST:
                return new AABB(aABB0.maxX + $$4, aABB0.minY, aABB0.minZ, aABB0.maxX + $$5, aABB0.maxY, aABB0.maxZ);
            case DOWN:
                return new AABB(aABB0.minX, aABB0.minY + $$4, aABB0.minZ, aABB0.maxX, aABB0.minY + $$5, aABB0.maxZ);
            case UP:
            default:
                return new AABB(aABB0.minX, aABB0.maxY + $$4, aABB0.minZ, aABB0.maxX, aABB0.maxY + $$5, aABB0.maxZ);
            case NORTH:
                return new AABB(aABB0.minX, aABB0.minY, aABB0.minZ + $$4, aABB0.maxX, aABB0.maxY, aABB0.minZ + $$5);
            case SOUTH:
                return new AABB(aABB0.minX, aABB0.minY, aABB0.maxZ + $$4, aABB0.maxX, aABB0.maxY, aABB0.maxZ + $$5);
        }
    }
}