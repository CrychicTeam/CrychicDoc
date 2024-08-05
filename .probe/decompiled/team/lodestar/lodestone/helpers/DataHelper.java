package team.lodestar.lodestone.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DataHelper {

    public static <T, K extends Collection<T>> K reverseOrder(K reversed, Collection<T> items) {
        ArrayList<T> original = new ArrayList(items);
        for (int i = items.size() - 1; i >= 0; i--) {
            reversed.add(original.get(i));
        }
        return reversed;
    }

    public static String toTitleCase(String givenString, String regex) {
        String[] stringArray = givenString.split(regex);
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : stringArray) {
            stringBuilder.append(Character.toUpperCase(string.charAt(0))).append(string.substring(1)).append(regex);
        }
        return stringBuilder.toString().trim().replaceAll(regex, " ").substring(0, stringBuilder.length() - 1);
    }

    public static int[] nextInts(int count, int range) {
        Random rand = new Random();
        int[] ints = new int[count];
        for (int i = 0; i < count; i++) {
            int nextInt;
            do {
                nextInt = rand.nextInt(range);
            } while (!Arrays.stream(ints).noneMatch(j -> j == nextInt));
            ints[i] = nextInt;
        }
        return ints;
    }

    public static <T> boolean hasDuplicate(T[] things) {
        Set<T> thingSet = new HashSet();
        return !Arrays.stream(things).allMatch(thingSet::add);
    }

    public static <T> T take(Collection<? extends T> src, T item) {
        src.remove(item);
        return item;
    }

    @SafeVarargs
    public static <T> Collection<T> takeAll(Collection<? extends T> src, T... items) {
        List<T> ret = Arrays.asList(items);
        for (T item : items) {
            if (!src.contains(item)) {
                return Collections.emptyList();
            }
        }
        return !src.removeAll(ret) ? Collections.emptyList() : ret;
    }

    public static <T> Collection<T> takeAll(Collection<T> src, Predicate<T> pred) {
        List<T> ret = new ArrayList();
        Iterator<T> iter = src.iterator();
        while (iter.hasNext()) {
            T item = (T) iter.next();
            if (pred.test(item)) {
                iter.remove();
                ret.add(item);
            }
        }
        return ret.isEmpty() ? Collections.emptyList() : ret;
    }

    @SafeVarargs
    public static <T> Collection<T> getAll(Collection<? extends T> src, T... items) {
        return List.copyOf(getAll((Collection<T>) src, t -> Arrays.stream(items).anyMatch(tAgain -> tAgain.getClass().isInstance(t))));
    }

    public static <T> Collection<T> getAll(Collection<T> src, Predicate<T> pred) {
        return (Collection<T>) src.stream().filter(pred).collect(Collectors.toList());
    }

    public static Vec3 radialOffset(Vec3 pos, float distance, float current, float total) {
        double angle = (double) (current / total) * (Math.PI * 2);
        double dx2 = (double) distance * Math.cos(angle);
        double dz2 = (double) distance * Math.sin(angle);
        Vec3 vector = new Vec3(dx2, 0.0, dz2);
        double x = vector.x * (double) distance;
        double z = vector.z * (double) distance;
        return pos.add(new Vec3(x, 0.0, z));
    }

    public static ArrayList<Vec3> rotatingRadialOffsets(Vec3 pos, float distance, float total, long gameTime, float time) {
        return rotatingRadialOffsets(pos, distance, distance, total, gameTime, time);
    }

    public static ArrayList<Vec3> rotatingRadialOffsets(Vec3 pos, float distanceX, float distanceZ, float total, long gameTime, float time) {
        ArrayList<Vec3> positions = new ArrayList();
        for (int i = 0; (float) i <= total; i++) {
            positions.add(rotatingRadialOffset(pos, distanceX, distanceZ, (float) i, total, gameTime, time));
        }
        return positions;
    }

    public static Vec3 rotatingRadialOffset(Vec3 pos, float distance, float current, float total, long gameTime, float time) {
        return rotatingRadialOffset(pos, distance, distance, current, total, gameTime, time);
    }

    public static Vec3 rotatingRadialOffset(Vec3 pos, float distanceX, float distanceZ, float current, float total, long gameTime, float time) {
        double angle = (double) (current / total) * (Math.PI * 2);
        angle += (double) ((float) gameTime % time / time) * (Math.PI * 2);
        double dx2 = (double) distanceX * Math.cos(angle);
        double dz2 = (double) distanceZ * Math.sin(angle);
        Vec3 vector2f = new Vec3(dx2, 0.0, dz2);
        double x = vector2f.x * (double) distanceX;
        double z = vector2f.z * (double) distanceZ;
        return pos.add(x, 0.0, z);
    }

    public static ArrayList<Vec3> blockOutlinePositions(Level level, BlockPos pos) {
        ArrayList<Vec3> arrayList = new ArrayList();
        double d0 = 0.5625;
        RandomSource random = level.random;
        for (Direction direction : Direction.values()) {
            BlockPos blockpos = pos.relative(direction);
            if (!level.getBlockState(blockpos).m_60804_(level, blockpos)) {
                Direction.Axis direction$axis = direction.getAxis();
                double d1 = direction$axis == Direction.Axis.X ? 0.5 + d0 * (double) direction.getStepX() : (double) random.nextFloat();
                double d2 = direction$axis == Direction.Axis.Y ? 0.5 + d0 * (double) direction.getStepY() : (double) random.nextFloat();
                double d3 = direction$axis == Direction.Axis.Z ? 0.5 + d0 * (double) direction.getStepZ() : (double) random.nextFloat();
                arrayList.add(new Vec3((double) pos.m_123341_() + d1, (double) pos.m_123342_() + d2, (double) pos.m_123343_() + d3));
            }
        }
        return arrayList;
    }

    public static float distSqr(float... a) {
        float d = 0.0F;
        for (float f : a) {
            d += f * f;
        }
        return d;
    }

    public static float distance(float... a) {
        return Mth.sqrt(distSqr(a));
    }
}