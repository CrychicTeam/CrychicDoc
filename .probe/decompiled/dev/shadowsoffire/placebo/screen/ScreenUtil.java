package dev.shadowsoffire.placebo.screen;

import net.minecraft.util.Mth;

public class ScreenUtil {

    public static int getHeight(float height, int current, int max) {
        return Mth.ceil(height * (float) current / (float) max);
    }
}