package icyllis.modernui.util;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.MathUtil;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Objects;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class MpmcArrayQueue<E> extends L2Padding<E> implements Pools.Pool<E> {

    private static final VarHandle HEAD;

    private static final VarHandle TAIL;

    private static final VarHandle SEQ;

    private static final long MAX_SEQ = 4611686018427387904L;

    private final long mask;

    private final E[] buf;

    private final long[] seq;

    public MpmcArrayQueue(int capacity) {
        if (capacity > 0 && capacity <= 1073741824) {
            int n = MathUtil.ceilPow2(capacity);
            this.mask = (long) (n - 1);
            this.buf = (E[]) (new Object[n]);
            this.seq = new long[n];
            for (int i = 0; i < n; i++) {
                SEQ.setVolatile(this.seq, i, (long) i);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int size() {
        long after = (long) HEAD.getVolatile(this);
        long before;
        long currentTail;
        do {
            before = after;
            currentTail = (long) TAIL.getVolatile(this);
            after = (long) HEAD.getVolatile(this);
        } while (before != after);
        long size = safeDiff(currentTail, after);
        return (int) MathUtil.clamp(size, 0L, 2147483648L);
    }

    public boolean isEmpty() {
        long after = (long) HEAD.getVolatile(this);
        long before;
        long currentTail;
        do {
            before = after;
            currentTail = (long) TAIL.getVolatile(this);
            after = (long) HEAD.getVolatile(this);
        } while (before != after);
        return currentTail == after;
    }

    public boolean isFull() {
        long after = (long) HEAD.getVolatile(this);
        long before;
        long currentTail;
        do {
            before = after;
            currentTail = (long) TAIL.getVolatile(this);
            after = (long) HEAD.getVolatile(this);
        } while (before != after);
        return currentTail == safeNext(after + this.mask);
    }

    @Nullable
    @Override
    public E acquire() {
        long mask = this.mask;
        long[] seq = this.seq;
        long curHead = (long) HEAD.getVolatile(this);
        while (true) {
            int curPos = (int) (curHead & mask);
            long curSeq = (long) SEQ.getVolatile(seq, curPos);
            long diff = safeDiff(curSeq, curHead);
            if (diff <= 0L) {
                long curTail = (long) TAIL.getVolatile(this);
                if (safeDiff(curHead, curTail) >= 0L) {
                    return null;
                }
            } else if (diff == 1L) {
                if (HEAD.compareAndSet(this, curHead, safeNext(curHead))) {
                    E instance = this.buf[curPos];
                    this.buf[curPos] = null;
                    SEQ.setRelease(seq, curPos, safeNext(curHead + mask));
                    return instance;
                }
                curHead = safeNext(curHead);
            } else {
                curHead = (long) HEAD.getVolatile(this);
            }
        }
    }

    @Override
    public boolean release(@NonNull E instance) {
        Objects.requireNonNull(instance);
        long mask = this.mask;
        long[] seq = this.seq;
        long curTail = (long) TAIL.getVolatile(this);
        while (true) {
            int curPos = (int) (curTail & mask);
            long curSeq = (long) SEQ.getVolatile(seq, curPos);
            long diff = safeDiff(curSeq, curTail);
            if (diff < 0L) {
                long curHead = (long) HEAD.getVolatile(this);
                if (safeDiff(curTail, safeNext(curHead + mask)) >= 0L) {
                    return false;
                }
            } else if (diff == 0L) {
                if (TAIL.compareAndSet(this, curTail, safeNext(curTail))) {
                    this.buf[curPos] = instance;
                    SEQ.setRelease(seq, curPos, safeNext(curTail));
                    return true;
                }
                curTail = safeNext(curTail);
            } else {
                curTail = (long) TAIL.getVolatile(this);
            }
        }
    }

    private static long safeNext(long v) {
        return v + 1L & 4611686018427387903L;
    }

    private static long safeDiff(long a, long b) {
        long d = a - b;
        if (d >= 2305843009213693952L) {
            return d - 4611686018427387904L;
        } else {
            return d <= -2305843009213693952L ? d + 4611686018427387904L : d;
        }
    }

    static {
        Lookup lookup = MethodHandles.lookup();
        try {
            HEAD = lookup.findVarHandle(HeadPadding.class, "head", long.class);
            TAIL = lookup.findVarHandle(TailPadding.class, "tail", long.class);
            SEQ = MethodHandles.arrayElementVarHandle(long[].class);
        } catch (IllegalAccessException | NoSuchFieldException var2) {
            throw new RuntimeException(var2);
        }
    }
}