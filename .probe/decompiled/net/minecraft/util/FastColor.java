package net.minecraft.util;

public class FastColor {

    public static class ABGR32 {

        public static int alpha(int int0) {
            return int0 >>> 24;
        }

        public static int red(int int0) {
            return int0 & 0xFF;
        }

        public static int green(int int0) {
            return int0 >> 8 & 0xFF;
        }

        public static int blue(int int0) {
            return int0 >> 16 & 0xFF;
        }

        public static int transparent(int int0) {
            return int0 & 16777215;
        }

        public static int opaque(int int0) {
            return int0 | 0xFF000000;
        }

        public static int color(int int0, int int1, int int2, int int3) {
            return int0 << 24 | int1 << 16 | int2 << 8 | int3;
        }

        public static int color(int int0, int int1) {
            return int0 << 24 | int1 & 16777215;
        }
    }

    public static class ARGB32 {

        public static int alpha(int int0) {
            return int0 >>> 24;
        }

        public static int red(int int0) {
            return int0 >> 16 & 0xFF;
        }

        public static int green(int int0) {
            return int0 >> 8 & 0xFF;
        }

        public static int blue(int int0) {
            return int0 & 0xFF;
        }

        public static int color(int int0, int int1, int int2, int int3) {
            return int0 << 24 | int1 << 16 | int2 << 8 | int3;
        }

        public static int multiply(int int0, int int1) {
            return color(alpha(int0) * alpha(int1) / 255, red(int0) * red(int1) / 255, green(int0) * green(int1) / 255, blue(int0) * blue(int1) / 255);
        }

        public static int lerp(float float0, int int1, int int2) {
            int $$3 = Mth.lerpInt(float0, alpha(int1), alpha(int2));
            int $$4 = Mth.lerpInt(float0, red(int1), red(int2));
            int $$5 = Mth.lerpInt(float0, green(int1), green(int2));
            int $$6 = Mth.lerpInt(float0, blue(int1), blue(int2));
            return color($$3, $$4, $$5, $$6);
        }
    }
}