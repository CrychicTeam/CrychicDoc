package icyllis.modernui.transition;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Slide extends Visibility {

    private static final String PROPNAME_SCREEN_POSITION = "modernui:slide:screenPosition";

    private Slide.CalculateSlide mSlideCalculator;

    private int mSlideEdge;

    private static final Slide.CalculateSlide sCalculateLeft = new Slide.CalculateSlideHorizontal() {

        @Override
        public float getGoneX(@Nonnull ViewGroup sceneRoot, @Nonnull View view) {
            return view.getTranslationX() - (float) sceneRoot.getWidth();
        }
    };

    private static final Slide.CalculateSlide sCalculateStart = new Slide.CalculateSlideHorizontal() {

        @Override
        public float getGoneX(@Nonnull ViewGroup sceneRoot, @Nonnull View view) {
            boolean isRtl = sceneRoot.isLayoutRtl();
            float x;
            if (isRtl) {
                x = view.getTranslationX() + (float) sceneRoot.getWidth();
            } else {
                x = view.getTranslationX() - (float) sceneRoot.getWidth();
            }
            return x;
        }
    };

    private static final Slide.CalculateSlide sCalculateTop = new Slide.CalculateSlideVertical() {

        @Override
        public float getGoneY(@Nonnull ViewGroup sceneRoot, @Nonnull View view) {
            return view.getTranslationY() - (float) sceneRoot.getHeight();
        }
    };

    private static final Slide.CalculateSlide sCalculateRight = new Slide.CalculateSlideHorizontal() {

        @Override
        public float getGoneX(@Nonnull ViewGroup sceneRoot, @Nonnull View view) {
            return view.getTranslationX() + (float) sceneRoot.getWidth();
        }
    };

    private static final Slide.CalculateSlide sCalculateEnd = new Slide.CalculateSlideHorizontal() {

        @Override
        public float getGoneX(@Nonnull ViewGroup sceneRoot, @Nonnull View view) {
            boolean isRtl = sceneRoot.isLayoutRtl();
            float x;
            if (isRtl) {
                x = view.getTranslationX() - (float) sceneRoot.getWidth();
            } else {
                x = view.getTranslationX() + (float) sceneRoot.getWidth();
            }
            return x;
        }
    };

    private static final Slide.CalculateSlide sCalculateBottom = new Slide.CalculateSlideVertical() {

        @Override
        public float getGoneY(@Nonnull ViewGroup sceneRoot, @Nonnull View view) {
            return view.getTranslationY() + (float) sceneRoot.getHeight();
        }
    };

    public Slide() {
        this.mSlideCalculator = sCalculateBottom;
        this.mSlideEdge = 80;
        this.setSlideEdge(80);
    }

    public Slide(int slideEdge) {
        this.mSlideCalculator = sCalculateBottom;
        this.mSlideEdge = 80;
        this.setSlideEdge(slideEdge);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        int[] position = new int[2];
        view.getLocationInWindow(position);
        transitionValues.values.put("modernui:slide:screenPosition", position);
    }

    @Override
    public void captureStartValues(@Nonnull TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        this.captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(@Nonnull TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        this.captureValues(transitionValues);
    }

    public void setSlideEdge(int slideEdge) {
        switch(slideEdge) {
            case 3:
                this.mSlideCalculator = sCalculateLeft;
                break;
            case 5:
                this.mSlideCalculator = sCalculateRight;
                break;
            case 48:
                this.mSlideCalculator = sCalculateTop;
                break;
            case 80:
                this.mSlideCalculator = sCalculateBottom;
                break;
            case 8388611:
                this.mSlideCalculator = sCalculateStart;
                break;
            case 8388613:
                this.mSlideCalculator = sCalculateEnd;
                break;
            default:
                throw new IllegalArgumentException("Invalid slide direction");
        }
        this.mSlideEdge = slideEdge;
        SidePropagation propagation = new SidePropagation();
        propagation.setSide(slideEdge);
        this.setPropagation(propagation);
    }

    public int getSlideEdge() {
        return this.mSlideEdge;
    }

    @Nullable
    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        if (endValues == null) {
            return null;
        } else {
            int[] position = (int[]) endValues.values.get("modernui:slide:screenPosition");
            float endX = view.getTranslationX();
            float endY = view.getTranslationY();
            float startX = this.mSlideCalculator.getGoneX(sceneRoot, view);
            float startY = this.mSlideCalculator.getGoneY(sceneRoot, view);
            return TranslationAnimationCreator.createAnimation(view, endValues, position[0], position[1], startX, startY, endX, endY, TimeInterpolator.DECELERATE, this);
        }
    }

    @Nullable
    @Override
    public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null) {
            return null;
        } else {
            int[] position = (int[]) startValues.values.get("modernui:slide:screenPosition");
            float startX = view.getTranslationX();
            float startY = view.getTranslationY();
            float endX = this.mSlideCalculator.getGoneX(sceneRoot, view);
            float endY = this.mSlideCalculator.getGoneY(sceneRoot, view);
            return TranslationAnimationCreator.createAnimation(view, startValues, position[0], position[1], startX, startY, endX, endY, TimeInterpolator.ACCELERATE, this);
        }
    }

    private interface CalculateSlide {

        float getGoneX(@Nonnull ViewGroup var1, @Nonnull View var2);

        float getGoneY(@Nonnull ViewGroup var1, @Nonnull View var2);
    }

    private abstract static class CalculateSlideHorizontal implements Slide.CalculateSlide {

        @Override
        public float getGoneY(@Nonnull ViewGroup sceneRoot, @Nonnull View view) {
            return view.getTranslationY();
        }
    }

    private abstract static class CalculateSlideVertical implements Slide.CalculateSlide {

        @Override
        public float getGoneX(@Nonnull ViewGroup sceneRoot, @Nonnull View view) {
            return view.getTranslationX();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface GravityFlag {
    }
}