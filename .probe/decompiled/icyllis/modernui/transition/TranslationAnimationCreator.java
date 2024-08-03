package icyllis.modernui.transition;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.AnimatorListener;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.PropertyValuesHolder;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.view.View;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class TranslationAnimationCreator {

    private TranslationAnimationCreator() {
    }

    @Nullable
    static Animator createAnimation(@Nonnull View view, @Nonnull TransitionValues values, int viewPosX, int viewPosY, float startX, float startY, float endX, float endY, @Nullable TimeInterpolator interpolator, @Nonnull Transition transition) {
        float terminalX = view.getTranslationX();
        float terminalY = view.getTranslationY();
        int[] startPosition = (int[]) values.view.getTag(67239939);
        if (startPosition != null) {
            startX = (float) (startPosition[0] - viewPosX) + terminalX;
            startY = (float) (startPosition[1] - viewPosY) + terminalY;
        }
        int startPosX = viewPosX + Math.round(startX - terminalX);
        int startPosY = viewPosY + Math.round(startY - terminalY);
        view.setTranslationX(startX);
        view.setTranslationY(startY);
        if (startX == endX && startY == endY) {
            return null;
        } else {
            ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat(View.TRANSLATION_X, startX, endX), PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, startY, endY));
            TranslationAnimationCreator.TransitionPositionListener listener = new TranslationAnimationCreator.TransitionPositionListener(view, values.view, startPosX, startPosY, terminalX, terminalY);
            transition.addListener(listener);
            anim.addListener(listener);
            anim.setInterpolator(interpolator);
            return anim;
        }
    }

    private static class TransitionPositionListener implements AnimatorListener, TransitionListener {

        private final View mViewInHierarchy;

        private final View mMovingView;

        private final int mStartX;

        private final int mStartY;

        private int[] mTransitionPosition;

        private float mPausedX;

        private float mPausedY;

        private final float mTerminalX;

        private final float mTerminalY;

        TransitionPositionListener(View movingView, View viewInHierarchy, int startX, int startY, float terminalX, float terminalY) {
            this.mMovingView = movingView;
            this.mViewInHierarchy = viewInHierarchy;
            this.mStartX = startX - Math.round(this.mMovingView.getTranslationX());
            this.mStartY = startY - Math.round(this.mMovingView.getTranslationY());
            this.mTerminalX = terminalX;
            this.mTerminalY = terminalY;
            this.mTransitionPosition = (int[]) this.mViewInHierarchy.getTag(67239939);
            if (this.mTransitionPosition != null) {
                this.mViewInHierarchy.setTag(67239939, null);
            }
        }

        @Override
        public void onAnimationCancel(@Nonnull Animator animation) {
            if (this.mTransitionPosition == null) {
                this.mTransitionPosition = new int[2];
            }
            this.mTransitionPosition[0] = Math.round((float) this.mStartX + this.mMovingView.getTranslationX());
            this.mTransitionPosition[1] = Math.round((float) this.mStartY + this.mMovingView.getTranslationY());
            this.mViewInHierarchy.setTag(67239939, this.mTransitionPosition);
        }

        @Override
        public void onAnimationPause(@Nonnull Animator animator) {
            this.mPausedX = this.mMovingView.getTranslationX();
            this.mPausedY = this.mMovingView.getTranslationY();
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        @Override
        public void onAnimationResume(@Nonnull Animator animator) {
            this.mMovingView.setTranslationX(this.mPausedX);
            this.mMovingView.setTranslationY(this.mPausedY);
        }

        @Override
        public void onTransitionEnd(@Nonnull Transition transition) {
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
            transition.removeListener(this);
        }
    }
}