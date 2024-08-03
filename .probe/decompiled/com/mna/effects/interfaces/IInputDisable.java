package com.mna.effects.interfaces;

import java.util.EnumSet;

public interface IInputDisable {

    EnumSet<IInputDisable.InputMask> getDisabledFlags();

    default int getDisableMask() {
        int bitMask = 0;
        for (IInputDisable.InputMask mask : this.getDisabledFlags()) {
            bitMask |= 1 << mask.ordinal();
        }
        return bitMask;
    }

    public static enum InputMask {

        MOVEMENT, LEFT_CLICK, RIGHT_CLICK, JUMP;

        public int mask() {
            return 1 << this.ordinal();
        }
    }
}