package dev.architectury.hooks.forge;

import net.minecraft.world.item.DyeColor;

public class DyeColorHooksImpl {

    public static int getColorValue(DyeColor dyeColor) {
        float[] colors = dyeColor.getTextureDiffuseColors();
        return ((int) ((double) (colors[0] * 255.0F) + 0.5) & 0xFF) << 16 | ((int) ((double) (colors[1] * 255.0F) + 0.5) & 0xFF) << 8 | (int) ((double) (colors[2] * 255.0F) + 0.5);
    }
}