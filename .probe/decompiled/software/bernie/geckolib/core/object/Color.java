package software.bernie.geckolib.core.object;

public record Color(int argbInt) {

    public static final Color WHITE = new Color(-1);

    public static final Color LIGHT_GRAY = new Color(-4144960);

    public static final Color GRAY = new Color(-8355712);

    public static final Color DARK_GRAY = new Color(-12566464);

    public static final Color BLACK = new Color(-16777216);

    public static final Color RED = new Color(-65536);

    public static final Color PINK = new Color(-20561);

    public static final Color ORANGE = new Color(-14336);

    public static final Color YELLOW = new Color(-256);

    public static final Color GREEN = new Color(-16711936);

    public static final Color MAGENTA = new Color(-65281);

    public static final Color CYAN = new Color(-16711681);

    public static final Color BLUE = new Color(-16776961);

    public static Color ofOpaque(int color) {
        return new Color(0xFF000000 | color);
    }

    public static Color ofRGB(float red, float green, float blue) {
        return ofRGBA(red, green, blue, 1.0F);
    }

    public static Color ofRGB(int r, int g, int b) {
        return ofRGBA(r, g, b, 255);
    }

    public static Color ofRGBA(float r, float g, float b, float a) {
        return ofRGBA((int) ((double) (r * 255.0F) + 0.5), (int) (g * 255.0F + 0.5F), (int) (b * 255.0F + 0.5F), (int) (a * 255.0F + 0.5F));
    }

    public static Color ofRGBA(int r, int g, int b, int a) {
        return new Color((a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF);
    }

    public static Color ofHSB(float hue, float saturation, float brightness) {
        return ofOpaque(HSBtoARGB(hue, saturation, brightness));
    }

    public static int HSBtoARGB(float hue, float saturation, float brightness) {
        int r = 0;
        int g = 0;
        int b = 0;
        if (saturation == 0.0F) {
            r = g = b = (int) (brightness * 255.0F + 0.5F);
        } else {
            float h = (hue - (float) Math.floor((double) hue)) * 6.0F;
            float f = h - (float) Math.floor((double) h);
            float p = brightness * (1.0F - saturation);
            float q = brightness * (1.0F - saturation * f);
            float t = brightness * (1.0F - saturation * (1.0F - f));
            switch((int) h) {
                case 0:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (t * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 1:
                    r = (int) (q * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (p * 255.0F + 0.5F);
                    break;
                case 2:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (brightness * 255.0F + 0.5F);
                    b = (int) (t * 255.0F + 0.5F);
                    break;
                case 3:
                    r = (int) (p * 255.0F + 0.5F);
                    g = (int) (q * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 4:
                    r = (int) (t * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (brightness * 255.0F + 0.5F);
                    break;
                case 5:
                    r = (int) (brightness * 255.0F + 0.5F);
                    g = (int) (p * 255.0F + 0.5F);
                    b = (int) (q * 255.0F + 0.5F);
            }
        }
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    public int getColor() {
        return this.argbInt;
    }

    public int getAlpha() {
        return this.argbInt >> 24 & 0xFF;
    }

    public float getAlphaFloat() {
        return (float) this.getAlpha() / 255.0F;
    }

    public int getRed() {
        return this.argbInt >> 16 & 0xFF;
    }

    public float getRedFloat() {
        return (float) this.getRed() / 255.0F;
    }

    public int getGreen() {
        return this.argbInt >> 8 & 0xFF;
    }

    public float getGreenFloat() {
        return (float) this.getGreen() / 255.0F;
    }

    public int getBlue() {
        return this.argbInt & 0xFF;
    }

    public float getBlueFloat() {
        return (float) this.getBlue() / 255.0F;
    }

    public Color brighter(double factor) {
        int r = this.getRed();
        int g = this.getGreen();
        int b = this.getBlue();
        int i = (int) (1.0 / (1.0 - 1.0 / factor));
        if (r == 0 && g == 0 && b == 0) {
            return ofRGBA(i, i, i, this.getAlpha());
        } else {
            if (r > 0 && r < i) {
                r = i;
            }
            if (g > 0 && g < i) {
                g = i;
            }
            if (b > 0 && b < i) {
                b = i;
            }
            return ofRGBA(Math.min((int) ((double) r / (1.0 / factor)), 255), Math.min((int) ((double) g / (1.0 / factor)), 255), Math.min((int) ((double) b / (1.0 / factor)), 255), this.getAlpha());
        }
    }

    public Color darker(float factor) {
        return ofRGBA(Math.max((int) ((float) this.getRed() * (1.0F / factor)), 0), Math.max((int) ((float) this.getGreen() * (1.0F / factor)), 0), Math.max((int) ((float) this.getBlue() * (1.0F / factor)), 0), this.getAlpha());
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return this.getClass() != other.getClass() ? false : this.hashCode() == other.hashCode();
        }
    }

    public int hashCode() {
        return this.argbInt;
    }

    public String toString() {
        return String.valueOf(this.argbInt);
    }
}