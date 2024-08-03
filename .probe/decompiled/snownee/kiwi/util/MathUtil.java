package snownee.kiwi.util;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class MathUtil {

    private MathUtil() {
    }

    public static List<Vec3> fibonacciSphere(Vec3 start, double radius, int samples, boolean randomize) {
        double rnd = 1.0;
        if (randomize) {
            rnd = Math.random() * (double) samples;
        }
        double offset = 2.0 / (double) samples;
        double increment = Math.PI * (3.0 - Math.sqrt(5.0));
        List<Vec3> points = Lists.newArrayListWithCapacity(samples);
        for (int i = 0; i < samples; i++) {
            double y = (double) i * offset - 1.0 + offset / 2.0;
            double r = Math.sqrt(1.0 - y * y) * radius;
            double phi = ((double) i + rnd) % (double) samples * increment;
            double x = Math.cos(phi) * r;
            double z = Math.sin(phi) * r;
            points.add(new Vec3(start.x + x, start.y + y * radius, start.z + z));
        }
        return points;
    }

    public static int posOnLine(Vec3 start, Vec3 end, Collection<BlockPos> list) {
        list.add(BlockPos.containing(start));
        if (start.equals(end)) {
            return 1;
        } else {
            int c = 1;
            double ex = Mth.lerp(-1.0E-7, end.x, start.x);
            double ey = Mth.lerp(-1.0E-7, end.y, start.y);
            double ez = Mth.lerp(-1.0E-7, end.z, start.z);
            double sx = Mth.lerp(-1.0E-7, start.x, end.x);
            double sy = Mth.lerp(-1.0E-7, start.y, end.y);
            double sz = Mth.lerp(-1.0E-7, start.z, end.z);
            int x = Mth.floor(sx);
            int y = Mth.floor(sy);
            int z = Mth.floor(sz);
            double subX = ex - sx;
            double subY = ey - sy;
            double subZ = ez - sz;
            int signX = Mth.sign(subX);
            int signY = Mth.sign(subY);
            int signZ = Mth.sign(subZ);
            double d9 = signX == 0 ? Double.MAX_VALUE : (double) signX / subX;
            double d10 = signY == 0 ? Double.MAX_VALUE : (double) signY / subY;
            double d11 = signZ == 0 ? Double.MAX_VALUE : (double) signZ / subZ;
            double d12 = d9 * (signX > 0 ? 1.0 - Mth.frac(sx) : Mth.frac(sx));
            double d13 = d10 * (signY > 0 ? 1.0 - Mth.frac(sy) : Mth.frac(sy));
            for (double d14 = d11 * (signZ > 0 ? 1.0 - Mth.frac(sz) : Mth.frac(sz)); d12 <= 1.0 || d13 <= 1.0 || d14 <= 1.0; c++) {
                if (d12 < d13) {
                    if (d12 < d14) {
                        x += signX;
                        d12 += d9;
                    } else {
                        z += signZ;
                        d14 += d11;
                    }
                } else if (d13 < d14) {
                    y += signY;
                    d13 += d10;
                } else {
                    z += signZ;
                    d14 += d11;
                }
                list.add(new BlockPos(x, y, z));
            }
            return c;
        }
    }

    public static Vector3f RGBtoHSV(int rgb) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));
        float v = (float) max;
        float delta = (float) (max - min);
        if (max != 0) {
            float s = delta / (float) max;
            float h;
            if (r == max) {
                h = (float) (g - b) / delta;
            } else if (g == max) {
                h = 2.0F + (float) (b - r) / delta;
            } else {
                h = 4.0F + (float) (r - g) / delta;
            }
            h /= 6.0F;
            if (h < 0.0F) {
                h++;
            }
            return new Vector3f(h, s, v / 255.0F);
        } else {
            float sx = 0.0F;
            float hx = -1.0F;
            return new Vector3f(hx, sx, 0.0F);
        }
    }
}