package io.redspace.ironsspellbooks.datafix;

import java.util.List;

public class ParallelMatcher implements AutoCloseable {

    private int[] matcherPositions;

    private final List<byte[]> matchTargets;

    public ParallelMatcher(List<byte[]> matchTargets) {
        this.matchTargets = matchTargets;
        this.matcherPositions = new int[matchTargets.size()];
    }

    private void reset() {
        this.matcherPositions = new int[this.matcherPositions.length];
    }

    public boolean pushValue(int nextToken) {
        for (int matcherIndex = 0; matcherIndex < this.matchTargets.size(); matcherIndex++) {
            byte[] matchTarget = (byte[]) this.matchTargets.get(matcherIndex);
            if (matchTarget[this.matcherPositions[matcherIndex]] == nextToken) {
                this.matcherPositions[matcherIndex]++;
                if (this.matcherPositions[matcherIndex] == matchTarget.length) {
                    this.reset();
                    return true;
                }
            } else {
                this.matcherPositions[matcherIndex] = 0;
            }
        }
        return false;
    }

    public void close() throws Exception {
        this.reset();
    }
}