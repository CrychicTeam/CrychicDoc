package me.jellysquid.mods.lithium.mixin.math.fast_util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ AABB.class })
public class BoxMixin {

    @Shadow
    @Final
    public double minX;

    @Shadow
    @Final
    public double minY;

    @Shadow
    @Final
    public double minZ;

    @Shadow
    @Final
    public double maxX;

    @Shadow
    @Final
    public double maxY;

    @Shadow
    @Final
    public double maxZ;

    @Overwrite
    public double min(Direction.Axis axis) {
        switch(axis) {
            case X:
                return this.minX;
            case Y:
                return this.minY;
            case Z:
                return this.minZ;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Overwrite
    public double max(Direction.Axis axis) {
        switch(axis) {
            case X:
                return this.maxX;
            case Y:
                return this.maxY;
            case Z:
                return this.maxZ;
            default:
                throw new IllegalArgumentException();
        }
    }

    static {
        assert Direction.Axis.X.ordinal() == 0;
        assert Direction.Axis.Y.ordinal() == 1;
        assert Direction.Axis.Z.ordinal() == 2;
        assert Direction.Axis.values().length == 3;
    }
}