package icyllis.modernui.graphics.drawable;

import icyllis.modernui.annotation.NonNull;

public interface Animatable2 extends Animatable {

    void registerAnimationCallback(@NonNull Animatable2.AnimationCallback var1);

    boolean unregisterAnimationCallback(@NonNull Animatable2.AnimationCallback var1);

    void clearAnimationCallbacks();

    public interface AnimationCallback {

        default void onAnimationStart(Drawable drawable) {
        }

        default void onAnimationEnd(Drawable drawable) {
        }
    }
}