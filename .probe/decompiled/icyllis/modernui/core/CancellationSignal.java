package icyllis.modernui.core;

import icyllis.modernui.annotation.Nullable;

public final class CancellationSignal {

    private boolean mIsCanceled;

    private CancellationSignal.OnCancelListener mOnCancelListener;

    private boolean mCancelInProgress;

    public boolean isCanceled() {
        synchronized (this) {
            return this.mIsCanceled;
        }
    }

    public void throwIfCanceled() {
        if (this.isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    public void cancel() {
        CancellationSignal.OnCancelListener listener;
        synchronized (this) {
            if (this.mIsCanceled) {
                return;
            }
            this.mIsCanceled = true;
            this.mCancelInProgress = true;
            listener = this.mOnCancelListener;
        }
        try {
            if (listener != null) {
                listener.onCancel();
            }
        } finally {
            synchronized (this) {
                this.mCancelInProgress = false;
                this.notifyAll();
            }
        }
    }

    public void setOnCancelListener(@Nullable CancellationSignal.OnCancelListener listener) {
        synchronized (this) {
            this.waitForCancelFinishedLocked();
            if (this.mOnCancelListener == listener) {
                return;
            }
            this.mOnCancelListener = listener;
            if (!this.mIsCanceled || listener == null) {
                return;
            }
        }
        listener.onCancel();
    }

    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                this.wait();
            } catch (InterruptedException var2) {
            }
        }
    }

    @FunctionalInterface
    public interface OnCancelListener {

        void onCancel();
    }
}