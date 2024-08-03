package aurelienribon.tweenengine.paths;

import aurelienribon.tweenengine.TweenPath;

public class Linear implements TweenPath {

    @Override
    public float compute(float t, float[] points, int pointsCnt) {
        int segment = (int) Math.floor((double) ((float) (pointsCnt - 1) * t));
        segment = Math.max(segment, 0);
        segment = Math.min(segment, pointsCnt - 2);
        t = t * (float) (pointsCnt - 1) - (float) segment;
        return points[segment] + t * (points[segment + 1] - points[segment]);
    }
}