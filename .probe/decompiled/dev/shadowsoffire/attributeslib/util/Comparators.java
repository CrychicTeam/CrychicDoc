package dev.shadowsoffire.attributeslib.util;

import java.util.Comparator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class Comparators {

    @SafeVarargs
    public static <T> Comparator<T> chained(Comparator<T>... comparators) {
        Comparator<T> c = comparators[0];
        for (int i = 1; i < comparators.length; i++) {
            c = c.thenComparing(comparators[i]);
        }
        return c;
    }

    public static <T> Comparator<T> idComparator(Registry<T> reg) {
        return Comparator.comparing(reg::m_7981_, ResourceLocation::compareTo);
    }
}