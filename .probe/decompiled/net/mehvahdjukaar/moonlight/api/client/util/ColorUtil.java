package net.mehvahdjukaar.moonlight.api.client.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.minecraft.util.FastColor;
import org.joml.Vector3f;

@Deprecated(forRemoval = true)
public class ColorUtil {

    public static final Codec<Integer> CODEC = ColorUtils.CODEC;

    public static final float MINECRAFT_LIGHT_POWER = 0.6F;

    public static final float MINECRAFT_AMBIENT_LIGHT = 0.4F;

    public static DataResult<String> isValidStringOrError(String s) {
        return ColorUtils.isValidStringOrError(s);
    }

    public static boolean isValidString(String s) {
        return isValidStringOrError(s).result().isPresent();
    }

    public static int shadeColor(Vector3f normal, int color) {
        return multiply(color, getShading(normal));
    }

    public static float getShading(Vector3f normal) {
        return ColorUtils.getShading(normal);
    }

    public static int multiply(int color, float amount) {
        return ColorUtils.multiply(color, amount);
    }

    public static int swapFormat(int argb) {
        return argb & -16711936 | argb >> 16 & 0xFF | argb << 16 & 0xFF0000;
    }

    public static int pack(float[] rgb) {
        return FastColor.ARGB32.color(255, (int) (rgb[0] * 255.0F), (int) (rgb[1] * 255.0F), (int) (rgb[2] * 255.0F));
    }
}