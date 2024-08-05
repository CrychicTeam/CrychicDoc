package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import java.util.Objects;

public final class OneShotPreDrawListener implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {

    private final View mView;

    private ViewTreeObserver mViewTreeObserver;

    private final Runnable mRunnable;

    private OneShotPreDrawListener(@NonNull View view, @NonNull Runnable runnable) {
        this.mView = view;
        this.mViewTreeObserver = view.getViewTreeObserver();
        this.mRunnable = runnable;
    }

    @NonNull
    public static OneShotPreDrawListener add(@NonNull View view, @NonNull Runnable runnable) {
        Objects.requireNonNull(view);
        Objects.requireNonNull(runnable);
        OneShotPreDrawListener listener = new OneShotPreDrawListener(view, runnable);
        view.getViewTreeObserver().addOnPreDrawListener(listener);
        view.addOnAttachStateChangeListener(listener);
        return listener;
    }

    @Override
    public boolean onPreDraw() {
        this.removeListener();
        this.mRunnable.run();
        return true;
    }

    public void removeListener() {
        if (this.mViewTreeObserver.isAlive()) {
            this.mViewTreeObserver.removeOnPreDrawListener(this);
        } else {
            this.mView.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        this.mView.removeOnAttachStateChangeListener(this);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull View v) {
        this.mViewTreeObserver = v.getViewTreeObserver();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull View v) {
        this.removeListener();
    }
}