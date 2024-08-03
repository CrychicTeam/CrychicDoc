package me.jellysquid.mods.lithium.mixin.math.fast_util;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ Direction.class })
public class DirectionMixin {

    @Shadow
    @Final
    private static Direction[] VALUES;

    @Shadow
    @Final
    private int oppositeIndex;

    @Overwrite
    public Direction getOpposite() {
        return VALUES[this.oppositeIndex];
    }

    @Overwrite
    public static Direction getRandom(RandomSource rand) {
        return VALUES[rand.nextInt(VALUES.length)];
    }
}