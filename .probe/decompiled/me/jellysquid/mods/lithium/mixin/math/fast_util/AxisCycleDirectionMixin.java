package me.jellysquid.mods.lithium.mixin.math.fast_util;

import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

public class AxisCycleDirectionMixin {

    static {
        assert Direction.Axis.X.ordinal() == 0;
        assert Direction.Axis.Y.ordinal() == 1;
        assert Direction.Axis.Z.ordinal() == 2;
        assert Direction.Axis.values().length == 3;
    }

    @Mixin(targets = { "net/minecraft/util/math/AxisCycleDirection$3" })
    public static class BackwardMixin {

        @Overwrite
        public Direction.Axis cycle(Direction.Axis axis) {
            switch(axis) {
                case X:
                    return Direction.Axis.Z;
                case Y:
                    return Direction.Axis.X;
                case Z:
                    return Direction.Axis.Y;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    @Mixin(targets = { "net/minecraft/util/math/AxisCycleDirection$2" })
    public static class ForwardMixin {

        @Overwrite
        public Direction.Axis cycle(Direction.Axis axis) {
            switch(axis) {
                case X:
                    return Direction.Axis.Y;
                case Y:
                    return Direction.Axis.Z;
                case Z:
                    return Direction.Axis.X;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}