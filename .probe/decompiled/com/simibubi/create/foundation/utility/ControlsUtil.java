package com.simibubi.create.foundation.utility;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.AllKeys;
import java.util.Vector;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class ControlsUtil {

    private static Vector<KeyMapping> standardControls;

    public static Vector<KeyMapping> getControls() {
        if (standardControls == null) {
            Options gameSettings = Minecraft.getInstance().options;
            standardControls = new Vector(6);
            standardControls.add(gameSettings.keyUp);
            standardControls.add(gameSettings.keyDown);
            standardControls.add(gameSettings.keyLeft);
            standardControls.add(gameSettings.keyRight);
            standardControls.add(gameSettings.keyJump);
            standardControls.add(gameSettings.keyShift);
        }
        return standardControls;
    }

    public static boolean isActuallyPressed(KeyMapping kb) {
        InputConstants.Key key = kb.getKey();
        return key.getType() == InputConstants.Type.MOUSE ? AllKeys.isMouseButtonDown(key.getValue()) : AllKeys.isKeyDown(key.getValue());
    }
}