package net.caffeinemc.mods.sodium.api.util;

import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class NormI8 {

    private static final int X_COMPONENT_OFFSET = 0;

    private static final int Y_COMPONENT_OFFSET = 8;

    private static final int Z_COMPONENT_OFFSET = 16;

    private static final float COMPONENT_RANGE = 127.0F;

    private static final float NORM = 0.007874016F;

    public static int pack(Vector3f normal) {
        return pack(normal.x(), normal.y(), normal.z());
    }

    public static int pack(float x, float y, float z) {
        int normX = encode(x);
        int normY = encode(y);
        int normZ = encode(z);
        return normZ << 16 | normY << 8 | normX << 0;
    }

    private static int encode(float comp) {
        return (int) (Mth.clamp(comp, -1.0F, 1.0F) * 127.0F) & 0xFF;
    }

    public static float unpackX(int norm) {
        return (float) ((byte) (norm >> 0 & 0xFF)) * 0.007874016F;
    }

    public static float unpackY(int norm) {
        return (float) ((byte) (norm >> 8 & 0xFF)) * 0.007874016F;
    }

    public static float unpackZ(int norm) {
        return (float) ((byte) (norm >> 16 & 0xFF)) * 0.007874016F;
    }
}