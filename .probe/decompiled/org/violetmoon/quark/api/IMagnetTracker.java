package org.violetmoon.quark.api;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

public interface IMagnetTracker {

    Vec3i getNetForce(BlockPos var1);

    void applyForce(BlockPos var1, int var2, boolean var3, Direction var4, int var5, BlockPos var6);

    void actOnForces(BlockPos var1);

    Collection<BlockPos> getTrackedPositions();

    void clear();
}