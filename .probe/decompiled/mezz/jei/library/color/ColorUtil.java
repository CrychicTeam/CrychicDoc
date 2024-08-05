package mezz.jei.library.color;

public final class ColorUtil {

    private ColorUtil() {
    }

    public static double fastPerceptualColorDistanceSquared(int[] color1, int[] color2) {
        int red1 = color1[0];
        int red2 = color2[0];
        int redMean = red1 + red2 >> 1;
        int r = red1 - red2;
        int g = color1[1] - color2[1];
        int b = color1[2] - color2[2];
        return (double) (((512 + redMean) * r * r >> 8) + 4 * g * g + ((767 - redMean) * b * b >> 8));
    }

    public static double slowPerceptualColorDistanceSquared(int color1, int color2) {
        int red1 = color1 >> 16 & 0xFF;
        int green1 = color1 >> 8 & 0xFF;
        int blue1 = color1 & 0xFF;
        int red2 = color2 >> 16 & 0xFF;
        int green2 = color2 >> 8 & 0xFF;
        int blue2 = color2 & 0xFF;
        int redMean = red1 + red2 >> 1;
        int r = red1 - red2;
        int g = green1 - green2;
        int b = blue1 - blue2;
        double colorDistanceSquared = (double) (((512 + redMean) * r * r >> 8) + 4 * g * g + ((767 - redMean) * b * b >> 8));
        double grey1 = (double) (red1 + green1 + blue1) / 3.0;
        double grey2 = (double) (red2 + green2 + blue2) / 3.0;
        double greyDistance1 = Math.abs(grey1 - (double) red1) + Math.abs(grey1 - (double) green1) + Math.abs(grey1 - (double) blue1);
        double greyDistance2 = Math.abs(grey2 - (double) red2) + Math.abs(grey2 - (double) green2) + Math.abs(grey2 - (double) blue2);
        double greyDistance = greyDistance1 - greyDistance2;
        return colorDistanceSquared + greyDistance * greyDistance / 10.0;
    }
}