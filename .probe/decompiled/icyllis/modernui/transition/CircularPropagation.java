package icyllis.modernui.transition;

import icyllis.modernui.graphics.Rect;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CircularPropagation extends VisibilityPropagation {

    private float mPropagationSpeed = 3.0F;

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
            TransitionValues positionValues;
            if (endValues != null && this.getViewVisibility(startValues) != 0) {
                positionValues = endValues;
            } else {
                positionValues = startValues;
                directionMultiplier = -1;
            }
            int viewCenterX = this.getViewX(positionValues);
            int viewCenterY = this.getViewY(positionValues);
            Rect epicenter = transition.getEpicenter();
            int epicenterX;
            int epicenterY;
            if (epicenter != null) {
                epicenterX = epicenter.centerX();
                epicenterY = epicenter.centerY();
            } else {
                int[] loc = new int[2];
                sceneRoot.getLocationInWindow(loc);
                int w = sceneRoot.getWidth() / 2;
                epicenterX = Math.round((float) (loc[0] + w) + sceneRoot.getTranslationX());
                int h = sceneRoot.getHeight() / 2;
                epicenterY = Math.round((float) (loc[1] + h) + sceneRoot.getTranslationY());
            }
            float distance = distance((float) viewCenterX, (float) viewCenterY, (float) epicenterX, (float) epicenterY);
            float maxDistance = distance(0.0F, 0.0F, (float) sceneRoot.getWidth(), (float) sceneRoot.getHeight());
            float distanceFraction = distance / maxDistance;
            long duration = transition.getDuration();
            if (duration < 0L) {
                duration = 300L;
            }
            return (long) Math.round((float) (duration * (long) directionMultiplier) / this.mPropagationSpeed * distanceFraction);
        }
    }

    private static float distance(float x1, float y1, float x2, float y2) {
        float x = x2 - x1;
        float y = y2 - y1;
        return (float) Math.sqrt((double) (x * x + y * y));
    }
}