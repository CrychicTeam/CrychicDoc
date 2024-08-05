package icyllis.modernui.core;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class Looper {

    private static final Marker MARKER = MarkerManager.getMarker("Looper");

    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal();

    private static volatile Looper sMainLooper;

    private static volatile Looper.Observer sObserver;

    final MessageQueue mQueue;

    final Thread mThread = Thread.currentThread();

    private boolean mInLoop;

    private long mSlowDispatchThresholdMs;

    private long mSlowDeliveryThresholdMs;

    private boolean mSlowDeliveryDetected;

    @NonNull
    public static Looper prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        } else {
            Looper looper = new Looper(false);
            sThreadLocal.set(looper);
            return looper;
        }
    }

    @Internal
    @MainThread
    public static void prepareMainLooper() {
        Core.checkMainThread();
        if (sMainLooper != null) {
            throw new IllegalStateException();
        } else {
            Looper me = new Looper(true);
            sThreadLocal.set(me);
            sMainLooper = me;
        }
    }

    public static Looper getMainLooper() {
        return sMainLooper;
    }

    public static void setObserver(@Nullable Looper.Observer observer) {
        sObserver = observer;
    }

    private static boolean poll(@NonNull Looper me) {
        Message msg = me.mQueue.next();
        if (msg == null) {
            return false;
        } else {
            Looper.Observer observer = sObserver;
            long slowDispatchThresholdMs = me.mSlowDispatchThresholdMs;
            long slowDeliveryThresholdMs = me.mSlowDeliveryThresholdMs;
            boolean logSlowDelivery = slowDeliveryThresholdMs > 0L && msg.when > 0L;
            boolean logSlowDispatch = slowDispatchThresholdMs > 0L;
            long dispatchStart = !logSlowDelivery && !logSlowDispatch ? 0L : Core.timeMillis();
            Object token = observer == null ? null : observer.messageDispatchStarting();
            long dispatchEnd;
            try {
                msg.target.dispatchMessage(msg);
                if (observer != null) {
                    observer.messageDispatched(token, msg);
                }
                dispatchEnd = logSlowDispatch ? Core.timeMillis() : 0L;
            } catch (Exception var15) {
                if (observer != null) {
                    observer.dispatchingThrewException(token, msg, var15);
                }
                throw var15;
            }
            if (logSlowDelivery) {
                if (me.mSlowDeliveryDetected) {
                    if (dispatchStart - msg.when <= 10L) {
                        ModernUI.LOGGER.warn(MARKER, "Drained");
                        me.mSlowDeliveryDetected = false;
                    }
                } else if (showSlowLog(slowDeliveryThresholdMs, msg.when, dispatchStart, "delivery", msg)) {
                    me.mSlowDeliveryDetected = true;
                }
            }
            if (logSlowDispatch) {
                showSlowLog(slowDispatchThresholdMs, dispatchStart, dispatchEnd, "dispatch", msg);
            }
            msg.recycleUnchecked();
            return true;
        }
    }

    public static void loop() {
        Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        } else {
            if (me.mInLoop) {
                ModernUI.LOGGER.warn(MARKER, "Loop again would have the queued messages be executed before this one completed.");
            }
            me.mInLoop = true;
            me.mSlowDeliveryDetected = false;
            while (poll(me)) {
            }
        }
    }

    private static boolean showSlowLog(long threshold, long measureStart, long measureEnd, String what, Message msg) {
        long actualTime = measureEnd - measureStart;
        if (actualTime < threshold) {
            return false;
        } else {
            ModernUI.LOGGER.warn(MARKER, "Slow {} took {}ms {} h={} c={} m={}", what, actualTime, Thread.currentThread().getName(), msg.target.getClass().getName(), msg.callback, msg.what);
            return true;
        }
    }

    @Nullable
    public static Looper myLooper() {
        return (Looper) sThreadLocal.get();
    }

    @NonNull
    public static MessageQueue myQueue() {
        return ((Looper) sThreadLocal.get()).mQueue;
    }

    private Looper(boolean main) {
        this.mQueue = new MessageQueue(main ? null : this.mThread);
    }

    public boolean isCurrentThread() {
        return Thread.currentThread() == this.mThread;
    }

    public void setSlowLogThresholdMs(long slowDispatchThresholdMs, long slowDeliveryThresholdMs) {
        this.mSlowDispatchThresholdMs = slowDispatchThresholdMs;
        this.mSlowDeliveryThresholdMs = slowDeliveryThresholdMs;
    }

    public void quit() {
        this.mQueue.quit(false);
    }

    public void quitSafely() {
        this.mQueue.quit(true);
    }

    @NonNull
    public Thread getThread() {
        return this.mThread;
    }

    @NonNull
    public MessageQueue getQueue() {
        return this.mQueue;
    }

    @NonNull
    public String toString() {
        return "Looper (" + this.mThread.getName() + ", tid " + this.mThread.getId() + ") {" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }

    public interface Observer {

        Object messageDispatchStarting();

        void messageDispatched(Object var1, Message var2);

        void dispatchingThrewException(Object var1, Message var2, Exception var3);
    }
}