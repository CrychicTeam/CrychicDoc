package me.jellysquid.mods.lithium.mixin.shapes.specialized_shapes;

import me.jellysquid.mods.lithium.common.shapes.VoxelShapeAlignedCuboid;
import me.jellysquid.mods.lithium.common.shapes.VoxelShapeEmpty;
import me.jellysquid.mods.lithium.common.shapes.VoxelShapeSimpleCube;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Shapes.class })
public abstract class VoxelShapesMixin {

    @Mutable
    @Shadow
    @Final
    public static final VoxelShape INFINITY = new VoxelShapeSimpleCube(VoxelShapesMixin.FULL_CUBE_VOXELS, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    @Mutable
    @Shadow
    @Final
    private static final VoxelShape BLOCK = new VoxelShapeSimpleCube(VoxelShapesMixin.FULL_CUBE_VOXELS, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

    @Mutable
    @Shadow
    @Final
    private static final VoxelShape EMPTY = new VoxelShapeEmpty(new BitSetDiscreteVoxelShape(0, 0, 0));

    private static final DiscreteVoxelShape FULL_CUBE_VOXELS = new BitSetDiscreteVoxelShape(1, 1, 1);

    @Overwrite
    public static VoxelShape create(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        if (maxX - minX < 1.0E-7 || maxY - minY < 1.0E-7 || maxZ - minZ < 1.0E-7) {
            return EMPTY;
        } else {
            int xRes;
            int yRes;
            int zRes;
            if ((xRes = Shapes.findBits(minX, maxX)) < 0 || (yRes = Shapes.findBits(minY, maxY)) < 0 || (zRes = Shapes.findBits(minZ, maxZ)) < 0) {
                return new VoxelShapeSimpleCube(FULL_CUBE_VOXELS, minX, minY, minZ, maxX, maxY, maxZ);
            } else {
                return (VoxelShape) (xRes == 0 && yRes == 0 && zRes == 0 ? BLOCK : new VoxelShapeAlignedCuboid((double) Math.round(minX * 8.0) / 8.0, (double) Math.round(minY * 8.0) / 8.0, (double) Math.round(minZ * 8.0) / 8.0, (double) Math.round(maxX * 8.0) / 8.0, (double) Math.round(maxY * 8.0) / 8.0, (double) Math.round(maxZ * 8.0) / 8.0, xRes, yRes, zRes));
            }
        }
    }

    static {
        FULL_CUBE_VOXELS.fill(0, 0, 0);
    }
}