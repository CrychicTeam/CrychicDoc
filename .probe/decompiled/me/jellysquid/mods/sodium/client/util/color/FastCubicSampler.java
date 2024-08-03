package me.jellysquid.mods.sodium.client.util.color;

import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class FastCubicSampler {

    private static final double[] DENSITY_CURVE = new double[] { 0.0, 1.0, 4.0, 6.0, 4.0, 1.0, 0.0 };

    private static final int DIAMETER = 6;

    public static Vec3 sampleColor(Vec3 pos, FastCubicSampler.ColorFetcher colorFetcher, Function<Vec3, Vec3> transformer) {
        int intX = Mth.floor(pos.x());
        int intY = Mth.floor(pos.y());
        int intZ = Mth.floor(pos.z());
        int[] values = new int[216];
        for (int x = 0; x < 6; x++) {
            int blockX = intX - 2 + x;
            for (int y = 0; y < 6; y++) {
                int blockY = intY - 2 + y;
                for (int z = 0; z < 6; z++) {
                    int blockZ = intZ - 2 + z;
                    values[index(x, y, z)] = colorFetcher.fetch(blockX, blockY, blockZ);
                }
            }
        }
        if (isHomogenousArray(values)) {
            return (Vec3) transformer.apply(Vec3.fromRGB24(values[0]));
        } else {
            double deltaX = pos.x() - (double) intX;
            double deltaY = pos.y() - (double) intY;
            double deltaZ = pos.z() - (double) intZ;
            Vec3 sum = Vec3.ZERO;
            double totalFactor = 0.0;
            for (int x = 0; x < 6; x++) {
                double densityX = Mth.lerp(deltaX, DENSITY_CURVE[x + 1], DENSITY_CURVE[x]);
                for (int y = 0; y < 6; y++) {
                    double densityY = Mth.lerp(deltaY, DENSITY_CURVE[y + 1], DENSITY_CURVE[y]);
                    for (int z = 0; z < 6; z++) {
                        double densityZ = Mth.lerp(deltaZ, DENSITY_CURVE[z + 1], DENSITY_CURVE[z]);
                        double factor = densityX * densityY * densityZ;
                        totalFactor += factor;
                        Vec3 color = (Vec3) transformer.apply(Vec3.fromRGB24(values[index(x, y, z)]));
                        sum = sum.add(color.scale(factor));
                    }
                }
            }
            return sum.scale(1.0 / totalFactor);
        }
    }

    private static int index(int x, int y, int z) {
        return 36 * z + 6 * y + x;
    }

    private static boolean isHomogenousArray(int[] arr) {
        int val = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != val) {
                return false;
            }
        }
        return true;
    }

    public interface ColorFetcher {

        int fetch(int var1, int var2, int var3);
    }
}