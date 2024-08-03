package net.mehvahdjukaar.moonlight.api.util.math.colors;

public final class ColorSpaces {

    private static final float SCALE_X = 0.95047F;

    private static final float SCALE_Y = 1.0F;

    private static final float SCALE_Z = 1.08883F;

    private static final float SCALE_L = 100.0F;

    private static final float SCALE_A = 255.0F;

    private static final float SCALE_B = 255.0F;

    private static final float UN = 0.2009F;

    private static final float VN = 0.461F;

    public static HSVColor RGBtoHSV(RGBColor color) {
        float r = color.red();
        float g = color.green();
        float b = color.blue();
        float cmax = Math.max(r, g);
        if (b > cmax) {
            cmax = b;
        }
        float cmin = Math.min(r, g);
        if (b < cmin) {
            cmin = b;
        }
        float saturation;
        if (cmax != 0.0F) {
            saturation = (cmax - cmin) / cmax;
        } else {
            saturation = 0.0F;
        }
        float hue;
        if (saturation == 0.0F) {
            hue = 0.0F;
        } else {
            float redc = (cmax - r) / (cmax - cmin);
            float greenc = (cmax - g) / (cmax - cmin);
            float bluec = (cmax - b) / (cmax - cmin);
            if (r == cmax) {
                hue = bluec - greenc;
            } else if (g == cmax) {
                hue = 2.0F + redc - bluec;
            } else {
                hue = 4.0F + greenc - redc;
            }
            hue /= 6.0F;
            if (hue < 0.0F) {
                hue++;
            }
        }
        return new HSVColor(hue, saturation, cmax, color.alpha());
    }

    public static RGBColor HSVtoRGB(HSVColor color) {
        float hue = color.hue();
        float saturation = color.saturation();
        float brightness = color.value();
        float r = 0.0F;
        float g = 0.0F;
        float b = 0.0F;
        if (saturation == 0.0F) {
            r = g = b = (float) ((int) (brightness * 255.0F + 0.5F));
        } else {
            float h = (hue - (float) Math.floor((double) hue)) * 6.0F;
            float f = h - (float) Math.floor((double) h);
            float p = brightness * (1.0F - saturation);
            float q = brightness * (1.0F - saturation * f);
            float t = brightness * (1.0F - saturation * (1.0F - f));
            switch((int) h) {
                case 0:
                    r = brightness;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = brightness;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = brightness;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = brightness;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = brightness;
                    break;
                case 5:
                    r = brightness;
                    g = p;
                    b = q;
            }
        }
        return new RGBColor(r, g, b, color.alpha());
    }

    public static HSLColor RGBtoHSL(RGBColor color) {
        float r = color.red();
        float g = color.green();
        float b = color.blue();
        float max = r > g && r > b ? r : Math.max(g, b);
        float min = r < g && r < b ? r : Math.min(g, b);
        float l = (max + min) / 2.0F;
        float h;
        float s;
        if (max == min) {
            s = 0.0F;
            h = 0.0F;
        } else {
            float d = max - min;
            s = l > 0.5F ? d / (2.0F - max - min) : d / (max + min);
            if (r > g && r > b) {
                h = (g - b) / d + (g < b ? 6.0F : 0.0F);
            } else if (g > b) {
                h = (b - r) / d + 2.0F;
            } else {
                h = (r - g) / d + 4.0F;
            }
            h /= 6.0F;
        }
        return new HSLColor(h, s, l, color.alpha());
    }

    public static RGBColor HSLtoRGB(HSLColor color) {
        float h = color.hue();
        float s = color.saturation();
        float l = color.lightness();
        float r;
        float g;
        float b;
        if (s == 0.0F) {
            b = l;
            g = l;
            r = l;
        } else {
            float q = l < 0.5F ? l * (1.0F + s) : l + s - l * s;
            float p = 2.0F * l - q;
            r = hueToRgb(p, q, h + 0.33333334F);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 0.33333334F);
        }
        return new RGBColor(r, g, b, color.alpha());
    }

    private static float hueToRgb(float p, float q, float t) {
        if (t < 0.0F) {
            t++;
        }
        if (t > 1.0F) {
            t--;
        }
        if (t < 0.16666667F) {
            return p + (q - p) * 6.0F * t;
        } else if (t < 0.5F) {
            return q;
        } else {
            return t < 0.6666667F ? p + (q - p) * (0.6666667F - t) * 6.0F : p;
        }
    }

    public static XYZColor RGBtoXYZ(RGBColor color) {
        float red = color.red();
        float green = color.green();
        float blue = color.blue();
        double r = red > 0.04045F ? Math.pow((double) ((red + 0.055F) / 1.055F), 2.4F) : (double) (red / 12.92F);
        double g = green > 0.04045F ? Math.pow((double) ((green + 0.055F) / 1.055F), 2.4F) : (double) (green / 12.92F);
        double b = blue > 0.04045F ? Math.pow((double) ((blue + 0.055F) / 1.055F), 2.4F) : (double) (blue / 12.92F);
        float x = (float) (0.4124F * r + 0.3576F * g + 0.1805F * b);
        float y = (float) (0.2126F * r + 0.7152F * g + 0.0722F * b);
        float z = (float) (0.0193F * r + 0.1192F * g + 0.9505F * b);
        return new XYZColor(x, y, z, color.alpha());
    }

    public static RGBColor XYZtoRGB(XYZColor color) {
        float x = color.x();
        float y = color.y();
        float z = color.z();
        float r = 3.2406F * x - 1.5372F * y - 0.4986F * z;
        float g = -0.9689F * x + 1.8758F * y + 0.0415F * z;
        float b = 0.0557F * x - 0.204F * y + 1.057F * z;
        r = r > 0.0031308F ? (float) (1.055F * Math.pow((double) r, 0.41666666F) - 0.055F) : 12.92F * r;
        g = g > 0.0031308F ? (float) (1.055F * Math.pow((double) g, 0.41666666F) - 0.055F) : 12.92F * g;
        b = b > 0.0031308F ? (float) (1.055F * Math.pow((double) b, 0.41666666F) - 0.055F) : 12.92F * b;
        return new RGBColor(r, g, b, color.alpha());
    }

    public static LABColor XYZtoLAB(XYZColor color) {
        float x = color.x() / 0.95047F;
        float y = color.y() / 1.0F;
        float z = color.z() / 1.08883F;
        x = x > 0.008856F ? (float) Math.cbrt((double) x) : 7.787F * x + 0.13793103F;
        y = y > 0.008856F ? (float) Math.cbrt((double) y) : 7.787F * y + 0.13793103F;
        z = z > 0.008856F ? (float) Math.cbrt((double) z) : 7.787F * z + 0.13793103F;
        float l = 116.0F * y - 16.0F;
        float a = 500.0F * (x - y);
        float b = 200.0F * (y - z);
        return new LABColor(l / 100.0F, a / 255.0F, b / 255.0F, color.alpha());
    }

    public static XYZColor LABtoXYZ(LABColor color) {
        float y0 = (color.luminance() * 100.0F + 16.0F) / 116.0F;
        float x0 = color.a() * 255.0F / 500.0F + y0;
        float z0 = y0 - color.b() * 255.0F / 200.0F;
        float x3 = x0 * x0 * x0;
        float x = (float) ((x3 > 0.008856F ? (double) x3 : (double) (x0 - 0.13793103F) / 7.787) * 0.95047F);
        float y3 = y0 * y0 * y0;
        float y = (float) ((y3 > 0.008856F ? (double) y3 : (double) (y0 - 0.13793103F) / 7.787) * 1.0);
        float z3 = z0 * z0 * z0;
        float z = (float) ((z3 > 0.008856F ? (double) z3 : (double) (z0 - 0.13793103F) / 7.787) * 1.08883F);
        return new XYZColor(x, y, z, color.alpha());
    }

    public static HCLColor LABtoHCL(LABColor color) {
        float l = color.luminance();
        float a = color.a();
        float b = color.b();
        float c = (float) Math.sqrt((double) (a * a + b * b));
        float h = (float) Math.atan2((double) b, (double) a);
        h = (float) ((double) h / (Math.PI * 2));
        while (h < 0.0F) {
            h++;
        }
        if (c < 0.0F || h < 0.0F || c > 1.0F || h > 1.0F) {
            boolean var6 = true;
        }
        return new HCLColor(h, c, l, color.alpha());
    }

    public static LABColor HCLtoLAB(HCLColor color) {
        float h = color.hue();
        float c = color.chroma();
        float l = color.luminance();
        float a = (float) ((double) c * Math.cos((double) h * Math.PI * 2.0));
        float b = (float) ((double) c * Math.sin((double) h * Math.PI * 2.0));
        return new LABColor(l, a, b, color.alpha());
    }

    public static LUVColor XYZtoLUV(XYZColor color) {
        float X = color.x();
        float Y = color.y();
        float Z = color.z();
        float P = X + 15.0F * Y + 3.0F * Z;
        float uP = 4.0F * X / P;
        float vP = 9.0F * Y / P;
        float f = 0.20689656F;
        float F = f * f * f;
        float L = (float) (Y <= F ? Math.pow(9.666667F, 3.0) * (double) Y : 116.0 * Math.cbrt((double) Y) - 16.0);
        float u = 13.0F * L * (uP - 0.2009F);
        float v = 13.0F * L * (vP - 0.461F);
        return new LUVColor(L / 255.0F, u / 255.0F, v / 255.0F, color.alpha());
    }

    public static XYZColor LUVtoXYZ(LUVColor color) {
        float L = color.luminance() * 255.0F;
        float u = color.u() * 255.0F;
        float v = color.v() * 255.0F;
        float uP = 0.2009F + u / (13.0F * L);
        float vP = 0.461F + v / (13.0F * L);
        float Y = (float) (L <= 8.0F ? (double) L * Math.pow(0.10344828F, 3.0) : Math.pow((double) ((L + 16.0F) / 116.0F), 3.0));
        float X = Y * 9.0F * uP / (4.0F * vP);
        float Z = Y * (12.0F - 3.0F * uP - 20.0F * vP) / (4.0F * vP);
        return new XYZColor(X, Y, Z, color.alpha());
    }

    public static HCLVColor LUVtoHCLV(LUVColor color) {
        float l = color.luminance();
        float a = color.u();
        float b = color.v();
        float c = (float) Math.sqrt((double) (a * a + b * b));
        float h = (float) Math.atan2((double) b, (double) a);
        h = (float) ((double) h / (Math.PI * 2));
        while (h < 0.0F) {
            h++;
        }
        return new HCLVColor(h, c, l, color.alpha());
    }

    public static LUVColor HCLVtoLUV(HCLVColor color) {
        float h = color.hue();
        float c = color.chroma();
        float l = color.luminance();
        float u = (float) ((double) c * Math.cos((double) h * Math.PI * 2.0));
        float v = (float) ((double) c * Math.sin((double) h * Math.PI * 2.0));
        return new LUVColor(l, u, v, color.alpha());
    }
}