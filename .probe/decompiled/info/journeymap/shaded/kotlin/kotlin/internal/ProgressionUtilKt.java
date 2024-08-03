package info.journeymap.shaded.kotlin.kotlin.internal;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.PublishedApi;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002\u001a \u0010\u0000\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a \u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u0001H\u0001\u001a \u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0005H\u0001\u001a\u0018\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0002\u001a\u0018\u0010\n\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0002¨\u0006\u000b" }, d2 = { "differenceModulo", "", "a", "b", "c", "", "getProgressionLastElement", "start", "end", "step", "mod", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public final class ProgressionUtilKt {

    private static final int mod(int a, int b) {
        int mod = a % b;
        return mod >= 0 ? mod : mod + b;
    }

    private static final long mod(long a, long b) {
        long mod = a % b;
        return mod >= 0L ? mod : mod + b;
    }

    private static final int differenceModulo(int a, int b, int c) {
        return mod(mod(a, c) - mod(b, c), c);
    }

    private static final long differenceModulo(long a, long b, long c) {
        return mod(mod(a, c) - mod(b, c), c);
    }

    @PublishedApi
    public static final int getProgressionLastElement(int start, int end, int step) {
        int var10000;
        if (step > 0) {
            var10000 = start >= end ? end : end - differenceModulo(end, start, step);
        } else {
            if (step >= 0) {
                throw new IllegalArgumentException("Step is zero.");
            }
            var10000 = start <= end ? end : end + differenceModulo(start, end, -step);
        }
        return var10000;
    }

    @PublishedApi
    public static final long getProgressionLastElement(long start, long end, long step) {
        long var10000;
        if (step > 0L) {
            var10000 = start >= end ? end : end - differenceModulo(end, start, step);
        } else {
            if (step >= 0L) {
                throw new IllegalArgumentException("Step is zero.");
            }
            var10000 = start <= end ? end : end + differenceModulo(start, end, -step);
        }
        return var10000;
    }
}