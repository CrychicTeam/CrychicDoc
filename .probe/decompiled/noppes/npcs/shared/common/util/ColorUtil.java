package noppes.npcs.shared.common.util;

public class ColorUtil {

    public static NopVector3f colorToRgb(int color) {
        return new NopVector3f(new float[] { (float) (color >> 16 & 0xFF) / 255.0F, (float) (color >> 8 & 0xFF) / 255.0F, (float) (color & 0xFF) / 255.0F });
    }

    public static int rgbToColor(NopVector3f color) {
        int r = (int) (color.x * 255.0F) << 16;
        int g = (int) (color.y * 255.0F) << 8;
        int b = (int) (color.z * 255.0F);
        return r + g + b;
    }

    public static String colorToHex(int color) {
        String str = Integer.toHexString(color);
        while (str.length() < 6) {
            str = "0" + str;
        }
        return str;
    }

    public static int hexToColor(String hex) {
        try {
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException var2) {
            return 16777215;
        }
    }
}