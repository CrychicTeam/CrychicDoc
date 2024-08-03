package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.util.Pools;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class VelocityTracker {

    private static final Pools.Pool<VelocityTracker> sPool = Pools.newSynchronizedPool(2);

    private static final long ASSUME_POINTER_STOPPED_TIME = 40000000L;

    public static final int VELOCITY_TRACKER_STRATEGY_DEFAULT = -1;

    public static final int VELOCITY_TRACKER_STRATEGY_IMPULSE = 0;

    public static final int VELOCITY_TRACKER_STRATEGY_LSQ1 = 1;

    public static final int VELOCITY_TRACKER_STRATEGY_LSQ2 = 2;

    public static final int VELOCITY_TRACKER_STRATEGY_LSQ3 = 3;

    public static final int VELOCITY_TRACKER_STRATEGY_WLSQ2_DELTA = 4;

    public static final int VELOCITY_TRACKER_STRATEGY_WLSQ2_CENTRAL = 5;

    public static final int VELOCITY_TRACKER_STRATEGY_WLSQ2_RECENT = 6;

    private final int mStrategyId;

    private long mLastEventTime;

    private float mVelocityX;

    private float mVelocityY;

    private final VelocityTracker.Strategy mStrategy;

    private final VelocityTracker.Estimator mEstimator = new VelocityTracker.Estimator();

    private VelocityTracker(int strategy) {
        this.mStrategyId = strategy;
        if (strategy == -1) {
            strategy = 2;
        }
        this.mStrategy = (VelocityTracker.Strategy) (switch(strategy) {
            case 0 ->
                new VelocityTracker.ImpulseStrategy();
            case 1 ->
                new VelocityTracker.LeastSquaresStrategy(1);
            case 2 ->
                new VelocityTracker.LeastSquaresStrategy(2);
            case 3 ->
                new VelocityTracker.LeastSquaresStrategy(3);
            case 4 ->
                new VelocityTracker.LeastSquaresStrategy(2, VelocityTracker.LeastSquaresStrategy.Weighting.WEIGHTING_DELTA);
            case 5 ->
                new VelocityTracker.LeastSquaresStrategy(2, VelocityTracker.LeastSquaresStrategy.Weighting.WEIGHTING_CENTRAL);
            case 6 ->
                new VelocityTracker.LeastSquaresStrategy(2, VelocityTracker.LeastSquaresStrategy.Weighting.WEIGHTING_RECENT);
            default ->
                throw new IllegalStateException("Unexpected value: " + strategy);
        });
    }

    @NonNull
    public static VelocityTracker obtain() {
        VelocityTracker instance = sPool.acquire();
        return instance != null ? instance : new VelocityTracker(-1);
    }

    @NonNull
    public static VelocityTracker obtain(int strategy) {
        return new VelocityTracker(strategy);
    }

    public void recycle() {
        if (this.mStrategyId == -1) {
            this.clear();
            sPool.release(this);
        }
    }

    public int getStrategyId() {
        return this.mStrategyId;
    }

    public void clear() {
        this.mVelocityX = 0.0F;
        this.mVelocityY = 0.0F;
        this.mStrategy.clear();
        this.mEstimator.clear();
    }

    public void addMovement(@NonNull MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case 0:
            case 9:
                this.clear();
            case 2:
            case 7:
                long eventTime = event.getEventTimeNano();
                if (eventTime - this.mLastEventTime >= 40000000L) {
                    this.mStrategy.clear();
                }
                this.mLastEventTime = eventTime;
                this.mStrategy.addMovement(eventTime, event.getX(), event.getY());
                return;
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
        }
    }

    public boolean computeCurrentVelocity(int units) {
        if (this.mStrategy.getEstimator(this.mEstimator) && this.mEstimator.degree >= 1) {
            float vx = this.mEstimator.xCoeff[1];
            float vy = this.mEstimator.yCoeff[1];
            this.mVelocityX = vx * (float) units / 1000.0F;
            this.mVelocityY = vy * (float) units / 1000.0F;
            return true;
        } else {
            this.mVelocityX = 0.0F;
            this.mVelocityY = 0.0F;
            return false;
        }
    }

    public boolean computeCurrentVelocity(int units, float maxVelocity) {
        if (this.mStrategy.getEstimator(this.mEstimator) && this.mEstimator.degree >= 1) {
            float vx = this.mEstimator.xCoeff[1];
            float vy = this.mEstimator.yCoeff[1];
            this.mVelocityX = Math.max(Math.min(vx * (float) units / 1000.0F, maxVelocity), -maxVelocity);
            this.mVelocityY = Math.max(Math.min(vy * (float) units / 1000.0F, maxVelocity), -maxVelocity);
            return true;
        } else {
            this.mVelocityX = 0.0F;
            this.mVelocityY = 0.0F;
            return false;
        }
    }

    public float getXVelocity() {
        return this.mVelocityX;
    }

    public float getYVelocity() {
        return this.mVelocityY;
    }

    @Internal
    public boolean getEstimator(@NonNull VelocityTracker.Estimator outEstimator) {
        return this.mStrategy.getEstimator(outEstimator);
    }

    abstract static class CommonStrategy implements VelocityTracker.Strategy {

        static final int HORIZON = 100000000;

        static final int HISTORY_SIZE = 20;

        int mIndex;

        final long[] mEventTime = new long[20];

        final float[] mX = new float[20];

        final float[] mY = new float[20];

        final float[] mTmpX = new float[20];

        final float[] mTmpY = new float[20];

        CommonStrategy() {
            this.clear();
        }

        @Override
        public void clear() {
            this.mIndex = 0;
        }

        @Override
        public void addMovement(long eventTime, float x, float y) {
            if (this.mEventTime[this.mIndex] != eventTime) {
                this.mIndex++;
            }
            if (this.mIndex == 20) {
                this.mIndex = 0;
            }
            this.mEventTime[this.mIndex] = eventTime;
            this.mX[this.mIndex] = x;
            this.mY[this.mIndex] = y;
        }
    }

    @Internal
    public static final class Estimator {

        private static final int MAX_DEGREE = 4;

        public long time;

        public final float[] xCoeff = new float[5];

        public final float[] yCoeff = new float[5];

        public int degree;

        public float confidence;

        public float estimateX(float time) {
            return this.estimate(time, this.xCoeff);
        }

        public float estimateY(float time) {
            return this.estimate(time, this.yCoeff);
        }

        public float getXCoeff(int index) {
            return index <= this.degree ? this.xCoeff[index] : 0.0F;
        }

        public float getYCoeff(int index) {
            return index <= this.degree ? this.yCoeff[index] : 0.0F;
        }

        public void clear() {
            this.time = 0L;
            this.degree = 0;
            this.confidence = 0.0F;
            for (int i = 0; i <= 4; i++) {
                this.xCoeff[i] = 0.0F;
                this.yCoeff[i] = 0.0F;
            }
        }

        private float estimate(float time, float[] c) {
            float a = 0.0F;
            float scale = 1.0F;
            for (int i = 0; i <= this.degree; i++) {
                a += c[i] * scale;
                scale *= time;
            }
            return a;
        }
    }

    public static class ImpulseStrategy extends VelocityTracker.CommonStrategy {

        final long[] mTmpTime = new long[20];

        static float kineticEnergyToVelocity(float work) {
            return (float) ((work < 0.0F ? -1.0 : 1.0) * Math.sqrt((double) Math.abs(work)) * 1.41421356237);
        }

        static float calculateImpulseVelocity(long[] t, float[] v, int count) {
            if (count < 2) {
                return 0.0F;
            } else {
                assert t[1] <= t[0];
                if (count == 2) {
                    return t[1] == t[0] ? 0.0F : (v[1] - v[0]) / (1.0E-9F * (float) (t[1] - t[0]));
                } else {
                    float work = 0.0F;
                    for (int i = count - 1; i > 0; i--) {
                        if (t[i] != t[i - 1]) {
                            float vPrev = kineticEnergyToVelocity(work);
                            float vCurr = (v[i] - v[i - 1]) / (1.0E-9F * (float) (t[i] - t[i - 1]));
                            work += (vCurr - vPrev) * Math.abs(vCurr);
                            if (i == count - 1) {
                                work = (float) ((double) work * 0.5);
                            }
                        }
                    }
                    return kineticEnergyToVelocity(work);
                }
            }
        }

        @Override
        public boolean getEstimator(@NonNull VelocityTracker.Estimator outEstimator) {
            outEstimator.clear();
            int m = 0;
            int index = this.mIndex;
            long newestTime = this.mEventTime[this.mIndex];
            do {
                long time = this.mEventTime[index];
                long age = newestTime - time;
                if (age > 100000000L) {
                    break;
                }
                this.mTmpX[m] = this.mX[index];
                this.mTmpY[m] = this.mY[index];
                this.mTmpTime[m] = time;
                index = (index == 0 ? 20 : index) - 1;
            } while (++m < 20);
            if (m == 0) {
                return false;
            } else {
                outEstimator.xCoeff[0] = 0.0F;
                outEstimator.yCoeff[0] = 0.0F;
                outEstimator.xCoeff[1] = calculateImpulseVelocity(this.mTmpTime, this.mTmpX, m);
                outEstimator.yCoeff[1] = calculateImpulseVelocity(this.mTmpTime, this.mTmpY, m);
                outEstimator.xCoeff[2] = 0.0F;
                outEstimator.yCoeff[2] = 0.0F;
                outEstimator.time = newestTime;
                outEstimator.degree = 2;
                outEstimator.confidence = 1.0F;
                return true;
            }
        }
    }

    public static class LeastSquaresStrategy extends VelocityTracker.CommonStrategy {

        final int mDegree;

        final VelocityTracker.LeastSquaresStrategy.Weighting mWeighting;

        final float[] mTmpW = new float[20];

        final float[] mTmpTime = new float[20];

        final float[] mTmpXCoeff = new float[3];

        final float[] mTmpYCoeff = new float[3];

        public LeastSquaresStrategy(int degree) {
            this(degree, VelocityTracker.LeastSquaresStrategy.Weighting.WEIGHTING_NONE);
        }

        public LeastSquaresStrategy(int degree, VelocityTracker.LeastSquaresStrategy.Weighting weighting) {
            this.mDegree = degree;
            this.mWeighting = weighting;
        }

        static float vectorDot(float[] a, float[] b, int m) {
            float r = 0.0F;
            for (int i = 0; i < m; i++) {
                r += a[i] * b[i];
            }
            return r;
        }

        static float vectorNorm(float[] a, int m) {
            float r = 0.0F;
            for (int i = 0; i < m; i++) {
                float t = a[i];
                r += t * t;
            }
            return (float) Math.sqrt((double) r);
        }

        @Experimental
        static float solveLeastSquares(float[] t, float[] v, float[] w, int m, int n, float[] outB) {
            float[][] a = new float[n][m];
            for (int h = 0; h < m; h++) {
                a[0][h] = w[h];
                for (int i = 1; i < n; i++) {
                    a[i][h] = a[i - 1][h] * t[h];
                }
            }
            float[][] q = new float[n][m];
            float[][] r = new float[n][n];
            for (int j = 0; j < n; j++) {
                System.arraycopy(a[j], 0, q[j], 0, m);
                for (int i = 0; i < j; i++) {
                    float dot = vectorDot(q[j], q[i], m);
                    for (int h = 0; h < m; h++) {
                        q[j][h] = q[j][h] - dot * q[i][h];
                    }
                }
                float norm = vectorNorm(q[j], m);
                if (norm < 1.0E-6F) {
                    return Float.NaN;
                }
                float invNorm = 1.0F / norm;
                for (int h = 0; h < m; h++) {
                    q[j][h] = q[j][h] * invNorm;
                }
                for (int i = 0; i < n; i++) {
                    r[j][i] = i < j ? 0.0F : vectorDot(q[j], a[i], m);
                }
            }
            float[] wy = new float[m];
            for (int h = 0; h < m; h++) {
                wy[h] = v[h] * w[h];
            }
            int i = n;
            while (i != 0) {
                outB[i] = vectorDot(q[--i], wy, m);
                for (int j = n - 1; j > i; j--) {
                    outB[i] -= r[i][j] * outB[j];
                }
                outB[i] /= r[i][i];
            }
            float ymean = 0.0F;
            for (int h = 0; h < m; h++) {
                ymean += v[h];
            }
            float var24 = ymean / (float) m;
            float sserr = 0.0F;
            float sstot = 0.0F;
            for (int h = 0; h < m; h++) {
                float err = v[h] - outB[0];
                float term = 1.0F;
                for (int ix = 1; ix < n; ix++) {
                    term *= t[h];
                    err -= term * outB[ix];
                }
                sserr += w[h] * w[h] * err * err;
                float var = v[h] - var24;
                sstot += w[h] * w[h] * var * var;
            }
            return sstot > 1.0E-6F ? 1.0F - sserr / sstot : 1.0F;
        }

        static boolean solveUnweightedLeastSquaresDeg2(float[] t, float[] v, int count, float[] out) {
            float sxi = 0.0F;
            float sxiyi = 0.0F;
            float syi = 0.0F;
            float sxi2 = 0.0F;
            float sxi3 = 0.0F;
            float sxi2yi = 0.0F;
            float sxi4 = 0.0F;
            for (int i = 0; i < count; i++) {
                float xi = t[i];
                float yi = v[i];
                float xi2 = xi * xi;
                float xi3 = xi2 * xi;
                float xi4 = xi3 * xi;
                float xiyi = xi * yi;
                float xi2yi = xi2 * yi;
                sxi += xi;
                sxi2 += xi2;
                sxiyi += xiyi;
                sxi2yi += xi2yi;
                syi += yi;
                sxi3 += xi3;
                sxi4 += xi4;
            }
            float Sxx = sxi2 - sxi * sxi / (float) count;
            float Sxy = sxiyi - sxi * syi / (float) count;
            float Sxx2 = sxi3 - sxi * sxi2 / (float) count;
            float Sx2y = sxi2yi - sxi2 * syi / (float) count;
            float Sx2x2 = sxi4 - sxi2 * sxi2 / (float) count;
            float denominator = Sxx * Sx2x2 - Sxx2 * Sxx2;
            if (denominator == 0.0F) {
                return false;
            } else {
                float numerator = Sx2y * Sxx - Sxy * Sxx2;
                float a = numerator / denominator;
                numerator = Sxy * Sx2x2 - Sx2y * Sxx2;
                float b = numerator / denominator;
                float c = syi / (float) count - b * sxi / (float) count - a * sxi2 / (float) count;
                out[0] = c;
                out[1] = b;
                out[2] = a;
                return true;
            }
        }

        @Override
        public boolean getEstimator(@NonNull VelocityTracker.Estimator outEstimator) {
            outEstimator.clear();
            int m = 0;
            int index = this.mIndex;
            long newestTime = this.mEventTime[this.mIndex];
            do {
                long time = this.mEventTime[index];
                long age = newestTime - time;
                if (age > 100000000L) {
                    break;
                }
                this.mTmpX[m] = this.mX[index];
                this.mTmpY[m] = this.mY[index];
                this.mTmpW[m] = this.chooseWeight(index);
                this.mTmpTime[m] = (float) (-age) * 1.0E-9F;
                index = (index == 0 ? 20 : index) - 1;
            } while (++m < 20);
            if (m == 0) {
                return false;
            } else {
                int degree = this.mDegree;
                if (degree > m - 1) {
                    degree = m - 1;
                }
                if (degree == 2 && this.mWeighting == VelocityTracker.LeastSquaresStrategy.Weighting.WEIGHTING_NONE) {
                    boolean xCoeff = solveUnweightedLeastSquaresDeg2(this.mTmpTime, this.mTmpX, m, this.mTmpXCoeff);
                    boolean yCoeff = solveUnweightedLeastSquaresDeg2(this.mTmpTime, this.mTmpY, m, this.mTmpYCoeff);
                    if (xCoeff && yCoeff) {
                        outEstimator.time = newestTime;
                        outEstimator.degree = 2;
                        outEstimator.confidence = 1.0F;
                        for (int i = 0; i <= outEstimator.degree; i++) {
                            outEstimator.xCoeff[i] = this.mTmpXCoeff[i];
                            outEstimator.yCoeff[i] = this.mTmpYCoeff[i];
                        }
                        return true;
                    }
                } else if (degree >= 1) {
                    int n = degree + 1;
                    float xDet = solveLeastSquares(this.mTmpTime, this.mTmpX, this.mTmpW, m, n, outEstimator.xCoeff);
                    float yDet = solveLeastSquares(this.mTmpTime, this.mTmpY, this.mTmpW, m, n, outEstimator.yCoeff);
                    if (!Float.isNaN(xDet) && !Float.isNaN(yDet)) {
                        outEstimator.time = newestTime;
                        outEstimator.degree = degree;
                        outEstimator.confidence = xDet * yDet;
                        return true;
                    }
                }
                outEstimator.xCoeff[0] = this.mTmpX[0];
                outEstimator.yCoeff[0] = this.mTmpY[0];
                outEstimator.time = newestTime;
                outEstimator.degree = 0;
                outEstimator.confidence = 1.0F;
                return true;
            }
        }

        private float chooseWeight(int index) {
            return switch(this.mWeighting) {
                case WEIGHTING_DELTA ->
                    {
                        if (index == this.mIndex) {
                        } else {
                            int nextIndex = (index + 1) % 20;
                            float deltaMillis = (float) (this.mEventTime[nextIndex] - this.mEventTime[index]) * 1.0E-6F;
                            ???;
                        }
                    }
                case WEIGHTING_CENTRAL ->
                    {
                        float ageMillis = (float) (this.mEventTime[this.mIndex] - this.mEventTime[index]) * 1.0E-6F;
                        ???;
                    }
                case WEIGHTING_RECENT ->
                    {
                        float ageMillis = (float) (this.mEventTime[this.mIndex] - this.mEventTime[index]) * 1.0E-6F;
                        ???;
                    }
                default ->
                    1.0F;
            };
        }

        public static enum Weighting {

            WEIGHTING_NONE, WEIGHTING_DELTA, WEIGHTING_CENTRAL, WEIGHTING_RECENT
        }
    }

    public interface Strategy {

        void clear();

        void addMovement(long var1, float var3, float var4);

        boolean getEstimator(@NonNull VelocityTracker.Estimator var1);
    }
}