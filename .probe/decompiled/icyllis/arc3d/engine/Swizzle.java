package icyllis.arc3d.engine;

import icyllis.arc3d.core.Size;
import org.jetbrains.annotations.Contract;

public final class Swizzle {

    public static final short RGBA = 12816;

    public static final short BGRA = 12306;

    public static final short RGB1 = 21008;

    public static final short BGR1 = 20498;

    public static final short AAAA = 13107;

    @Contract(pure = true)
    public static int charToIndex(char c) {
        return switch(c) {
            case '0' ->
                4;
            case '1' ->
                5;
            case 'a' ->
                3;
            case 'b' ->
                2;
            case 'g' ->
                1;
            case 'r' ->
                0;
            default ->
                throw new AssertionError(c);
        };
    }

    @Contract(pure = true)
    public static char indexToChar(int idx) {
        return switch(idx) {
            case 0 ->
                'r';
            case 1 ->
                'g';
            case 2 ->
                'b';
            case 3 ->
                'a';
            case 4 ->
                '0';
            case 5 ->
                '1';
            default ->
                throw new AssertionError(idx);
        };
    }

    @Contract(pure = true)
    public static short make(String s) {
        return make(s.charAt(0), s.charAt(1), s.charAt(2), s.charAt(3));
    }

    @Contract(pure = true)
    public static short make(char r, char g, char b, char a) {
        return (short) (charToIndex(r) | charToIndex(g) << 4 | charToIndex(b) << 8 | charToIndex(a) << 12);
    }

    public static short concat(short a, short b) {
        int swizzle = 0;
        for (int i = 0; i < 4; i++) {
            int idx = b >> 4 * i & 15;
            if (idx != 4 && idx != 5) {
                assert idx < 4;
                idx = a >> 4 * idx & 15;
            }
            swizzle |= idx << 4 * i;
        }
        return (short) swizzle;
    }

    public static float[] apply(short swizzle, @Size(min = 4L) float[] v) {
        float r = v[0];
        float g = v[1];
        float b = v[2];
        float a = v[3];
        for (int i = 0; i < 4; i++) {
            v[i] = switch(swizzle & 15) {
                case 0 ->
                    r;
                case 1 ->
                    g;
                case 2 ->
                    b;
                case 3 ->
                    a;
                case 4 ->
                    0.0F;
                case 5 ->
                    1.0F;
                default ->
                    throw new IllegalStateException();
            };
            swizzle = (short) (swizzle >> 4);
        }
        return v;
    }

    public static String toString(short swizzle) {
        return "" + indexToChar(swizzle & 15) + indexToChar(swizzle >> 4 & 15) + indexToChar(swizzle >> 8 & 15) + indexToChar(swizzle >>> 12);
    }

    static {
        assert make('r', 'g', 'b', 'a') == 12816;
        assert make('b', 'g', 'r', 'a') == 12306;
        assert make('r', 'g', 'b', '1') == 21008;
        assert make('b', 'g', 'r', '1') == 20498;
        assert make('a', 'a', 'a', 'a') == 13107;
        assert concat(make('1', '1', '1', 'r'), (short) 13107) == make('r', 'r', 'r', 'r');
    }
}