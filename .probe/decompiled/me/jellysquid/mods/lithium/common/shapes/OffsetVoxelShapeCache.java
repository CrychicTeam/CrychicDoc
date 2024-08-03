package me.jellysquid.mods.lithium.common.shapes;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface OffsetVoxelShapeCache {

    VoxelShape getOffsetSimplifiedShape(float var1, Direction var2);

    void setShape(float var1, Direction var2, VoxelShape var3);
}