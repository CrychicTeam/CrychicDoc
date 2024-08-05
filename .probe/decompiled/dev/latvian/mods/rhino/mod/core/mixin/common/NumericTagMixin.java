package dev.latvian.mods.rhino.mod.core.mixin.common;

import dev.latvian.mods.rhino.util.SpecialEquality;
import net.minecraft.nbt.NumericTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ NumericTag.class })
public abstract class NumericTagMixin implements SpecialEquality {

    @Shadow
    public abstract byte getAsByte();

    @Shadow
    public abstract double getAsDouble();

    @Override
    public boolean specialEquals(Object o, boolean shallow) {
        if (o instanceof Boolean b) {
            return b == (this.getAsByte() != 0);
        } else if (o instanceof Number n1) {
            return this.getAsDouble() == n1.doubleValue();
        } else {
            return !shallow && o instanceof NumericTag n1 ? this.getAsDouble() == n1.getAsDouble() : this.equals(o);
        }
    }
}