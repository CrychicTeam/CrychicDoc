package icyllis.modernui.fragment;

import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.lifecycle.Lifecycle;
import icyllis.modernui.lifecycle.LifecycleObserver;
import icyllis.modernui.lifecycle.LifecycleOwner;
import java.util.ArrayDeque;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class OnBackPressedDispatcher {

    @Nullable
    private final Runnable mFallbackOnBackPressed;

    final ArrayDeque<OnBackPressedCallback> mOnBackPressedCallbacks = new ArrayDeque();

    public OnBackPressedDispatcher() {
        this(null);
    }

    public OnBackPressedDispatcher(@Nullable Runnable fallbackOnBackPressed) {
        this.mFallbackOnBackPressed = fallbackOnBackPressed;
    }

    @UiThread
    public void addCallback(@Nonnull OnBackPressedCallback onBackPressedCallback) {
        this.addCancellableCallback(onBackPressedCallback);
    }

    @Nonnull
    @UiThread
    OnBackPressedCallback.Cancellable addCancellableCallback(@Nonnull OnBackPressedCallback onBackPressedCallback) {
        this.mOnBackPressedCallbacks.add(onBackPressedCallback);
        OnBackPressedDispatcher.OnBackPressedCancellable cancellable = new OnBackPressedDispatcher.OnBackPressedCancellable(onBackPressedCallback);
        onBackPressedCallback.addCancellable(cancellable);
        return cancellable;
    }

    @UiThread
    public void addCallback(@Nonnull LifecycleOwner owner, @Nonnull OnBackPressedCallback callback) {
        Lifecycle lifecycle = owner.getLifecycle();
        if (lifecycle.getCurrentState() != Lifecycle.State.DESTROYED) {
            callback.addCancellable(new OnBackPressedDispatcher.LifecycleOnBackPressedCancellable(lifecycle, callback));
        }
    }

    @UiThread
    public boolean hasEnabledCallbacks() {
        Iterator<OnBackPressedCallback> iterator = this.mOnBackPressedCallbacks.descendingIterator();
        while (iterator.hasNext()) {
            if (((OnBackPressedCallback) iterator.next()).isEnabled()) {
                return true;
            }
        }
        return false;
    }

    @UiThread
    public void onBackPressed() {
        Iterator<OnBackPressedCallback> iterator = this.mOnBackPressedCallbacks.descendingIterator();
        while (iterator.hasNext()) {
            OnBackPressedCallback callback = (OnBackPressedCallback) iterator.next();
            if (callback.isEnabled()) {
                callback.handleOnBackPressed();
                return;
            }
        }
        if (this.mFallbackOnBackPressed != null) {
            this.mFallbackOnBackPressed.run();
        }
    }

    private class LifecycleOnBackPressedCancellable implements LifecycleObserver, OnBackPressedCallback.Cancellable {

        private final Lifecycle mLifecycle;

        private final OnBackPressedCallback mOnBackPressedCallback;

        @Nullable
        private OnBackPressedCallback.Cancellable mCurrentCancellable;

        LifecycleOnBackPressedCancellable(@Nonnull Lifecycle lifecycle, @Nonnull OnBackPressedCallback onBackPressedCallback) {
            this.mLifecycle = lifecycle;
            this.mOnBackPressedCallback = onBackPressedCallback;
            lifecycle.addObserver(this);
        }

        @Override
        public void onStateChanged(@Nonnull LifecycleOwner source, @Nonnull Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_START) {
                this.mCurrentCancellable = OnBackPressedDispatcher.this.addCancellableCallback(this.mOnBackPressedCallback);
            } else if (event == Lifecycle.Event.ON_STOP) {
                if (this.mCurrentCancellable != null) {
                    this.mCurrentCancellable.cancel();
                }
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                this.cancel();
            }
        }

        @Override
        public void cancel() {
            this.mLifecycle.removeObserver(this);
            this.mOnBackPressedCallback.removeCancellable(this);
            if (this.mCurrentCancellable != null) {
                this.mCurrentCancellable.cancel();
                this.mCurrentCancellable = null;
            }
        }
    }

    private class OnBackPressedCancellable implements OnBackPressedCallback.Cancellable {

        private final OnBackPressedCallback mOnBackPressedCallback;

        OnBackPressedCancellable(OnBackPressedCallback onBackPressedCallback) {
            this.mOnBackPressedCallback = onBackPressedCallback;
        }

        @Override
        public void cancel() {
            OnBackPressedDispatcher.this.mOnBackPressedCallbacks.remove(this.mOnBackPressedCallback);
            this.mOnBackPressedCallback.removeCancellable(this);
        }
    }
}