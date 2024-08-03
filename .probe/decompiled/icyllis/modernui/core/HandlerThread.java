package icyllis.modernui.core;

import icyllis.modernui.annotation.NonNull;
import org.jetbrains.annotations.ApiStatus.Internal;

public class HandlerThread extends Thread {

    private Looper mLooper;

    private Handler mHandler;

    public HandlerThread(String name) {
        super(name);
    }

    protected void onLooperPrepared() {
    }

    public void run() {
        Looper.prepare();
        synchronized (this) {
            this.mLooper = Looper.myLooper();
            this.notifyAll();
        }
        this.onLooperPrepared();
        Looper.loop();
    }

    public Looper getLooper() {
        if (!this.isAlive()) {
            return null;
        } else {
            boolean wasInterrupted = false;
            synchronized (this) {
                while (this.isAlive() && this.mLooper == null) {
                    try {
                        this.wait();
                    } catch (InterruptedException var5) {
                        wasInterrupted = true;
                    }
                }
            }
            if (wasInterrupted) {
                Thread.currentThread().interrupt();
            }
            return this.mLooper;
        }
    }

    @Internal
    @NonNull
    public Handler getThreadHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(this.getLooper());
        }
        return this.mHandler;
    }

    public boolean quit() {
        Looper looper = this.getLooper();
        if (looper != null) {
            looper.quit();
            return true;
        } else {
            return false;
        }
    }

    public boolean quitSafely() {
        Looper looper = this.getLooper();
        if (looper != null) {
            looper.quitSafely();
            return true;
        } else {
            return false;
        }
    }
}