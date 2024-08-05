package snownee.jade.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.math.BigDecimal;
import java.util.Objects;

public class Color {

    public static final Codec<Integer> CODEC = Codec.STRING.comapFlatMap($ -> {
        try {
            return DataResult.success(valueOf($).toInt());
        } catch (IllegalArgumentException var2) {
            return DataResult.error(() -> "Invalid color: " + $);
        }
    }, $ -> rgb($).getHex());

    private int red;

    private int green;

    private int blue;

    private double hue;

    private double saturation;

    private double lightness;

    private double opacity;

    private String hex;

    private Color() {
    }

    public static Color rgb(int red, int green, int blue) {
        return rgb(red, green, blue, 1.0);
    }

    public static Color rgb(int red, int green, int blue, double opacity) {
        Color color = new Color();
        color.red = red;
        color.green = green;
        color.blue = blue;
        color.opacity = opacity;
        double r = (double) red / 255.0;
        double g = (double) green / 255.0;
        double b = (double) blue / 255.0;
        double max = Math.max(Math.max(r, g), b);
        double min = Math.min(Math.min(r, g), b);
        double delta = max - min;
        color.lightness = (max + min) / 2.0;
        color.saturation = delta == 0.0 ? 0.0 : delta / (1.0 - Math.abs(2.0 * color.lightness - 1.0));
        if (delta == 0.0) {
            color.hue = 0.0;
        } else if (max == r) {
            color.hue = 60.0 * ((g - b) / delta + 0.0);
        } else if (max == g) {
            color.hue = 60.0 * ((b - r) / delta + 2.0);
        } else if (max == b) {
            color.hue = 60.0 * ((r - g) / delta + 4.0);
        }
        color.hue = color.hue < 0.0 ? color.hue + 360.0 : (color.hue > 360.0 ? 360.0 : color.hue);
        color.hex = String.format("#%02x%02x%02x", red, green, blue);
        return color;
    }

    public static Color hsl(double hue, double saturation, double lightness) {
        return hsl(hue, saturation, lightness, 1.0);
    }

    public static Color hsl(double hue, double saturation, double lightness, double opacity) {
        double _c = (1.0 - Math.abs(2.0 * lightness - 1.0)) * saturation;
        double _h = hue / 60.0;
        double _x = _c * (1.0 - Math.abs(_h % 2.0 - 1.0));
        double[] _rgb = new double[] { 0.0, 0.0, 0.0 };
        if (_h >= 0.0 && _h < 1.0) {
            _rgb = new double[] { _c, _x, 0.0 };
        } else if (_h >= 1.0 && _h < 2.0) {
            _rgb = new double[] { _x, _c, 0.0 };
        } else if (_h >= 2.0 && _h < 3.0) {
            _rgb = new double[] { 0.0, _c, _x };
        } else if (_h >= 3.0 && _h < 4.0) {
            _rgb = new double[] { 0.0, _x, _c };
        } else if (_h >= 4.0 && _h < 5.0) {
            _rgb = new double[] { _x, 0.0, _c };
        } else if (_h >= 5.0 && _h < 6.0) {
            _rgb = new double[] { _c, 0.0, _x };
        }
        double _m = lightness - _c / 2.0;
        int red = (int) ((_rgb[0] + _m) * 255.0);
        int green = (int) ((_rgb[1] + _m) * 255.0);
        int blue = (int) ((_rgb[2] + _m) * 255.0);
        Color color = rgb(red, green, blue, opacity);
        color.hue = hue;
        color.saturation = saturation;
        color.lightness = lightness;
        return color;
    }

    public static Color hex(String value) {
        value = value.trim();
        String hex = value.startsWith("#") ? value.substring(1) : value;
        String filledHex = fill(hex);
        int red = Integer.parseInt(filledHex.substring(0, 2), 16);
        int green = Integer.parseInt(filledHex.substring(2, 4), 16);
        int blue = Integer.parseInt(filledHex.substring(4, 6), 16);
        double opacity = 1.0;
        if (filledHex.length() == 8) {
            opacity = (double) Integer.parseInt(filledHex.substring(6, 8), 16) / 255.0;
        }
        Color color = rgb(red, green, blue, opacity);
        color.hex = "#".concat(hex);
        return color;
    }

    public static Color valueOf(String value) throws IllegalArgumentException {
        value = value.trim().toLowerCase();
        if (!value.contains("rgb")) {
            if (value.contains("hsl")) {
                String[] hsl = split(value);
                double hue = toDegrees(hsl[0]);
                double saturation = parseDouble(hsl[1], 1.0);
                double lightness = parseDouble(hsl[2], 1.0);
                double opacity = hsl.length >= 4 ? parseDouble(hsl[3], 1.0) : 1.0;
                return hsl(hue, saturation, lightness, opacity);
            } else if ("transparent".equals(value)) {
                return rgb(0, 0, 0, 0.0);
            } else if (!value.startsWith("#") && value.length() != 3 && value.length() != 4 && value.length() != 6 && value.length() != 8) {
                throw new IllegalArgumentException(value + " cannot be recognized.");
            } else {
                return hex(value);
            }
        } else {
            String[] rgb = split(value);
            if (rgb[0].contains("%") || rgb[1].contains("%") || rgb[2].contains("%")) {
                boolean makeSense = rgb[0].contains("%") && rgb[1].contains("%") && rgb[2].contains("%");
                if (!makeSense) {
                    throw new IllegalArgumentException("Cannot mix numbers and percentages in RGB calculations.");
                }
            }
            int red = parseInt(rgb[0], 255.0);
            int green = parseInt(rgb[1], 255.0);
            int blue = parseInt(rgb[2], 255.0);
            double opacity = rgb.length >= 4 ? parseDouble(rgb[3], 1.0) : 1.0;
            return rgb(red, green, blue, opacity);
        }
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

    public double getHue() {
        return this.hue;
    }

    public double getSaturation() {
        return this.saturation;
    }

    public double getLightness() {
        return this.lightness;
    }

    public double getOpacity() {
        return this.opacity;
    }

    public String getHex() {
        return this.hex;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Color color = (Color) o;
            return this.red == color.red && this.green == color.green && this.blue == color.blue && Double.compare(color.hue, this.hue) == 0 && Double.compare(color.saturation, this.saturation) == 0 && Double.compare(color.lightness, this.lightness) == 0 && Double.compare(color.opacity, this.opacity) == 0;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.red, this.green, this.blue, this.hue, this.saturation, this.lightness, this.opacity });
    }

    public String toString() {
        return "Color{red=" + this.red + ", green=" + this.green + ", blue=" + this.blue + ", hue=" + this.hue + ", saturation=" + this.saturation + ", lightness=" + this.lightness + ", opacity=" + this.opacity + ", hex='" + this.hex + "'}";
    }

    private static String fill(String hex) {
        if (hex.length() != 3 && hex.length() != 4) {
            return hex;
        } else {
            String value = "";
            for (char letter : hex.toCharArray()) {
                value = value.concat(String.valueOf(new char[] { letter, letter }));
            }
            return value;
        }
    }

    private static double parseDouble(String value, double limit) {
        boolean hasPercent = value.contains("%");
        value = value.replace("%", "").trim();
        double number = Double.valueOf(value);
        return hasPercent ? BigDecimal.valueOf(number).multiply(BigDecimal.valueOf(limit / 100.0)).doubleValue() : number;
    }

    private static int parseInt(String value, double limit) {
        double number = parseDouble(value, limit);
        int round = (int) Math.round(number);
        return (double) round > limit ? (int) limit : round;
    }

    private static String[] split(String value) {
        value = value.replace("/", " ");
        value = value.replaceAll("(\\s)+", " ");
        value = value.substring(value.indexOf("(") + 1, value.indexOf(")"));
        return value.contains(",") ? value.split(",") : value.split(" ");
    }

    private static double toDegrees(String value) {
        value = value.toLowerCase().trim();
        if (value.contains("deg")) {
            return Double.valueOf(value.replace("deg", "").trim());
        } else if (value.contains("grad")) {
            return Double.valueOf(value.replace("grad", "").trim()) / 400.0 * 360.0;
        } else if (value.contains("rad")) {
            return Double.valueOf(value.replace("rad", "").trim()) * (180.0 / Math.PI);
        } else {
            return value.contains("turn") ? Double.valueOf(value.replace("turn", "").trim()) * 360.0 : parseDouble(value, 360.0);
        }
    }

    public int toInt() {
        return (((int) (this.opacity * 255.0) & 0xFF) << 24) + ((this.red & 0xFF) << 16) + ((this.green & 0xFF) << 8) + (this.blue & 0xFF);
    }

    public static Color rgb(int color) {
        double a = (double) (color >> 24 & 0xFF) / 255.0;
        if (a == 0.0) {
            a = 1.0;
        }
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        return rgb(r, g, b, a);
    }
}