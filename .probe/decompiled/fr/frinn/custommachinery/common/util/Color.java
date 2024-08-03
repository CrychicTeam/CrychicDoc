package fr.frinn.custommachinery.common.util;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.impl.codec.NamedMapCodec;
import java.util.stream.DoubleStream;
import net.minecraft.util.FastColor;

public class Color {

    public static final NamedMapCodec<Color> MAP_CODEC = NamedCodec.record(colorInstance -> colorInstance.group(NamedCodec.FLOAT.optionalFieldOf("alpha", 1.0F).forGetter(color -> (float) color.getAlpha() / 255.0F), NamedCodec.FLOAT.optionalFieldOf("red", 1.0F).forGetter(color -> (float) color.getRed() / 255.0F), NamedCodec.FLOAT.optionalFieldOf("green", 1.0F).forGetter(color -> (float) color.getGreen() / 255.0F), NamedCodec.FLOAT.optionalFieldOf("blue", 1.0F).forGetter(color -> (float) color.getBlue() / 255.0F)).apply(colorInstance, Color::fromColors), "Color");

    public static final NamedCodec<Color> ARRAY_CODEC = NamedCodec.DOUBLE_STREAM.comapFlatMap(stream -> NamedCodec.validateDoubleStreamSize(stream, 4).map(array -> fromColors(array[0], array[1], array[2], array[3])), color -> DoubleStream.of(new double[] { (double) ((float) color.getAlpha() / 255.0F), (double) ((float) color.getRed() / 255.0F), (double) ((float) color.getGreen() / 255.0F), (double) ((float) color.getBlue() / 255.0F) }), "Color");

    public static final NamedCodec<Color> CODEC = EitherManyCodec.of(MAP_CODEC, ARRAY_CODEC, NamedCodec.intRange(0, Integer.MAX_VALUE).xmap(Color::fromARGB, Color::getARGB, ""));

    public static final Color WHITE = fromColors(255, 255, 255, 255);

    public static final Color TRANSPARENT_WHITE = fromColors(127, 255, 255, 255);

    private final int alpha;

    private final int red;

    private final int green;

    private final int blue;

    public static Color fromARGB(int colorARGB) {
        return new Color(FastColor.ARGB32.alpha(colorARGB), FastColor.ARGB32.red(colorARGB), FastColor.ARGB32.green(colorARGB), FastColor.ARGB32.blue(colorARGB));
    }

    public static Color fromColors(int alpha, int red, int green, int blue) {
        return new Color(alpha, red, green, blue);
    }

    public static Color fromColors(float alpha, float red, float green, float blue) {
        return fromColors((int) (alpha * 255.0F), (int) (red * 255.0F), (int) (green * 255.0F), (int) (blue * 255.0F));
    }

    public static Color fromColors(double alpha, double red, double green, double blue) {
        return fromColors((int) (alpha * 255.0), (int) (red * 255.0), (int) (green * 255.0), (int) (blue * 255.0));
    }

    private Color(int alpha, int red, int green, int blue) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getAlpha() {
        return this.alpha;
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

    public int getARGB() {
        return FastColor.ARGB32.color(this.alpha, this.red, this.green, this.blue);
    }
}