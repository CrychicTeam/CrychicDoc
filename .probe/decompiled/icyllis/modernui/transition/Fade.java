package icyllis.modernui.transition;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.AnimatorListener;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.util.FloatProperty;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Fade extends Visibility {

    private static final String PROPNAME_TRANSITION_ALPHA = "modernui:fade:transitionAlpha";

    public static final int IN = 1;

    public static final int OUT = 2;

    private static final FloatProperty<View> TRANSITION_ALPHA = new FloatProperty<View>("transitionAlpha") {

        public void setValue(View view, float alpha) {
            view.setTransitionAlpha(alpha);
        }

        public Float get(View view) {
            return view.getTransitionAlpha();
        }
    };

    public Fade() {
    }

    public Fade(int fadingMode) {
        this.setMode(fadingMode);
    }

    @Override
    public void captureStartValues(@Nonnull TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        transitionValues.values.put("modernui:fade:transitionAlpha", transitionValues.view.getTransitionAlpha());
    }

    @Nullable
    private Animator createAnimation(View view, float startAlpha, float endAlpha) {
        if (startAlpha == endAlpha) {
            return null;
        } else {
            view.setTransitionAlpha(startAlpha);
            ObjectAnimator anim = ObjectAnimator.ofFloat(view, TRANSITION_ALPHA, endAlpha);
            Fade.FadeAnimatorListener listener = new Fade.FadeAnimatorListener(view);
            anim.addListener(listener);
            this.addListener(new TransitionListener() {

                @Override
                public void onTransitionEnd(@Nonnull Transition transition) {
                    view.setTransitionAlpha(1.0F);
                    transition.removeListener(this);
                }
            });
            return anim;
        }
    }

    @Nullable
    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        float startAlpha = getStartAlpha(startValues, 0.0F);
        if (startAlpha == 1.0F) {
            startAlpha = 0.0F;
        }
        return this.createAnimation(view, startAlpha, 1.0F);
    }

    @Nullable
    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        float startAlpha = getStartAlpha(startValues, 1.0F);
        return this.createAnimation(view, startAlpha, 0.0F);
    }

    private static float getStartAlpha(TransitionValues startValues, float fallbackValue) {
        float startAlpha = fallbackValue;
        if (startValues != null) {
            Float startAlphaFloat = (Float) startValues.values.get("modernui:fade:transitionAlpha");
            if (startAlphaFloat != null) {
                startAlpha = startAlphaFloat;
            }
        }
        return startAlpha;
    }

    private static class FadeAnimatorListener implements AnimatorListener {

        private final View mView;

        private boolean mLayerTypeChanged = false;

        FadeAnimatorListener(View view) {
            this.mView = view;
        }

        @Override
        public void onAnimationStart(@Nonnull Animator animation) {
        }

        @Override
        public void onAnimationEnd(@Nonnull Animator animation) {
            this.mView.setTransitionAlpha(1.0F);
        }
    }
}