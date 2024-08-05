package me.lucko.spark.common.sampler.aggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntPredicate;
import me.lucko.spark.common.sampler.ThreadGrouper;
import me.lucko.spark.common.sampler.node.ThreadNode;

public abstract class AbstractDataAggregator implements DataAggregator {

    protected final Map<String, ThreadNode> threadData = new ConcurrentHashMap();

    protected final ThreadGrouper threadGrouper;

    protected AbstractDataAggregator(ThreadGrouper threadGrouper) {
        this.threadGrouper = threadGrouper;
    }

    protected ThreadNode getNode(String group) {
        ThreadNode node = (ThreadNode) this.threadData.get(group);
        return node != null ? node : (ThreadNode) this.threadData.computeIfAbsent(group, ThreadNode::new);
    }

    @Override
    public void pruneData(IntPredicate timeWindowPredicate) {
        this.threadData.values().removeIf(node -> node.removeTimeWindowsRecursively(timeWindowPredicate));
    }

    @Override
    public List<ThreadNode> exportData() {
        List<ThreadNode> data = new ArrayList(this.threadData.values());
        for (ThreadNode node : data) {
            node.setThreadLabel(this.threadGrouper.getLabel(node.getThreadGroup()));
        }
        return data;
    }
}