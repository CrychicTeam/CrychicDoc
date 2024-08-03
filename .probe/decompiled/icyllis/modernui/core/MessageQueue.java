package icyllis.modernui.core;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.locks.LockSupport;
import javax.annotation.concurrent.GuardedBy;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.glfw.GLFW;

public final class MessageQueue {

    private static final Marker MARKER = MarkerManager.getMarker("MessageQueue");

    private static final boolean DEBUG = false;

    private final Thread mThread;

    @GuardedBy("this")
    Message mMessages;

    @GuardedBy("this")
    private final ArrayList<MessageQueue.IdleHandler> mIdleHandlers = new ArrayList();

    private MessageQueue.IdleHandler[] mPendingIdleHandlers;

    @GuardedBy("this")
    private boolean mQuitting;

    private volatile boolean mPolling;

    @GuardedBy("this")
    private boolean mBlocked;

    private boolean mDisposed;

    @GuardedBy("this")
    private int mNextBarrierToken;

    MessageQueue(Thread thread) {
        this.mThread = thread;
    }

    public boolean isIdle() {
        synchronized (this) {
            long now = Core.timeMillis();
            return this.mMessages == null || now < this.mMessages.when;
        }
    }

    public void addIdleHandler(@NonNull MessageQueue.IdleHandler handler) {
        synchronized (this) {
            this.mIdleHandlers.add(handler);
        }
    }

    public void removeIdleHandler(@NonNull MessageQueue.IdleHandler handler) {
        synchronized (this) {
            this.mIdleHandlers.remove(handler);
        }
    }

    public boolean isPolling() {
        synchronized (this) {
            return !this.mQuitting && this.mPolling;
        }
    }

    @Nullable
    Message next() {
        if (this.mDisposed) {
            return null;
        } else {
            int pendingIdleHandlerCount = -1;
            int nextPollTimeoutMillis = 0;
            while (true) {
                while (true) {
                    if (this.mThread == null) {
                        this.mPolling = true;
                        if (nextPollTimeoutMillis < 0) {
                            GLFW.glfwWaitEvents();
                        } else if (nextPollTimeoutMillis == 0) {
                            GLFW.glfwPollEvents();
                        } else {
                            GLFW.glfwWaitEventsTimeout((double) nextPollTimeoutMillis / 1000.0);
                        }
                        this.mPolling = false;
                    } else {
                        this.mPolling = true;
                        if (nextPollTimeoutMillis < 0) {
                            LockSupport.park();
                        } else if (nextPollTimeoutMillis > 0) {
                            LockSupport.parkNanos((long) nextPollTimeoutMillis * 1000000L);
                        }
                        this.mPolling = false;
                    }
                    synchronized (this) {
                        long now = Core.timeMillis();
                        Message prevMsg = null;
                        Message msg = this.mMessages;
                        if (msg != null && msg.target == null) {
                            do {
                                prevMsg = msg;
                                msg = msg.next;
                            } while (msg != null && !msg.isAsynchronous());
                        }
                        if (msg != null) {
                            if (now >= msg.when) {
                                this.mBlocked = false;
                                if (prevMsg != null) {
                                    prevMsg.next = msg.next;
                                } else {
                                    this.mMessages = msg.next;
                                }
                                msg.next = null;
                                msg.markInUse();
                                return msg;
                            }
                            nextPollTimeoutMillis = (int) Math.min(msg.when - now, 2147483647L);
                        } else {
                            nextPollTimeoutMillis = -1;
                        }
                        if (this.mQuitting) {
                            this.mDisposed = true;
                            return null;
                        }
                        if (pendingIdleHandlerCount < 0 && (this.mMessages == null || now < this.mMessages.when)) {
                            pendingIdleHandlerCount = this.mIdleHandlers.size();
                        }
                        if (pendingIdleHandlerCount > 0) {
                            if (this.mPendingIdleHandlers == null) {
                                this.mPendingIdleHandlers = new MessageQueue.IdleHandler[Math.max(pendingIdleHandlerCount, 4)];
                            }
                            this.mPendingIdleHandlers = (MessageQueue.IdleHandler[]) this.mIdleHandlers.toArray(this.mPendingIdleHandlers);
                            break;
                        }
                        this.mBlocked = true;
                    }
                }
                for (int i = 0; i < pendingIdleHandlerCount; i++) {
                    MessageQueue.IdleHandler idler = this.mPendingIdleHandlers[i];
                    this.mPendingIdleHandlers[i] = null;
                    boolean keep = false;
                    try {
                        keep = idler.queueIdle();
                    } catch (Throwable var11) {
                        ModernUI.LOGGER.fatal(MARKER, "IdleHandler threw exception", var11);
                    }
                    if (!keep) {
                        synchronized (this) {
                            this.mIdleHandlers.remove(idler);
                        }
                    }
                }
                pendingIdleHandlerCount = 0;
                nextPollTimeoutMillis = 0;
            }
        }
    }

    void quit(boolean safe) {
        synchronized (this) {
            if (!this.mQuitting) {
                this.mQuitting = true;
                if (safe) {
                    this.removeAllFutureMessagesLocked();
                } else {
                    this.removeAllMessagesLocked();
                }
                if (this.mThread == null) {
                    GLFW.glfwPostEmptyEvent();
                } else {
                    LockSupport.unpark(this.mThread);
                }
            }
        }
    }

    public int postSyncBarrier() {
        long when = Core.timeMillis();
        synchronized (this) {
            int token = this.mNextBarrierToken++;
            Message msg = Message.obtain();
            msg.markInUse();
            msg.when = when;
            msg.arg1 = token;
            Message prev = null;
            Message p = this.mMessages;
            if (when != 0L) {
                while (p != null && p.when <= when) {
                    prev = p;
                    p = p.next;
                }
            }
            msg.next = p;
            if (prev != null) {
                prev.next = msg;
            } else {
                this.mMessages = msg;
            }
            return token;
        }
    }

    public void removeSyncBarrier(int token) {
        synchronized (this) {
            Message prev = null;
            Message p;
            for (p = this.mMessages; p != null && (p.target != null || p.arg1 != token); p = p.next) {
                prev = p;
            }
            if (p == null) {
                throw new IllegalStateException("The specified message queue synchronization  barrier token has not been posted or has already been removed.");
            } else {
                boolean needWake;
                if (prev != null) {
                    prev.next = p.next;
                    needWake = false;
                } else {
                    this.mMessages = p.next;
                    needWake = this.mMessages == null || this.mMessages.target != null;
                }
                p.recycleUnchecked();
                if (needWake && !this.mQuitting) {
                    if (this.mThread == null) {
                        GLFW.glfwPostEmptyEvent();
                    } else {
                        LockSupport.unpark(this.mThread);
                    }
                }
            }
        }
    }

    boolean enqueueMessage(@NonNull Message msg, long when) {
        if (msg.target == null) {
            throw new IllegalArgumentException("Message must have a target.");
        } else {
            synchronized (this) {
                if (msg.isInUse()) {
                    throw new IllegalStateException(msg + " This message is already in use.");
                } else if (this.mQuitting) {
                    IllegalStateException e = new IllegalStateException(msg.target + " sending message to a Handler on a dead thread");
                    ModernUI.LOGGER.warn(MARKER, e.getMessage(), e);
                    msg.recycle();
                    return false;
                } else {
                    msg.markInUse();
                    msg.when = when;
                    Message p = this.mMessages;
                    boolean needWake;
                    if (p != null && when != 0L && when >= p.when) {
                        needWake = this.mBlocked && p.target == null && msg.isAsynchronous();
                        while (true) {
                            Message prev = p;
                            p = p.next;
                            if (p == null || when < p.when) {
                                msg.next = p;
                                prev.next = msg;
                                break;
                            }
                            if (needWake && p.isAsynchronous()) {
                                needWake = false;
                            }
                        }
                    } else {
                        msg.next = p;
                        this.mMessages = msg;
                        needWake = this.mBlocked;
                    }
                    if (needWake) {
                        if (this.mThread == null) {
                            GLFW.glfwPostEmptyEvent();
                        } else {
                            LockSupport.unpark(this.mThread);
                        }
                    }
                    return true;
                }
            }
        }
    }

    boolean hasMessages(Handler h, int what, Object object) {
        if (h == null) {
            return false;
        } else {
            synchronized (this) {
                for (Message p = this.mMessages; p != null; p = p.next) {
                    if (p.target == h && p.what == what && (object == null || p.obj == object)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    boolean hasMessages(@NonNull Handler h, Runnable r) {
        synchronized (this) {
            for (Message p = this.mMessages; p != null; p = p.next) {
                if (p.target == h && p.callback == r) {
                    return true;
                }
            }
            return false;
        }
    }

    boolean hasMessages(@NonNull Handler h) {
        synchronized (this) {
            for (Message p = this.mMessages; p != null; p = p.next) {
                if (p.target == h) {
                    return true;
                }
            }
            return false;
        }
    }

    void removeMessages(@NonNull Handler h, int what, Object object) {
        synchronized (this) {
            Message p = this.mMessages;
            while (p != null && p.target == h && p.what == what && (object == null || p.obj == object)) {
                Message n = p.next;
                this.mMessages = n;
                p.recycleUnchecked();
                p = n;
            }
            while (p != null) {
                Message n = p.next;
                if (n != null && n.target == h && n.what == what && (object == null || n.obj == object)) {
                    Message nn = n.next;
                    n.recycleUnchecked();
                    p.next = nn;
                } else {
                    p = n;
                }
            }
        }
    }

    void removeMessages(@NonNull Handler h, Runnable r, Object object) {
        if (r != null) {
            synchronized (this) {
                Message p = this.mMessages;
                while (p != null && p.target == h && p.callback == r && (object == null || p.obj == object)) {
                    Message n = p.next;
                    this.mMessages = n;
                    p.recycleUnchecked();
                    p = n;
                }
                while (p != null) {
                    Message n = p.next;
                    if (n != null && n.target == h && n.callback == r && (object == null || n.obj == object)) {
                        Message nn = n.next;
                        n.recycleUnchecked();
                        p.next = nn;
                    } else {
                        p = n;
                    }
                }
            }
        }
    }

    void removeCallbacksAndMessages(@NonNull Handler h, Object object) {
        synchronized (this) {
            Message p = this.mMessages;
            while (p != null && p.target == h && (object == null || p.obj == object)) {
                Message n = p.next;
                this.mMessages = n;
                p.recycleUnchecked();
                p = n;
            }
            while (p != null) {
                Message n = p.next;
                if (n != null && n.target == h && (object == null || n.obj == object)) {
                    Message nn = n.next;
                    n.recycleUnchecked();
                    p.next = nn;
                } else {
                    p = n;
                }
            }
        }
    }

    private void removeAllMessagesLocked() {
        Message p = this.mMessages;
        while (p != null) {
            Message n = p.next;
            p.recycleUnchecked();
            p = n;
        }
        this.mMessages = null;
    }

    private void removeAllFutureMessagesLocked() {
        long now = Core.timeMillis();
        Message p = this.mMessages;
        if (p != null) {
            if (p.when > now) {
                this.removeAllMessagesLocked();
            } else {
                while (true) {
                    Message n = p.next;
                    if (n == null) {
                        return;
                    }
                    if (n.when > now) {
                        p.next = null;
                        do {
                            p = n;
                            n = n.next;
                            p.recycleUnchecked();
                        } while (n != null);
                        return;
                    }
                    p = n;
                }
            }
        }
    }

    @FunctionalInterface
    public interface IdleHandler {

        boolean queueIdle();
    }
}