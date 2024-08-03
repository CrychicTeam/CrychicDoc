package icyllis.modernui.mc.text;

@Deprecated
public class Color3i {

    public static final Color3i BLACK = new Color3i(0, 0, 0);

    public static final Color3i DARK_BLUE = new Color3i(0, 0, 170);

    public static final Color3i DARK_GREEN = new Color3i(0, 170, 0);

    public static final Color3i DARK_AQUA = new Color3i(0, 170, 170);

    public static final Color3i DARK_RED = new Color3i(170, 0, 0);

    public static final Color3i DARK_PURPLE = new Color3i(170, 0, 170);

    public static final Color3i GOLD = new Color3i(255, 170, 0);

    public static final Color3i GRAY = new Color3i(170, 170, 170);

    public static final Color3i DARK_GRAY = new Color3i(85, 85, 85);

    public static final Color3i BLUE = new Color3i(85, 85, 255);

    public static final Color3i GREEN = new Color3i(85, 255, 85);

    public static final Color3i AQUA = new Color3i(85, 255, 255);

    public static final Color3i RED = new Color3i(255, 85, 85);

    public static final Color3i LIGHT_PURPLE = new Color3i(255, 85, 255);

    public static final Color3i YELLOW = new Color3i(255, 255, 85);

    public static final Color3i WHITE = new Color3i(255, 255, 255);

    public static final Color3i BLUE_C = new Color3i(170, 220, 240);

    public static final Color3i GRAY_224 = new Color3i(224, 224, 224);

    public static final Color3i[] FORMATTING_COLORS = new Color3i[] { BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE };

    private final int red;

    private final int green;

    private final int blue;

    private final int color;

    Color3i(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.color = red << 16 | green << 8 | blue;
    }

    Color3i(int color) {
        this.red = color >> 16 & 0xFF;
        this.green = color >> 8 & 0xFF;
        this.blue = color & 0xFF;
        this.color = color;
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }

    public int getColor() {
        return this.color;
    }

    public String toString() {
        return "Color3i{color=" + this.color + "}";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Color3i color3i = (Color3i) o;
            return this.color == color3i.color;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.color;
    }

    public static Color3i fromFormattingCode(int code) {
        return code >= 0 && code <= 15 ? FORMATTING_COLORS[code] : WHITE;
    }

    public static float getRedFrom(int color) {
        return (float) (color >> 16 & 0xFF) / 255.0F;
    }

    public static float getGreenFrom(int color) {
        return (float) (color >> 8 & 0xFF) / 255.0F;
    }

    public static float getBlueFrom(int color) {
        return (float) (color & 0xFF) / 255.0F;
    }
}