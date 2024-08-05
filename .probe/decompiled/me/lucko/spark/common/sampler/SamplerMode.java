package me.lucko.spark.common.sampler;

import java.util.function.LongToDoubleFunction;
import me.lucko.spark.proto.SparkSamplerProtos;

public enum SamplerMode {

    EXECUTION(value -> (double) value / 1000.0, 4, SparkSamplerProtos.SamplerMetadata.SamplerMode.EXECUTION), ALLOCATION(value -> (double) value, 524287, SparkSamplerProtos.SamplerMetadata.SamplerMode.ALLOCATION);

    private final LongToDoubleFunction valueTransformer;

    private final int defaultInterval;

    private final SparkSamplerProtos.SamplerMetadata.SamplerMode proto;

    private SamplerMode(LongToDoubleFunction valueTransformer, int defaultInterval, SparkSamplerProtos.SamplerMetadata.SamplerMode proto) {
        this.valueTransformer = valueTransformer;
        this.defaultInterval = defaultInterval;
        this.proto = proto;
    }

    public LongToDoubleFunction valueTransformer() {
        return this.valueTransformer;
    }

    public int defaultInterval() {
        return this.defaultInterval;
    }

    public SparkSamplerProtos.SamplerMetadata.SamplerMode asProto() {
        return this.proto;
    }
}