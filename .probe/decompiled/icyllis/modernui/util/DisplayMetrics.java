package icyllis.modernui.util;

public class DisplayMetrics {

    public static final int DENSITY_LOW = 72;

    public static final int DENSITY_84 = 84;

    public static final int DENSITY_MEDIUM = 96;

    public static final int DENSITY_108 = 108;

    public static final int DENSITY_120 = 120;

    public static final int DENSITY_132 = 132;

    public static final int DENSITY_HIGH = 144;

    public static final int DENSITY_156 = 156;

    public static final int DENSITY_168 = 168;

    public static final int DENSITY_180 = 180;

    public static final int DENSITY_XHIGH = 192;

    public static final int DENSITY_204 = 204;

    public static final int DENSITY_216 = 216;

    public static final int DENSITY_228 = 228;

    public static final int DENSITY_240 = 240;

    public static final int DENSITY_252 = 252;

    public static final int DENSITY_264 = 264;

    public static final int DENSITY_276 = 276;

    public static final int DENSITY_XXHIGH = 288;

    public static final int DENSITY_DEFAULT = 72;

    public static final float DENSITY_DEFAULT_SCALE = 0.013888889F;

    public int widthPixels;

    public int heightPixels;

    public float density;

    public int densityDpi;

    public float scaledDensity;

    public float xdpi;

    public float ydpi;

    public void setTo(DisplayMetrics o) {
        if (this != o) {
            this.widthPixels = o.widthPixels;
            this.heightPixels = o.heightPixels;
            this.density = o.density;
            this.densityDpi = o.densityDpi;
            this.scaledDensity = o.scaledDensity;
            this.xdpi = o.xdpi;
            this.ydpi = o.ydpi;
        }
    }

    public void setToDefaults() {
        this.widthPixels = 0;
        this.heightPixels = 0;
        this.density = 1.0F;
        this.densityDpi = 72;
        this.scaledDensity = this.density;
        this.xdpi = 72.0F;
        this.ydpi = 72.0F;
    }

    public boolean equals(Object o) {
        return o instanceof DisplayMetrics && this.equals((DisplayMetrics) o);
    }

    public boolean equals(DisplayMetrics other) {
        return this.equalsPhysical(other) && this.scaledDensity == other.scaledDensity;
    }

    public boolean equalsPhysical(DisplayMetrics other) {
        return other != null && this.widthPixels == other.widthPixels && this.heightPixels == other.heightPixels && this.density == other.density && this.densityDpi == other.densityDpi && this.xdpi == other.xdpi && this.ydpi == other.ydpi;
    }

    public int hashCode() {
        return this.widthPixels * this.heightPixels * this.densityDpi;
    }

    public String toString() {
        return "DisplayMetrics{density=" + this.density + ", width=" + this.widthPixels + ", height=" + this.heightPixels + ", scaledDensity=" + this.scaledDensity + ", xdpi=" + this.xdpi + ", ydpi=" + this.ydpi + "}";
    }
}