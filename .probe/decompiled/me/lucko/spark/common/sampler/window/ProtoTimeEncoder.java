package me.lucko.spark.common.sampler.window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongToDoubleFunction;
import java.util.stream.IntStream;
import me.lucko.spark.common.sampler.node.ThreadNode;

public class ProtoTimeEncoder {

    private final LongToDoubleFunction valueTransformer;

    private final int[] keys;

    private final Map<Integer, Integer> keysToIndex;

    public ProtoTimeEncoder(LongToDoubleFunction valueTransformer, List<ThreadNode> sourceData) {
        this.valueTransformer = valueTransformer;
        this.keys = ((IntStream) sourceData.stream().map(n -> n.getTimeWindows().stream().mapToInt(ix -> ix)).reduce(IntStream.empty(), IntStream::concat)).distinct().sorted().toArray();
        this.keysToIndex = new HashMap(this.keys.length);
        for (int i = 0; i < this.keys.length; i++) {
            this.keysToIndex.put(this.keys[i], i);
        }
    }

    public int[] getKeys() {
        return this.keys;
    }

    public double[] encode(Map<Integer, LongAdder> times) {
        double[] array = new double[this.keys.length];
        times.forEach((key, value) -> {
            Integer idx = (Integer) this.keysToIndex.get(key);
            if (idx == null) {
                throw new RuntimeException("No index for key " + key + " in " + this.keysToIndex.keySet());
            } else {
                array[idx] = this.valueTransformer.applyAsDouble(value.longValue());
            }
        });
        return array;
    }
}