package me.jellysquid.mods.sodium.client.world.biome;

import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;

public class BiomeColorMaps {

    private static final int WIDTH = 256;

    private static final int HEIGHT = 256;

    private static final int INVALID_INDEX = -1;

    private static final int DEFAULT_COLOR = -65281;

    public static int getGrassColor(int index) {
        return index != -1 && index < GrassColor.pixels.length ? GrassColor.pixels[index] : -65281;
    }

    public static int getFoliageColor(int index) {
        return index != -1 && index < FoliageColor.pixels.length ? FoliageColor.pixels[index] : -65281;
    }

    public static int getIndex(double temperature, double humidity) {
        humidity *= temperature;
        int x = (int) ((1.0 - temperature) * 255.0);
        int y = (int) ((1.0 - humidity) * 255.0);
        if (x < 0 || x >= 256) {
            return -1;
        } else {
            return y >= 0 && y < 256 ? y << 8 | x : -1;
        }
    }
}