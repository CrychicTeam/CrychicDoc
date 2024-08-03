package net.minecraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.function.BooleanSupplier;

public class ToggleKeyMapping extends KeyMapping {

    private final BooleanSupplier needsToggle;

    public ToggleKeyMapping(String string0, int int1, String string2, BooleanSupplier booleanSupplier3) {
        super(string0, InputConstants.Type.KEYSYM, int1, string2);
        this.needsToggle = booleanSupplier3;
    }

    @Override
    public void setDown(boolean boolean0) {
        if (this.needsToggle.getAsBoolean()) {
            if (boolean0) {
                super.setDown(!this.m_90857_());
            }
        } else {
            super.setDown(boolean0);
        }
    }

    protected void reset() {
        super.setDown(false);
    }
}