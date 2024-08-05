package net.mehvahdjukaar.moonlight.api.util.math;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Locale;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import org.joml.Vector3f;

public class ColorUtils {

    public static final Codec<Integer> CODEC = Codec.either(Codec.intRange(0, -1), Codec.STRING.flatXmap(ColorUtils::isValidStringOrError, s -> isValidStringOrError(s).map(ColorUtils::formatString))).xmap(either -> (Integer) either.map(integer -> integer, s -> Integer.parseUnsignedInt(s, 16)), integer -> Either.right("#" + String.format("%08X", integer)));

    private static final Vector3f DIFFUSE_LIGHT_0 = new Vector3f(0.2F, 1.0F, -0.7F).normalize();

    private static final Vector3f DIFFUSE_LIGHT_1 = new Vector3f(-0.2F, 1.0F, 0.7F).normalize();

    public static final float MINECRAFT_LIGHT_POWER = 0.6F;

    public static final float MINECRAFT_AMBIENT_LIGHT = 0.4F;

    private static String formatString(String s) {
        return "#" + s.toUpperCase(Locale.ROOT);
    }

    public static DataResult<String> isValidStringOrError(String s) {
        String st = s;
        if (s.startsWith("0x")) {
            st = s.substring(2);
        } else if (s.startsWith("#")) {
            st = s.substring(1);
        }
        if (st.length() > 8) {
            return DataResult.error(() -> "Invalid color format. Hex value must have up to 8 characters.");
        } else {
            try {
                int parsedValue = Integer.parseUnsignedInt(st, 16);
                return DataResult.success(st);
            } catch (NumberFormatException var3) {
                return DataResult.error(() -> "Invalid color format. Must be in hex format (0xff00ff00, #ff00ff00, ff00ff00) or integer value");
            }
        }
    }

    public static boolean isValidString(String s) {
        return isValidStringOrError(s).result().isPresent();
    }

    public static int shadeColor(Vector3f normal, int color) {
        return multiply(color, getShading(normal));
    }

    public static float getShading(Vector3f normal) {
        if (normal.equals(Direction.UP.step())) {
            return 1.0F;
        } else {
            Vector3f lightDir0 = DIFFUSE_LIGHT_0;
            Vector3f lightDir1 = DIFFUSE_LIGHT_1;
            lightDir0.normalize();
            lightDir1.normalize();
            float light0 = Math.max(0.0F, lightDir0.dot(normal));
            float light1 = Math.max(0.0F, lightDir1.dot(normal));
            return Math.min(1.0F, (light0 + light1) * 0.6F + 0.4F);
        }
    }

    public static int multiply(int color, float amount) {
        if (amount == 1.0F) {
            return color;
        } else {
            int j = Math.min(255, (int) ((float) FastColor.ABGR32.red(color) * amount));
            int k = Math.min(255, (int) ((float) FastColor.ABGR32.green(color) * amount));
            int l = Math.min(255, (int) ((float) FastColor.ABGR32.blue(color) * amount));
            return FastColor.ABGR32.color(0, l, k, j);
        }
    }

    public static int swapFormat(int argb) {
        return argb & -16711936 | argb >> 16 & 0xFF | argb << 16 & 0xFF0000;
    }

    public static int pack(float[] rgb) {
        return FastColor.ARGB32.color(255, (int) (rgb[0] * 255.0F), (int) (rgb[1] * 255.0F), (int) (rgb[2] * 255.0F));
    }
}