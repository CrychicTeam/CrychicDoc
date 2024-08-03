package me.lucko.spark.common.sampler.node;

import java.util.Objects;
import me.lucko.spark.common.sampler.window.ProtoTimeEncoder;
import me.lucko.spark.common.util.MethodDisambiguator;
import me.lucko.spark.proto.SparkSamplerProtos;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class StackTraceNode extends AbstractNode {

    public static final int NULL_LINE_NUMBER = -1;

    private final StackTraceNode.Description description;

    public StackTraceNode(StackTraceNode.Description description) {
        this.description = description;
    }

    public String getClassName() {
        return this.description.className;
    }

    public String getMethodName() {
        return this.description.methodName;
    }

    public String getMethodDescription() {
        return this.description.methodDescription;
    }

    public int getLineNumber() {
        return this.description.lineNumber;
    }

    public int getParentLineNumber() {
        return this.description.parentLineNumber;
    }

    public SparkSamplerProtos.StackTraceNode toProto(MergeMode mergeMode, ProtoTimeEncoder timeEncoder, Iterable<Integer> childrenRefs) {
        SparkSamplerProtos.StackTraceNode.Builder proto = SparkSamplerProtos.StackTraceNode.newBuilder().setClassName(this.description.className).setMethodName(this.description.methodName);
        double[] times = this.encodeTimesForProto(timeEncoder);
        for (double time : times) {
            proto.addTimes(time);
        }
        if (this.description.lineNumber >= 0) {
            proto.setLineNumber(this.description.lineNumber);
        }
        if (mergeMode.separateParentCalls() && this.description.parentLineNumber >= 0) {
            proto.setParentLineNumber(this.description.parentLineNumber);
        }
        if (this.description.methodDescription != null) {
            proto.setMethodDesc(this.description.methodDescription);
        } else {
            mergeMode.getMethodDisambiguator().disambiguate(this).map(MethodDisambiguator.MethodDescription::getDesc).ifPresent(proto::setMethodDesc);
        }
        proto.addAllChildrenRefs(childrenRefs);
        return proto.build();
    }

    @FunctionalInterface
    public interface Describer<T> {

        StackTraceNode.Description describe(T var1, @Nullable T var2);
    }

    public static final class Description {

        private final String className;

        private final String methodName;

        private final String methodDescription;

        private final int lineNumber;

        private final int parentLineNumber;

        private final int hash;

        public Description(String className, String methodName, int lineNumber, int parentLineNumber) {
            this.className = className;
            this.methodName = methodName;
            this.methodDescription = null;
            this.lineNumber = lineNumber;
            this.parentLineNumber = parentLineNumber;
            this.hash = Objects.hash(new Object[] { this.className, this.methodName, this.lineNumber, this.parentLineNumber });
        }

        public Description(String className, String methodName, String methodDescription) {
            this.className = className;
            this.methodName = methodName;
            this.methodDescription = methodDescription;
            this.lineNumber = -1;
            this.parentLineNumber = -1;
            this.hash = Objects.hash(new Object[] { this.className, this.methodName, this.methodDescription });
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                StackTraceNode.Description description = (StackTraceNode.Description) o;
                return this.hash == description.hash && this.lineNumber == description.lineNumber && this.parentLineNumber == description.parentLineNumber && this.className.equals(description.className) && this.methodName.equals(description.methodName) && Objects.equals(this.methodDescription, description.methodDescription);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return this.hash;
        }
    }
}