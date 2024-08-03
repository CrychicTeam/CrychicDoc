package icyllis.modernui.transition;

import javax.annotation.Nonnull;

public interface TransitionListener {

    default void onTransitionStart(@Nonnull Transition transition) {
    }

    default void onTransitionEnd(@Nonnull Transition transition) {
    }

    default void onTransitionCancel(@Nonnull Transition transition) {
    }

    default void onTransitionPause(@Nonnull Transition transition) {
    }

    default void onTransitionResume(@Nonnull Transition transition) {
    }
}