package net.minecraft.util.profiling.jfr;

import com.google.common.math.Quantiles;
import com.google.common.math.Quantiles.ScaleAndIndexes;
import it.unimi.dsi.fastutil.ints.Int2DoubleRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleSortedMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleSortedMaps;
import java.util.Comparator;
import java.util.Map;
import net.minecraft.Util;

public class Percentiles {

    public static final ScaleAndIndexes DEFAULT_INDEXES = Quantiles.scale(100).indexes(new int[] { 50, 75, 90, 99 });

    private Percentiles() {
    }

    public static Map<Integer, Double> evaluate(long[] long0) {
        return long0.length == 0 ? Map.of() : sorted(DEFAULT_INDEXES.compute(long0));
    }

    public static Map<Integer, Double> evaluate(double[] double0) {
        return double0.length == 0 ? Map.of() : sorted(DEFAULT_INDEXES.compute(double0));
    }

    private static Map<Integer, Double> sorted(Map<Integer, Double> mapIntegerDouble0) {
        Int2DoubleSortedMap $$1 = Util.make(new Int2DoubleRBTreeMap(Comparator.reverseOrder()), p_185389_ -> p_185389_.putAll(mapIntegerDouble0));
        return Int2DoubleSortedMaps.unmodifiable($$1);
    }
}