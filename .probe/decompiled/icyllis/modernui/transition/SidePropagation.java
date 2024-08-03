package icyllis.modernui.transition;

import icyllis.modernui.graphics.Rect;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SidePropagation extends VisibilityPropagation {

    private float mPropagationSpeed = 3.0F;

    private int mSide = 80;

    public void setSide(int side) {
        this.mSide = side;
    }

    public void setPropagationSpeed(float propagationSpeed) {
        if (propagationSpeed == 0.0F) {
            throw new IllegalArgumentException("propagationSpeed may not be 0");
        } else {
            this.mPropagationSpeed = propagationSpeed;
        }
    }

    @Override
    public long getStartDelay(@Nonnull ViewGroup sceneRoot, @Nonnull Transition transition, @Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {
        if (startValues == null && endValues == null) {
            return 0L;
        } else {
            int directionMultiplier = 1;
            Rect epicenter = transition.getEpicenter();
            TransitionValues positionValues;
            if (endValues != null && this.getViewVisibility(startValues) != 0) {
                positionValues = endValues;
            } else {
                positionValues = startValues;
                directionMultiplier = -1;
            }
            int viewCenterX = this.getViewX(positionValues);
            int viewCenterY = this.getViewY(positionValues);
            int[] loc = new int[2];
            sceneRoot.getLocationInWindow(loc);
            int left = loc[0] + Math.round(sceneRoot.getTranslationX());
            int top = loc[1] + Math.round(sceneRoot.getTranslationY());
            int right = left + sceneRoot.getWidth();
            int bottom = top + sceneRoot.getHeight();
            int epicenterX;
            int epicenterY;
            if (epicenter != null) {
                epicenterX = epicenter.centerX();
                epicenterY = epicenter.centerY();
            } else {
                epicenterX = (left + right) / 2;
                epicenterY = (top + bottom) / 2;
            }
            float distance = (float) this.distance(sceneRoot, viewCenterX, viewCenterY, epicenterX, epicenterY, left, top, right, bottom);
            float maxDistance = (float) this.getMaxDistance(sceneRoot);
            float distanceFraction = distance / maxDistance;
            long duration = transition.getDuration();
            if (duration < 0L) {
                duration = 300L;
            }
            return (long) Math.round((float) (duration * (long) directionMultiplier) / this.mPropagationSpeed * distanceFraction);
        }
    }

    private int distance(View sceneRoot, int viewX, int viewY, int epicenterX, int epicenterY, int left, int top, int right, int bottom) {
        int side;
        if (this.mSide == 8388611) {
            boolean isRtl = sceneRoot.isLayoutRtl();
            side = isRtl ? 5 : 3;
        } else if (this.mSide == 8388613) {
            boolean isRtl = sceneRoot.isLayoutRtl();
            side = isRtl ? 3 : 5;
        } else {
            side = this.mSide;
        }
        return switch(side) {
            case 3 ->
                right - viewX + Math.abs(epicenterY - viewY);
            case 5 ->
                viewX - left + Math.abs(epicenterY - viewY);
            case 48 ->
                bottom - viewY + Math.abs(epicenterX - viewX);
            case 80 ->
                viewY - top + Math.abs(epicenterX - viewX);
            default ->
                0;
        };
    }

    private int getMaxDistance(@Nonnull ViewGroup sceneRoot) {
        return switch(this.mSide) {
            case 3, 5, 8388611, 8388613 ->
                sceneRoot.getWidth();
            default ->
                sceneRoot.getHeight();
        };
    }
}