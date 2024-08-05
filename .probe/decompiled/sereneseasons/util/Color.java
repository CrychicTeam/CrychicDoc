package sereneseasons.util;

public class Color {

    int r;

    int g;

    int b;

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color(int color) {
        this(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF);
    }

    public Color(double r, double g, double b) {
        this((int) (r * 255.0), (int) (g * 255.0), (int) (b * 255.0));
    }

    public int getRed() {
        return this.r;
    }

    public int getGreen() {
        return this.g;
    }

    public int getBlue() {
        return this.b;
    }

    public int toInt() {
        return (this.getRed() & 0xFF) << 16 | (this.getGreen() & 0xFF) << 8 | this.getBlue() & 0xFF;
    }

    public double[] toHSV() {
        return convertRGBtoHSV((double) this.getRed() / 255.0, (double) this.getGreen() / 255.0, (double) this.getBlue() / 255.0);
    }

    public static double[] convertRGBtoHSV(double r, double g, double b) {
        double min = r < g ? r : g;
        min = min < b ? min : b;
        double max = r > g ? r : g;
        max = max > b ? max : b;
        double delta = max - min;
        if (delta < 1.0E-5) {
            double s = 0.0;
            double h = 0.0;
            return new double[] { h, s, max };
        } else if (max > 0.0) {
            double s = delta / max;
            double h;
            if (r >= max) {
                h = (g - b) / delta;
            } else if (g >= max) {
                h = 2.0 + (b - r) / delta;
            } else {
                h = 4.0 + (r - g) / delta;
            }
            h *= 60.0;
            if (h < 0.0) {
                h += 360.0;
            }
            return new double[] { h, s, max };
        } else {
            double sx = 0.0;
            double hx = -1.0;
            return new double[] { hx, sx, max };
        }
    }

    public static Color convertHSVtoRGB(double h, double s, double v) {
        if (s <= 0.0) {
            return new Color(v, v, v);
        } else {
            double hh = h;
            if (h >= 360.0) {
                hh = 0.0;
            }
            hh /= 60.0;
            int i = (int) hh;
            double ff = hh - (double) i;
            double p = v * (1.0 - s);
            double q = v * (1.0 - s * ff);
            double t = v * (1.0 - s * (1.0 - ff));
            double r;
            double g;
            double b;
            switch(i) {
                case 0:
                    r = v;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = v;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = v;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = v;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = v;
                    break;
                case 5:
                default:
                    r = v;
                    g = p;
                    b = q;
            }
            return new Color(r, g, b);
        }
    }
}