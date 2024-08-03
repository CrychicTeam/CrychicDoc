package me.lucko.spark.common.sampler.aggregator;

import java.util.List;
import java.util.function.IntPredicate;
import me.lucko.spark.common.sampler.node.ThreadNode;
import me.lucko.spark.proto.SparkSamplerProtos;

public interface DataAggregator {

    List<ThreadNode> exportData();

    void pruneData(IntPredicate var1);

    SparkSamplerProtos.SamplerMetadata.DataAggregator getMetadata();
}