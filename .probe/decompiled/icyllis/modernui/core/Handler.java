package icyllis.modernui.core;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.Objects;

public class Handler {

    final MessageQueue mQueue;

    final Handler.Callback mCallback;

    final boolean mAsynchronous;

    public Handler(Looper looper) {
        this(looper, null, false);
    }

    public Handler(Looper looper, Handler.Callback callback) {
        this(looper, callback, false);
    }

    private Handler(Looper looper, Handler.Callback callback, boolean async) {
        this.mQueue = ((Looper) Objects.requireNonNull(looper, "No Looper")).mQueue;
        this.mCallback = callback;
        this.mAsynchronous = async;
    }

    @NonNull
    public static Handler createAsync(Looper looper) {
        return new Handler(looper, null, true);
    }

    @NonNull
    public static Handler createAsync(Looper looper, Handler.Callback callback) {
        return new Handler(looper, callback, true);
    }

    @NonNull
    public String getMessageName(@NonNull Message message) {
        return message.callback != null ? message.callback.getClass().getName() : "0x" + Integer.toHexString(message.what);
    }

    @NonNull
    public final Message obtainMessage() {
        return Message.obtain(this);
    }

    @NonNull
    public final Message obtainMessage(int what) {
        return Message.obtain(this, what);
    }

    @NonNull
    public final Message obtainMessage(int what, @Nullable Object obj) {
        return Message.obtain(this, what, obj);
    }

    @NonNull
    public final Message obtainMessage(int what, int arg1, int arg2) {
        return Message.obtain(this, what, arg1, arg2);
    }

    @NonNull
    public final Message obtainMessage(int what, int arg1, int arg2, @Nullable Object obj) {
        return Message.obtain(this, what, arg1, arg2, obj);
    }

    public final boolean post(@NonNull Runnable r) {
        return this.sendMessageDelayed(getPostMessage(r), 0L);
    }

    public final boolean postAtTime(@NonNull Runnable r, long timeMillis) {
        return this.sendMessageAtTime(getPostMessage(r), timeMillis);
    }

    public final boolean postAtTime(@NonNull Runnable r, @Nullable Object token, long timeMillis) {
        return this.sendMessageAtTime(getPostMessage(r, token), timeMillis);
    }

    public final boolean postDelayed(@NonNull Runnable r, long delayMillis) {
        return this.sendMessageDelayed(getPostMessage(r), delayMillis);
    }

    public final boolean postDelayed(@NonNull Runnable r, @Nullable Object token, long delayMillis) {
        return this.sendMessageDelayed(getPostMessage(r, token), delayMillis);
    }

    public final boolean postAtFrontOfQueue(@NonNull Runnable r) {
        return this.sendMessageAtFrontOfQueue(getPostMessage(r));
    }

    public final void removeCallbacks(@NonNull Runnable r) {
        this.mQueue.removeMessages(this, r, null);
    }

    public final void removeCallbacks(@NonNull Runnable r, @Nullable Object token) {
        this.mQueue.removeMessages(this, r, token);
    }

    public final boolean sendMessage(@NonNull Message msg) {
        return this.sendMessageDelayed(msg, 0L);
    }

    public final boolean sendEmptyMessage(int what) {
        return this.sendEmptyMessageDelayed(what, 0L);
    }

    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        return this.sendMessageDelayed(msg, delayMillis);
    }

    public final boolean sendEmptyMessageAtTime(int what, long timeMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        return this.sendMessageAtTime(msg, timeMillis);
    }

    public final boolean sendMessageDelayed(@NonNull Message msg, long delayMillis) {
        if (delayMillis < 0L) {
            delayMillis = 0L;
        }
        return this.sendMessageAtTime(msg, Core.timeMillis() + delayMillis);
    }

    public final boolean sendMessageAtTime(@NonNull Message msg, long timeMillis) {
        return this.enqueueMessage(msg, timeMillis);
    }

    public final boolean sendMessageAtFrontOfQueue(@NonNull Message msg) {
        return this.enqueueMessage(msg, 0L);
    }

    private boolean enqueueMessage(@NonNull Message msg, long timeMillis) {
        msg.target = this;
        if (this.mAsynchronous) {
            msg.setAsynchronous(true);
        }
        return this.mQueue.enqueueMessage(msg, timeMillis);
    }

    public final void removeMessages(int what) {
        this.mQueue.removeMessages(this, what, null);
    }

    public final void removeMessages(int what, @Nullable Object object) {
        this.mQueue.removeMessages(this, what, object);
    }

    public final void removeCallbacksAndMessages(@Nullable Object token) {
        this.mQueue.removeCallbacksAndMessages(this, token);
    }

    public final boolean hasMessages(int what) {
        return this.mQueue.hasMessages(this, what, null);
    }

    public final boolean hasMessages() {
        return this.mQueue.hasMessages(this);
    }

    public final boolean hasMessages(int what, @Nullable Object object) {
        return this.mQueue.hasMessages(this, what, object);
    }

    public final boolean hasCallbacks(@NonNull Runnable r) {
        return this.mQueue.hasMessages(this, r);
    }

    public void handleMessage(@NonNull Message msg) {
    }

    public void dispatchMessage(@NonNull Message msg) {
        if (msg.callback != null) {
            msg.callback.run();
        } else {
            if (this.mCallback != null && this.mCallback.handleMessage(msg)) {
                return;
            }
            this.handleMessage(msg);
        }
    }

    @NonNull
    public final MessageQueue getQueue() {
        return this.mQueue;
    }

    public final boolean isCurrentThread() {
        Looper looper = Looper.myLooper();
        return looper != null && looper.mQueue == this.mQueue;
    }

    public String toString() {
        return "Handler (" + this.getClass().getName() + ") {" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }

    @NonNull
    private static Message getPostMessage(Runnable r) {
        Message m = Message.obtain();
        m.callback = r;
        return m;
    }

    @NonNull
    private static Message getPostMessage(Runnable r, Object token) {
        Message m = Message.obtain();
        m.obj = token;
        m.callback = r;
        return m;
    }

    @FunctionalInterface
    public interface Callback {

        boolean handleMessage(@NonNull Message var1);
    }
}