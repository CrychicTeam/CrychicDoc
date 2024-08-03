package me.lucko.spark.common.sampler.java;

import java.lang.management.ThreadInfo;
import java.util.concurrent.ExecutorService;
import me.lucko.spark.common.sampler.ThreadGrouper;
import me.lucko.spark.proto.SparkSamplerProtos;

public class SimpleDataAggregator extends JavaDataAggregator {

    public SimpleDataAggregator(ExecutorService workerPool, ThreadGrouper threadGrouper, int interval, boolean ignoreSleeping, boolean ignoreNative) {
        super(workerPool, threadGrouper, interval, ignoreSleeping, ignoreNative);
    }

    @Override
    public SparkSamplerProtos.SamplerMetadata.DataAggregator getMetadata() {
        return SparkSamplerProtos.SamplerMetadata.DataAggregator.newBuilder().setType(SparkSamplerProtos.SamplerMetadata.DataAggregator.Type.SIMPLE).setThreadGrouper(this.threadGrouper.asProto()).build();
    }

    @Override
    public void insertData(ThreadInfo threadInfo, int window) {
        this.writeData(threadInfo, window);
    }
}