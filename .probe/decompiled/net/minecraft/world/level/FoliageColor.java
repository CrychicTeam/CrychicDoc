package net.minecraft.world.level;

public class FoliageColor {

    private static int[] pixels = new int[65536];

    public static void init(int[] int0) {
        pixels = int0;
    }

    public static int get(double double0, double double1) {
        double1 *= double0;
        int $$2 = (int) ((1.0 - double0) * 255.0);
        int $$3 = (int) ((1.0 - double1) * 255.0);
        int $$4 = $$3 << 8 | $$2;
        return $$4 >= pixels.length ? getDefaultColor() : pixels[$$4];
    }

    public static int getEvergreenColor() {
        return 6396257;
    }

    public static int getBirchColor() {
        return 8431445;
    }

    public static int getDefaultColor() {
        return 4764952;
    }

    public static int getMangroveColor() {
        return 9619016;
    }
}