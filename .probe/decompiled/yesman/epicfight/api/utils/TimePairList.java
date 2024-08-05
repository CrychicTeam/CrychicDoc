package yesman.epicfight.api.utils;

public class TimePairList {

    private final TimePairList.TimePair[] timePairs;

    private TimePairList(TimePairList.TimePair[] timePairs) {
        this.timePairs = timePairs;
    }

    public boolean isTimeInPairs(float time) {
        for (TimePairList.TimePair timePair : this.timePairs) {
            if (timePair.isTimeIn(time)) {
                return true;
            }
        }
        return false;
    }

    public static TimePairList create(float... times) {
        if (times.length % 2 != 0) {
            throw new IllegalArgumentException("Time pair exception : number of given times is not an even number");
        } else {
            TimePairList.TimePair[] timePairs = new TimePairList.TimePair[times.length / 2];
            for (int i = 0; i < times.length / 2; i++) {
                timePairs[i] = new TimePairList.TimePair(times[i * 2], times[i * 2 + 1]);
            }
            return new TimePairList(timePairs);
        }
    }

    private static class TimePair {

        public final float begin;

        public final float end;

        private TimePair(float begin, float end) {
            this.begin = begin;
            this.end = end;
        }

        private boolean isTimeIn(float time) {
            return time >= this.begin && time < this.end;
        }
    }
}