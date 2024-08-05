package me.lucko.spark.common.sampler.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.IntPredicate;
import me.lucko.spark.common.sampler.window.ProtoTimeEncoder;

public abstract class AbstractNode {

    protected static final int MAX_STACK_DEPTH = Integer.getInteger("spark.maxStackDepth", 300);

    private final Map<StackTraceNode.Description, StackTraceNode> children = new ConcurrentHashMap();

    private final Map<Integer, LongAdder> times = new ConcurrentHashMap();

    protected LongAdder getTimeAccumulator(int window) {
        LongAdder adder = (LongAdder) this.times.get(window);
        if (adder == null) {
            adder = new LongAdder();
            this.times.put(window, adder);
        }
        return adder;
    }

    public Set<Integer> getTimeWindows() {
        return this.times.keySet();
    }

    public boolean removeTimeWindows(IntPredicate predicate) {
        return this.times.keySet().removeIf(predicate::test);
    }

    protected double[] encodeTimesForProto(ProtoTimeEncoder encoder) {
        return encoder.encode(this.times);
    }

    public Collection<StackTraceNode> getChildren() {
        return this.children.values();
    }

    protected StackTraceNode resolveChild(StackTraceNode.Description description) {
        StackTraceNode result = (StackTraceNode) this.children.get(description);
        return result != null ? result : (StackTraceNode) this.children.computeIfAbsent(description, StackTraceNode::new);
    }

    protected void merge(AbstractNode other) {
        other.times.forEach((key, value) -> this.getTimeAccumulator(key).add(value.longValue()));
        for (Entry<StackTraceNode.Description, StackTraceNode> child : other.children.entrySet()) {
            this.resolveChild((StackTraceNode.Description) child.getKey()).merge((AbstractNode) child.getValue());
        }
    }

    protected List<StackTraceNode> exportChildren(MergeMode mergeMode) {
        if (this.children.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<StackTraceNode> list = new ArrayList(this.children.size());
            label27: for (StackTraceNode child : this.children.values()) {
                for (StackTraceNode other : list) {
                    if (mergeMode.shouldMerge(other, child)) {
                        other.merge(child);
                        continue label27;
                    }
                }
                list.add(child);
            }
            return list;
        }
    }
}