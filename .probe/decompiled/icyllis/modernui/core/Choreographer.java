package icyllis.modernui.core;

import icyllis.modernui.animation.AnimationUtils;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.Objects;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class Choreographer {

    private static final Marker MARKER = MarkerManager.getMarker("Choreographer");

    private static final boolean DEBUG_FRAMES = false;

    private static volatile long sFrameDelay = 1L;

    private static final ThreadLocal<Choreographer> sThreadInstance = ThreadLocal.withInitial(() -> {
        if (Core.isOnRenderThread()) {
            throw new IllegalStateException("The render thread cannot have a choreographer!");
        } else {
            Looper looper = Looper.myLooper();
            if (looper == null) {
                throw new IllegalStateException("The current thread must have a looper!");
            } else {
                return new Choreographer(looper);
            }
        }
    });

    private static final int MSG_DO_FRAME = 0;

    private static final int MSG_DO_SCHEDULE_CALLBACK = 1;

    private static final Object FRAME_CALLBACK_TOKEN = new Object() {

        @NonNull
        public String toString() {
            return "FRAME_CALLBACK_TOKEN";
        }
    };

    @Internal
    public static final int CALLBACK_INPUT = 0;

    @Internal
    public static final int CALLBACK_ANIMATION = 1;

    @Internal
    public static final int CALLBACK_TRAVERSAL = 2;

    @Internal
    public static final int CALLBACK_COMMIT = 3;

    private static final int CALLBACK_LAST = 3;

    private final Object mLock = new Object();

    private final Handler mHandler;

    private Choreographer.CallbackRecord mCallbackPool;

    private final Choreographer.CallbackQueue[] mCallbackQueues;

    private boolean mFrameScheduled;

    private boolean mCallbacksRunning;

    private long mLastFrameTimeNanos;

    private Choreographer(@NonNull Looper looper) {
        this.mHandler = new Handler(looper, this::handleMessage);
        this.mLastFrameTimeNanos = Long.MIN_VALUE;
        this.mCallbackQueues = new Choreographer.CallbackQueue[4];
        for (int i = 0; i <= 3; i++) {
            this.mCallbackQueues[i] = new Choreographer.CallbackQueue();
        }
    }

    @NonNull
    public static Choreographer getInstance() {
        return (Choreographer) sThreadInstance.get();
    }

    @Internal
    public static long getFrameDelay() {
        return sFrameDelay;
    }

    @Internal
    public static void setFrameDelay(long frameDelay) {
        sFrameDelay = frameDelay;
    }

    @Internal
    public static long subtractFrameDelay(long delayMillis) {
        long frameDelay = sFrameDelay;
        return delayMillis <= frameDelay ? 0L : delayMillis - frameDelay;
    }

    @Internal
    public void postCallback(int callbackType, @NonNull Runnable action, @Nullable Object token) {
        this.postCallbackDelayed(callbackType, action, token, 0L);
    }

    @Internal
    public void postCallbackDelayed(int callbackType, @NonNull Runnable action, @Nullable Object token, long delayMillis) {
        Objects.requireNonNull(action);
        if (callbackType >= 0 && callbackType <= 3) {
            this.postCallbackDelayedInternal(callbackType, action, token, delayMillis);
        } else {
            throw new AssertionError(callbackType);
        }
    }

    private void postCallbackDelayedInternal(int callbackType, @NonNull Object action, @Nullable Object token, long delayMillis) {
        synchronized (this.mLock) {
            long now = Core.timeMillis();
            long dueTime = now + delayMillis;
            this.mCallbackQueues[callbackType].addCallbackLocked(dueTime, action, token);
            if (dueTime <= now) {
                this.scheduleFrameLocked(now);
            } else {
                Message msg = this.mHandler.obtainMessage(1, action);
                msg.arg1 = callbackType;
                msg.setAsynchronous(true);
                this.mHandler.sendMessageAtTime(msg, dueTime);
            }
        }
    }

    @Internal
    public void removeCallbacks(int callbackType, @Nullable Runnable action, @Nullable Object token) {
        if (callbackType >= 0 && callbackType <= 3) {
            this.removeCallbacksInternal(callbackType, action, token);
        } else {
            throw new AssertionError(callbackType);
        }
    }

    private void removeCallbacksInternal(int callbackType, @Nullable Object action, @Nullable Object token) {
        synchronized (this.mLock) {
            this.mCallbackQueues[callbackType].removeCallbacksLocked(action, token);
            if (action != null && token == null) {
                this.mHandler.removeMessages(1, action);
            }
        }
    }

    public void postFrameCallback(@NonNull Choreographer.FrameCallback callback) {
        this.postFrameCallbackDelayed(callback, 0L);
    }

    public void postFrameCallbackDelayed(@NonNull Choreographer.FrameCallback callback, long delayMillis) {
        Objects.requireNonNull(callback);
        this.postCallbackDelayedInternal(1, callback, FRAME_CALLBACK_TOKEN, delayMillis);
    }

    public void removeFrameCallback(@NonNull Choreographer.FrameCallback callback) {
        Objects.requireNonNull(callback);
        this.removeCallbacksInternal(1, callback, FRAME_CALLBACK_TOKEN);
    }

    @Internal
    public long getFrameTime() {
        return this.getFrameTimeNanos() / 1000000L;
    }

    @Internal
    public long getFrameTimeNanos() {
        synchronized (this.mLock) {
            if (!this.mCallbacksRunning) {
                throw new IllegalStateException("This method must only be called as part of a callback while a frame is in progress.");
            } else {
                return this.mLastFrameTimeNanos;
            }
        }
    }

    @Internal
    public long getLastFrameTimeNanos() {
        synchronized (this.mLock) {
            return this.mLastFrameTimeNanos;
        }
    }

    private void scheduleFrameLocked(long now) {
        if (!this.mFrameScheduled) {
            this.mFrameScheduled = true;
            long nextFrameTime = Math.max(this.mLastFrameTimeNanos / 1000000L + sFrameDelay, now);
            Message msg = this.mHandler.obtainMessage(0);
            msg.setAsynchronous(true);
            this.mHandler.sendMessageAtTime(msg, nextFrameTime);
        }
    }

    void doFrame() {
        long frameTimeNanos = Core.timeNanos();
        try {
            synchronized (this.mLock) {
                if (!this.mFrameScheduled) {
                    return;
                }
                if (frameTimeNanos < this.mLastFrameTimeNanos) {
                    return;
                }
                this.mFrameScheduled = false;
                this.mLastFrameTimeNanos = frameTimeNanos;
            }
            AnimationUtils.lockAnimationClock(frameTimeNanos / 1000000L);
            this.doCallbacks(0, frameTimeNanos);
            this.doCallbacks(1, frameTimeNanos);
            this.doCallbacks(2, frameTimeNanos);
            this.doCallbacks(3, frameTimeNanos);
        } finally {
            AnimationUtils.unlockAnimationClock();
        }
    }

    void doCallbacks(int callbackType, long frameTimeNanos) {
        Choreographer.CallbackRecord callbacks;
        synchronized (this.mLock) {
            long now = Core.timeMillis();
            callbacks = this.mCallbackQueues[callbackType].extractDueCallbacksLocked(now);
            if (callbacks == null) {
                return;
            }
            this.mCallbacksRunning = true;
        }
        try {
            for (Choreographer.CallbackRecord c = callbacks; c != null; c = c.next) {
                c.run(frameTimeNanos);
            }
        } finally {
            synchronized (this.mLock) {
                this.mCallbacksRunning = false;
                Choreographer.CallbackRecord next;
                do {
                    next = callbacks.next;
                    this.recycleCallbackLocked(callbacks);
                    callbacks = next;
                } while (next != null);
            }
        }
    }

    void doScheduleCallback(int callbackType) {
        synchronized (this.mLock) {
            if (!this.mFrameScheduled) {
                long now = Core.timeMillis();
                if (this.mCallbackQueues[callbackType].hasDueCallbacksLocked(now)) {
                    this.scheduleFrameLocked(now);
                }
            }
        }
    }

    @NonNull
    private Choreographer.CallbackRecord obtainCallbackLocked(long dueTime, @NonNull Object action, @Nullable Object token) {
        Choreographer.CallbackRecord callback = this.mCallbackPool;
        if (callback == null) {
            callback = new Choreographer.CallbackRecord();
        } else {
            this.mCallbackPool = callback.next;
            callback.next = null;
        }
        callback.dueTime = dueTime;
        callback.action = action;
        callback.token = token;
        return callback;
    }

    private void recycleCallbackLocked(@NonNull Choreographer.CallbackRecord callback) {
        callback.action = null;
        callback.token = null;
        callback.next = this.mCallbackPool;
        this.mCallbackPool = callback;
    }

    private boolean handleMessage(@NonNull Message msg) {
        switch(msg.what) {
            case 0:
                this.doFrame();
                break;
            case 1:
                this.doScheduleCallback(msg.arg1);
        }
        return true;
    }

    private final class CallbackQueue {

        @Nullable
        private Choreographer.CallbackRecord mHead;

        public boolean hasDueCallbacksLocked(long now) {
            return this.mHead != null && this.mHead.dueTime <= now;
        }

        @Nullable
        public Choreographer.CallbackRecord extractDueCallbacksLocked(long now) {
            Choreographer.CallbackRecord callbacks = this.mHead;
            if (callbacks != null && callbacks.dueTime <= now) {
                Choreographer.CallbackRecord last = callbacks;
                Choreographer.CallbackRecord next;
                for (next = callbacks.next; next != null; next = next.next) {
                    if (next.dueTime > now) {
                        last.next = null;
                        break;
                    }
                    last = next;
                }
                this.mHead = next;
                return callbacks;
            } else {
                return null;
            }
        }

        public void addCallbackLocked(long dueTime, @NonNull Object action, @Nullable Object token) {
            Choreographer.CallbackRecord callback = Choreographer.this.obtainCallbackLocked(dueTime, action, token);
            Choreographer.CallbackRecord entry = this.mHead;
            if (entry == null) {
                this.mHead = callback;
            } else if (dueTime < entry.dueTime) {
                callback.next = entry;
                this.mHead = callback;
            } else {
                while (entry.next != null) {
                    if (dueTime < entry.next.dueTime) {
                        callback.next = entry.next;
                        break;
                    }
                    entry = entry.next;
                }
                entry.next = callback;
            }
        }

        public void removeCallbacksLocked(@Nullable Object action, @Nullable Object token) {
            Choreographer.CallbackRecord predecessor = null;
            Choreographer.CallbackRecord callback = this.mHead;
            while (callback != null) {
                Choreographer.CallbackRecord next = callback.next;
                if ((action == null || callback.action == action) && (token == null || callback.token == token)) {
                    if (predecessor != null) {
                        predecessor.next = next;
                    } else {
                        this.mHead = next;
                    }
                    Choreographer.this.recycleCallbackLocked(callback);
                } else {
                    predecessor = callback;
                }
                callback = next;
            }
        }
    }

    private final class CallbackRecord {

        public Choreographer.CallbackRecord next;

        public long dueTime;

        public Object action;

        public Object token;

        public void run(long frameTimeNanos) {
            if (this.token == Choreographer.FRAME_CALLBACK_TOKEN) {
                ((Choreographer.FrameCallback) this.action).doFrame(Choreographer.this, frameTimeNanos);
            } else {
                ((Runnable) this.action).run();
            }
        }
    }

    @FunctionalInterface
    public interface FrameCallback {

        void doFrame(@NonNull Choreographer var1, long var2);
    }
}