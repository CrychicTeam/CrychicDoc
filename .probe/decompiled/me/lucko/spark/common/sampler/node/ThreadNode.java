package me.lucko.spark.common.sampler.node;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.IntPredicate;
import me.lucko.spark.common.sampler.window.ProtoTimeEncoder;
import me.lucko.spark.common.util.IndexedListBuilder;
import me.lucko.spark.proto.SparkSamplerProtos;

public final class ThreadNode extends AbstractNode {

    private final String name;

    public String label;

    public ThreadNode(String name) {
        this.name = name;
    }

    public String getThreadLabel() {
        return this.label != null ? this.label : this.name;
    }

    public String getThreadGroup() {
        return this.name;
    }

    public void setThreadLabel(String label) {
        this.label = label;
    }

    public <T> void log(StackTraceNode.Describer<T> describer, T[] stack, long time, int window) {
        if (stack.length != 0) {
            this.getTimeAccumulator(window).add(time);
            AbstractNode node = this;
            T previousElement = null;
            for (int offset = 0; offset < Math.min(MAX_STACK_DEPTH, stack.length); offset++) {
                T element = stack[stack.length - 1 - offset];
                node = node.resolveChild(describer.describe(element, previousElement));
                node.getTimeAccumulator(window).add(time);
                previousElement = element;
            }
        }
    }

    public boolean removeTimeWindowsRecursively(IntPredicate predicate) {
        Queue<AbstractNode> queue = new ArrayDeque();
        queue.add(this);
        while (!queue.isEmpty()) {
            AbstractNode node = (AbstractNode) queue.remove();
            Collection<StackTraceNode> children = node.getChildren();
            boolean needToProcessChildren = false;
            Iterator<StackTraceNode> it = children.iterator();
            while (it.hasNext()) {
                StackTraceNode child = (StackTraceNode) it.next();
                boolean windowsWereRemoved = child.removeTimeWindows(predicate);
                boolean childIsNowEmpty = child.getTimeWindows().isEmpty();
                if (childIsNowEmpty) {
                    it.remove();
                } else if (windowsWereRemoved) {
                    needToProcessChildren = true;
                }
            }
            if (needToProcessChildren) {
                queue.addAll(children);
            }
        }
        this.removeTimeWindows(predicate);
        return this.getTimeWindows().isEmpty();
    }

    public SparkSamplerProtos.ThreadNode toProto(MergeMode mergeMode, ProtoTimeEncoder timeEncoder) {
        SparkSamplerProtos.ThreadNode.Builder proto = SparkSamplerProtos.ThreadNode.newBuilder().setName(this.getThreadLabel());
        double[] times = this.encodeTimesForProto(timeEncoder);
        for (double time : times) {
            proto.addTimes(time);
        }
        IndexedListBuilder<SparkSamplerProtos.StackTraceNode> nodesArray = new IndexedListBuilder<>();
        Deque<ThreadNode.Node> stack = new ArrayDeque();
        List<Integer> childrenRefs = new LinkedList();
        for (StackTraceNode child : this.exportChildren(mergeMode)) {
            stack.push(new ThreadNode.Node(child, childrenRefs));
        }
        while (!stack.isEmpty()) {
            ThreadNode.Node node = (ThreadNode.Node) stack.peek();
            if (node.firstVisit) {
                for (StackTraceNode child : node.stackTraceNode.exportChildren(mergeMode)) {
                    stack.push(new ThreadNode.Node(child, node.childrenRefs));
                }
                node.firstVisit = false;
            } else {
                SparkSamplerProtos.StackTraceNode childProto = node.stackTraceNode.toProto(mergeMode, timeEncoder, node.childrenRefs);
                int childIndex = nodesArray.add(childProto);
                node.parentChildrenRefs.add(childIndex);
                stack.pop();
            }
        }
        proto.addAllChildrenRefs(childrenRefs);
        proto.addAllChildren(nodesArray.build());
        return proto.build();
    }

    private static final class Node {

        private final StackTraceNode stackTraceNode;

        private boolean firstVisit = true;

        private final List<Integer> childrenRefs = new LinkedList();

        private final List<Integer> parentChildrenRefs;

        private Node(StackTraceNode node, List<Integer> parentChildrenRefs) {
            this.stackTraceNode = node;
            this.parentChildrenRefs = parentChildrenRefs;
        }
    }
}