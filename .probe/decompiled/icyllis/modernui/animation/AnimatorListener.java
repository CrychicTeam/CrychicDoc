package icyllis.modernui.animation;

import icyllis.modernui.annotation.NonNull;

public interface AnimatorListener {

    default void onAnimationStart(@NonNull Animator animation, boolean isReverse) {
        this.onAnimationStart(animation);
    }

    default void onAnimationEnd(@NonNull Animator animation, boolean isReverse) {
        this.onAnimationEnd(animation);
    }

    default void onAnimationStart(@NonNull Animator animation) {
    }

    default void onAnimationEnd(@NonNull Animator animation) {
    }

    default void onAnimationCancel(@NonNull Animator animation) {
    }

    default void onAnimationRepeat(@NonNull Animator animation) {
    }

    default void onAnimationPause(@NonNull Animator animation) {
    }

    default void onAnimationResume(@NonNull Animator animation) {
    }
}