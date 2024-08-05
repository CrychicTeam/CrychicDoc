package org.violetmoon.quark.addons.oddities.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

public record Force(int magnitude, boolean pushing, Direction direction, int distance, BlockPos origin) {

    public Vec3i add(Vec3i force) {
        return new Vec3i(force.getX() + this.direction.getStepX() * this.magnitude, force.getY() + this.direction.getStepY() * this.magnitude, force.getZ() + this.direction.getStepZ() * this.magnitude);
    }
}