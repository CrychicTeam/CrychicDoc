package icyllis.modernui.lifecycle;

import javax.annotation.Nonnull;

public interface LifecycleObserver {

    default void onCreate(@Nonnull LifecycleOwner owner) {
    }

    default void onStart(@Nonnull LifecycleOwner owner) {
    }

    default void onResume(@Nonnull LifecycleOwner owner) {
    }

    default void onPause(@Nonnull LifecycleOwner owner) {
    }

    default void onStop(@Nonnull LifecycleOwner owner) {
    }

    default void onDestroy(@Nonnull LifecycleOwner owner) {
    }

    default void onStateChanged(@Nonnull LifecycleOwner source, @Nonnull Lifecycle.Event event) {
    }
}