package net.minecraft.util.random;

import java.util.List;
import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.util.RandomSource;

public class WeightedRandom {

    private WeightedRandom() {
    }

    public static int getTotalWeight(List<? extends WeightedEntry> listExtendsWeightedEntry0) {
        long $$1 = 0L;
        for (WeightedEntry $$2 : listExtendsWeightedEntry0) {
            $$1 += (long) $$2.getWeight().asInt();
        }
        if ($$1 > 2147483647L) {
            throw new IllegalArgumentException("Sum of weights must be <= 2147483647");
        } else {
            return (int) $$1;
        }
    }

    public static <T extends WeightedEntry> Optional<T> getRandomItem(RandomSource randomSource0, List<T> listT1, int int2) {
        if (int2 < 0) {
            throw (IllegalArgumentException) Util.pauseInIde((T) (new IllegalArgumentException("Negative total weight in getRandomItem")));
        } else if (int2 == 0) {
            return Optional.empty();
        } else {
            int $$3 = randomSource0.nextInt(int2);
            return getWeightedItem(listT1, $$3);
        }
    }

    public static <T extends WeightedEntry> Optional<T> getWeightedItem(List<T> listT0, int int1) {
        for (T $$2 : listT0) {
            int1 -= $$2.getWeight().asInt();
            if (int1 < 0) {
                return Optional.of($$2);
            }
        }
        return Optional.empty();
    }

    public static <T extends WeightedEntry> Optional<T> getRandomItem(RandomSource randomSource0, List<T> listT1) {
        return getRandomItem(randomSource0, listT1, getTotalWeight(listT1));
    }
}