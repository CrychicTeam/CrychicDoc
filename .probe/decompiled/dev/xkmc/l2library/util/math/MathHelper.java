package dev.xkmc.l2library.util.math;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.world.phys.Vec3;

public class MathHelper {

    public static double horSq(Vec3 vec3) {
        return vec3.x * vec3.x + vec3.z * vec3.z;
    }

    public static UUID getUUIDFromString(String str) {
        int hash = str.hashCode();
        Random r = new Random((long) hash);
        long l0 = r.nextLong();
        long l1 = r.nextLong();
        return new UUID(l0, l1);
    }

    @Nullable
    public static <T> T pick(List<T> list, Function<T, Integer> func, double random) {
        int total = 0;
        for (T t : list) {
            total += func.apply(t);
        }
        double val = random * (double) total;
        for (T t : list) {
            val -= (double) ((Integer) func.apply(t)).intValue();
            if (val < 0.0) {
                return t;
            }
        }
        return null;
    }
}