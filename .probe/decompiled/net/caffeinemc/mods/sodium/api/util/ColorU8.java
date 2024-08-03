package net.caffeinemc.mods.sodium.api.util;

public interface ColorU8 {

    int COMPONENT_BITS = 8;

    int COMPONENT_MASK = 255;

    float COMPONENT_RANGE = 255.0F;

    float COMPONENT_RANGE_INVERSE = 0.003921569F;

    static int normalizedFloatToByte(float value) {
        return (int) (value * 255.0F) & 0xFF;
    }

    static float byteToNormalizedFloat(int value) {
        return (float) value * 0.003921569F;
    }
}