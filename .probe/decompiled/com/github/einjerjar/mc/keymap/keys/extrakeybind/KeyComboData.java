package com.github.einjerjar.mc.keymap.keys.extrakeybind;

import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KeyComboData {

    public static final KeyComboData UNBOUND = new KeyComboData(-1);

    protected int keyCode;

    protected KeyType keyType;

    protected boolean alt;

    protected boolean shift;

    protected boolean ctrl;

    public KeyComboData(int keyCode) {
        this(keyCode, KeyType.KEYBOARD);
    }

    public KeyComboData(int keyCode, KeyType keyType) {
        this(keyCode, keyType, false, false, false);
    }

    public KeyComboData(int keyCode, boolean alt, boolean shift, boolean ctrl) {
        this(keyCode, KeyType.KEYBOARD, alt, shift, ctrl);
    }

    public KeyComboData(InputConstants.Key k) {
        this(k.getValue(), k.getType() == InputConstants.Type.MOUSE ? KeyType.MOUSE : KeyType.KEYBOARD);
    }

    public KeyComboData(int keyCode, KeyType keyType, boolean alt, boolean shift, boolean ctrl) {
        this.keyCode = keyCode;
        this.keyType = keyType;
        this.alt = alt;
        this.shift = shift;
        this.ctrl = ctrl;
        if (this.isModifierOnly()) {
            this.alt = this.shift = this.ctrl = false;
        }
    }

    public InputConstants.Key toKey() {
        InputConstants.Type t = this.keyType == KeyType.KEYBOARD ? InputConstants.Type.KEYSYM : InputConstants.Type.MOUSE;
        return t.getOrCreate(this.keyCode);
    }

    public String searchString() {
        return this.toKey().getDisplayName().getString();
    }

    public String toKeyString() {
        List<String> x = new ArrayList();
        if (this.ctrl) {
            x.add("ctrl");
        }
        if (this.alt) {
            x.add("alt");
        }
        if (this.shift) {
            x.add("shift");
        }
        x.add(this.toKey().getDisplayName().getString());
        return String.join(" + ", x);
    }

    public boolean onlyKey() {
        return !this.alt && !this.shift && !this.ctrl;
    }

    public int modifierCount() {
        int x = this.alt ? 1 : 0;
        x += this.shift ? 1 : 0;
        return x + (this.ctrl ? 1 : 0);
    }

    public boolean isModifier() {
        return KeymapRegistry.MODIFIER_KEYS.contains(this.keyCode);
    }

    public boolean isModifierOnly() {
        return this.isModifier() && this.modifierCount() == 1;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            KeyComboData that = (KeyComboData) o;
            return this.keyCode == that.keyCode && this.alt == that.alt && this.shift == that.shift && this.ctrl == that.ctrl && this.keyType == that.keyType;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.keyCode, this.keyType, this.alt, this.shift, this.ctrl });
    }

    public int keyCode() {
        return this.keyCode;
    }

    public KeyType keyType() {
        return this.keyType;
    }

    public boolean alt() {
        return this.alt;
    }

    public boolean shift() {
        return this.shift;
    }

    public boolean ctrl() {
        return this.ctrl;
    }

    public String toString() {
        return "KeyComboData(keyCode=" + this.keyCode() + ", keyType=" + this.keyType() + ", alt=" + this.alt() + ", shift=" + this.shift() + ", ctrl=" + this.ctrl() + ")";
    }
}