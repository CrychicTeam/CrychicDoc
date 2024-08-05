package me.lucko.spark.common.sampler.async;

import me.lucko.spark.common.sampler.ThreadGrouper;
import me.lucko.spark.common.sampler.aggregator.AbstractDataAggregator;
import me.lucko.spark.common.sampler.node.StackTraceNode;
import me.lucko.spark.common.sampler.node.ThreadNode;
import me.lucko.spark.proto.SparkSamplerProtos;

public class AsyncDataAggregator extends AbstractDataAggregator {

    private static final StackTraceNode.Describer<AsyncStackTraceElement> STACK_TRACE_DESCRIBER = (element, parent) -> new StackTraceNode.Description(element.getClassName(), element.getMethodName(), element.getMethodDescription());

    protected AsyncDataAggregator(ThreadGrouper threadGrouper) {
        super(threadGrouper);
    }

    @Override
    public SparkSamplerProtos.SamplerMetadata.DataAggregator getMetadata() {
        return SparkSamplerProtos.SamplerMetadata.DataAggregator.newBuilder().setType(SparkSamplerProtos.SamplerMetadata.DataAggregator.Type.SIMPLE).setThreadGrouper(this.threadGrouper.asProto()).build();
    }

    public void insertData(ProfileSegment element, int window) {
        try {
            ThreadNode node = this.getNode(this.threadGrouper.getGroup((long) element.getNativeThreadId(), element.getThreadName()));
            node.log(STACK_TRACE_DESCRIBER, element.getStackTrace(), element.getValue(), window);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}