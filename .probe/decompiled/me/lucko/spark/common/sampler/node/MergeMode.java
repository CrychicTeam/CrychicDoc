package me.lucko.spark.common.sampler.node;

import java.util.Objects;
import me.lucko.spark.common.util.MethodDisambiguator;

public final class MergeMode {

    private final MethodDisambiguator methodDisambiguator;

    private final boolean separateParentCalls;

    public static MergeMode sameMethod(MethodDisambiguator methodDisambiguator) {
        return new MergeMode(methodDisambiguator, false);
    }

    public static MergeMode separateParentCalls(MethodDisambiguator methodDisambiguator) {
        return new MergeMode(methodDisambiguator, true);
    }

    MergeMode(MethodDisambiguator methodDisambiguator, boolean separateParentCalls) {
        this.methodDisambiguator = methodDisambiguator;
        this.separateParentCalls = separateParentCalls;
    }

    public MethodDisambiguator getMethodDisambiguator() {
        return this.methodDisambiguator;
    }

    public boolean separateParentCalls() {
        return this.separateParentCalls;
    }

    public boolean shouldMerge(StackTraceNode n1, StackTraceNode n2) {
        if (!n1.getClassName().equals(n2.getClassName())) {
            return false;
        } else if (!n1.getMethodName().equals(n2.getMethodName())) {
            return false;
        } else if (this.separateParentCalls && n1.getParentLineNumber() != n2.getParentLineNumber()) {
            return false;
        } else {
            String desc1 = (String) this.methodDisambiguator.disambiguate(n1).map(MethodDisambiguator.MethodDescription::getDesc).orElse(null);
            String desc2 = (String) this.methodDisambiguator.disambiguate(n2).map(MethodDisambiguator.MethodDescription::getDesc).orElse(null);
            return desc1 == null && desc2 == null ? true : Objects.equals(desc1, desc2);
        }
    }
}