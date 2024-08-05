package dev.latvian.mods.rhino.mod.core.mixin.common;

import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { Direction.class }, priority = 1001)
public abstract class DirectionMixin {

    @Shadow
    @RemapForJS("getX")
    public abstract int getStepX();

    @Shadow
    @RemapForJS("getY")
    public abstract int getStepY();

    @Shadow
    @RemapForJS("getZ")
    public abstract int getStepZ();

    @Shadow
    @RemapForJS("getIndex")
    public abstract int get3DDataValue();

    @Shadow
    @RemapForJS("getHorizontalIndex")
    public abstract int get2DDataValue();

    @Shadow
    @RemapForJS("getYaw")
    public abstract float toYRot();

    @RemapForJS("getPitch")
    public float rhino$getPitch() {
        return this == Direction.UP ? 180.0F : (this == Direction.DOWN ? 0.0F : 90.0F);
    }
}