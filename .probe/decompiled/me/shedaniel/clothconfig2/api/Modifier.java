package me.shedaniel.clothconfig2.api;

import java.util.Objects;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Modifier {

    private final short value;

    private Modifier(short value) {
        this.value = value;
    }

    public static Modifier none() {
        return of((short) 0);
    }

    public static Modifier of(boolean alt, boolean control, boolean shift) {
        short value = setFlag((short) 0, (short) 1, alt);
        value = setFlag(value, (short) 2, control);
        value = setFlag(value, (short) 4, shift);
        return of(value);
    }

    public static Modifier of(short value) {
        return new Modifier(value);
    }

    public static Modifier current() {
        return of(Screen.hasAltDown(), Screen.hasControlDown(), Screen.hasShiftDown());
    }

    private static short setFlag(short base, short flag, boolean val) {
        return val ? setFlag(base, flag) : removeFlag(base, flag);
    }

    private static short setFlag(short base, short flag) {
        return (short) (base | flag);
    }

    private static short removeFlag(short base, short flag) {
        return (short) (base & ~flag);
    }

    private static boolean getFlag(short base, short flag) {
        return (base & flag) != 0;
    }

    public boolean matchesCurrent() {
        return this.equals(current());
    }

    public short getValue() {
        return this.value;
    }

    public boolean hasAlt() {
        return getFlag(this.value, (short) 1);
    }

    public boolean hasControl() {
        return getFlag(this.value, (short) 2);
    }

    public boolean hasShift() {
        return getFlag(this.value, (short) 4);
    }

    public boolean isEmpty() {
        return this.value == 0;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else {
            return !(other instanceof Modifier) ? false : this.value == ((Modifier) other).value;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.value });
    }
}