package icyllis.arc3d.core;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.Comparator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class RefCnt implements RefCounted {

    private static final VarHandle REF_CNT;

    private static final ConcurrentMap<RefCnt, Boolean> TRACKER;

    private volatile int mRefCnt = 1;

    public RefCnt() {
        assert TRACKER.put(this, Boolean.TRUE) == null;
    }

    @SharedPtr
    public static <T extends RefCounted> T move(@SharedPtr T sp) {
        if (sp != null) {
            sp.unref();
        }
        return null;
    }

    @SharedPtr
    public static <T extends RefCounted> T move(@SharedPtr T sp, @SharedPtr T that) {
        if (sp != null) {
            sp.unref();
        }
        return that;
    }

    @SharedPtr
    public static <T extends RefCounted> T create(@SharedPtr T that) {
        if (that != null) {
            that.ref();
        }
        return that;
    }

    @SharedPtr
    public static <T extends RefCounted> T create(@SharedPtr T sp, @SharedPtr T that) {
        if (sp != null) {
            sp.unref();
        }
        if (that != null) {
            that.ref();
        }
        return that;
    }

    public final boolean unique() {
        return (int) REF_CNT.getAcquire(this) == 1;
    }

    @Override
    public final void ref() {
        int refCnt = (int) REF_CNT.getAndAddAcquire(this, 1);
        assert refCnt > 0 : "Reference count has reached zero " + this;
    }

    @Override
    public final void unref() {
        int refCnt = (int) REF_CNT.getAndAdd(this, -1);
        assert refCnt > 0 : "Reference count has reached zero " + this;
        if (refCnt == 1) {
            this.deallocate();
            assert TRACKER.remove(this) == Boolean.TRUE;
        }
    }

    public final int getRefCnt() {
        return (int) REF_CNT.getOpaque(this);
    }

    public final int getRefCntAcquire() {
        return (int) REF_CNT.getAcquire(this);
    }

    public final int getRefCntVolatile() {
        return this.mRefCnt;
    }

    protected abstract void deallocate();

    static {
        Lookup lookup = MethodHandles.lookup();
        try {
            REF_CNT = lookup.findVarHandle(RefCnt.class, "mRefCnt", int.class);
        } catch (IllegalAccessException | NoSuchFieldException var3) {
            throw new RuntimeException(var3);
        }
        TRACKER = new ConcurrentSkipListMap(Comparator.comparingInt(System::identityHashCode));
        try {
            assert false;
        } catch (AssertionError var2) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                TRACKER.forEach((o, __) -> System.err.printf("RefCnt %d: %s\n", o.getRefCntVolatile(), o));
                assert TRACKER.isEmpty() : "Memory leaks in reference-counted objects";
            }, "RefCnt-Tracker"));
        }
    }
}