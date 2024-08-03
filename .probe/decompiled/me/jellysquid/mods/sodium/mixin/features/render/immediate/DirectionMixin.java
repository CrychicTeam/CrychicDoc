package me.jellysquid.mods.sodium.mixin.features.render.immediate;

import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ Direction.class })
public class DirectionMixin {

    @Overwrite
    public static Direction getNearest(float x, float y, float z) {
        if (x == 0.0F && y == 0.0F && z == 0.0F) {
            return Direction.NORTH;
        } else {
            float yM = Math.abs(y);
            float zM = Math.abs(z);
            float xM = Math.abs(x);
            if (yM >= zM) {
                if (yM >= xM) {
                    if (y <= 0.0F) {
                        return Direction.DOWN;
                    }
                    return Direction.UP;
                }
            } else if (zM >= xM) {
                if (z <= 0.0F) {
                    return Direction.NORTH;
                }
                return Direction.SOUTH;
            }
            return x <= 0.0F ? Direction.WEST : Direction.EAST;
        }
    }
}