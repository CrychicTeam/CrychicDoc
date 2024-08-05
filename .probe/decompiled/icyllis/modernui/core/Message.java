package icyllis.modernui.core;

import icyllis.modernui.annotation.NonNull;
import java.util.Objects;

public final class Message {

    public int what;

    public int arg1;

    public int arg2;

    public Object obj;

    static final int FLAG_IN_USE = 1;

    static final int FLAG_ASYNCHRONOUS = 2;

    int flags;

    long when;

    Handler target;

    Runnable callback;

    Message next;

    private static final boolean NO_POOLING = true;

    private static final Object sPoolSync = new Object();

    private static Message sPool;

    private static int sPoolSize = 0;

    private static final int MAX_POOL_SIZE = 50;

    @NonNull
    public static Message obtain() {
        return new Message();
    }

    @NonNull
    public static Message obtain(@NonNull Message o) {
        Message m = obtain();
        m.what = o.what;
        m.arg1 = o.arg1;
        m.arg2 = o.arg2;
        m.obj = o.obj;
        m.target = o.target;
        m.callback = o.callback;
        return m;
    }

    @NonNull
    public static Message obtain(@NonNull Handler h) {
        Message m = obtain();
        m.target = h;
        return m;
    }

    @NonNull
    public static Message obtain(@NonNull Handler h, @NonNull Runnable callback) {
        Message m = obtain();
        m.target = h;
        m.callback = callback;
        return m;
    }

    @NonNull
    public static Message obtain(@NonNull Handler h, int what) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        return m;
    }

    @NonNull
    public static Message obtain(@NonNull Handler h, int what, Object obj) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.obj = obj;
        return m;
    }

    @NonNull
    public static Message obtain(@NonNull Handler h, int what, int arg1, int arg2) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        return m;
    }

    @NonNull
    public static Message obtain(@NonNull Handler h, int what, int arg1, int arg2, Object obj) {
        Message m = obtain();
        m.target = h;
        m.what = what;
        m.arg1 = arg1;
        m.arg2 = arg2;
        m.obj = obj;
        return m;
    }

    public void recycle() {
        if (this.isInUse()) {
            throw new IllegalStateException("This message cannot be recycled because it is still in use.");
        } else {
            this.recycleUnchecked();
        }
    }

    void recycleUnchecked() {
        this.flags = 1;
        this.what = 0;
        this.arg1 = 0;
        this.arg2 = 0;
        this.obj = null;
        this.when = 0L;
        this.target = null;
        this.callback = null;
    }

    public long getWhen() {
        return this.when;
    }

    public Handler getTarget() {
        return this.target;
    }

    public Runnable getCallback() {
        return this.callback;
    }

    public void sendToTarget() {
        this.target.sendMessage(this);
    }

    public boolean isAsynchronous() {
        return (this.flags & 2) != 0;
    }

    public void setAsynchronous(boolean async) {
        if (async) {
            this.flags |= 2;
        } else {
            this.flags &= -3;
        }
    }

    boolean isInUse() {
        return (this.flags & 1) == 1;
    }

    void markInUse() {
        this.flags |= 1;
    }

    @NonNull
    public String toString() {
        return this.toString(Core.timeMillis());
    }

    @NonNull
    String toString(long now) {
        StringBuilder b = new StringBuilder();
        b.append("{ when=");
        b.append(this.when - now);
        if (this.target != null) {
            if (this.callback != null) {
                b.append(" callback=");
                b.append(this.callback.getClass().getName());
            } else {
                b.append(" what=");
                b.append(this.what);
            }
            if (this.arg1 != 0) {
                b.append(" arg1=");
                b.append(this.arg1);
            }
            if (this.arg2 != 0) {
                b.append(" arg2=");
                b.append(this.arg2);
            }
            if (this.obj != null) {
                b.append(" obj=");
                b.append(this.obj);
            }
            b.append(" target=");
            b.append(Objects.requireNonNullElse(this.target.mCallback, this.target).getClass().getName());
        } else {
            b.append(" barrier=");
            b.append(this.arg1);
        }
        b.append(" }");
        return b.toString();
    }
}