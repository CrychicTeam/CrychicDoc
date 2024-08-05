package dev.ftb.mods.ftblibrary.ui.input;

public class KeyModifiers {

    public static final KeyModifiers NONE = new KeyModifiers(0);

    public final int modifiers;

    public KeyModifiers(int m) {
        this.modifiers = m;
    }

    public boolean shift() {
        return (this.modifiers & 1) != 0;
    }

    public boolean control() {
        return (this.modifiers & 2) != 0;
    }

    public boolean alt() {
        return (this.modifiers & 4) != 0;
    }

    public boolean start() {
        return (this.modifiers & 8) != 0;
    }

    public boolean numLock() {
        return (this.modifiers & 32) != 0;
    }

    public boolean capsLock() {
        return (this.modifiers & 16) != 0;
    }

    public boolean onlyControl() {
        return this.control() && !this.shift() && !this.alt();
    }
}