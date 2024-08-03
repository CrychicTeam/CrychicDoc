package aurelienribon.tweenengine;

import aurelienribon.tweenengine.equations.Quad;
import java.util.HashMap;
import java.util.Map;

public final class Tween extends BaseTween<Tween> {

    public static final int INFINITY = -1;

    private static int combinedAttrsLimit = 3;

    private static int waypointsLimit = 0;

    private static final Pool.Callback<Tween> poolCallback = new Pool.Callback<Tween>() {

        public void onPool(Tween obj) {
            obj.reset();
        }

        public void onUnPool(Tween obj) {
            obj.reset();
        }
    };

    private static final Pool<Tween> pool = new Pool<Tween>(20, poolCallback) {

        protected Tween create() {
            return new Tween();
        }
    };

    private static final Map<Class<?>, TweenAccessor<?>> registeredAccessors = new HashMap();

    private Object target;

    private Class<?> targetClass;

    private TweenAccessor<Object> accessor;

    private int type;

    private TweenEquation equation;

    private TweenPath path;

    private boolean isFrom;

    private boolean isRelative;

    private int combinedAttrsCnt;

    private int waypointsCnt;

    private final float[] startValues = new float[combinedAttrsLimit];

    private final float[] targetValues = new float[combinedAttrsLimit];

    private final float[] waypoints = new float[waypointsLimit * combinedAttrsLimit];

    private float[] accessorBuffer = new float[combinedAttrsLimit];

    private float[] pathBuffer = new float[(2 + waypointsLimit) * combinedAttrsLimit];

    public static void setCombinedAttributesLimit(int limit) {
        combinedAttrsLimit = limit;
    }

    public static void setWaypointsLimit(int limit) {
        waypointsLimit = limit;
    }

    public static String getVersion() {
        return "6.3.3";
    }

    public static int getPoolSize() {
        return pool.size();
    }

    public static void ensurePoolCapacity(int minCapacity) {
        pool.ensureCapacity(minCapacity);
    }

    public static void registerAccessor(Class<?> someClass, TweenAccessor<?> defaultAccessor) {
        registeredAccessors.put(someClass, defaultAccessor);
    }

    public static TweenAccessor<?> getRegisteredAccessor(Class<?> someClass) {
        return (TweenAccessor<?>) registeredAccessors.get(someClass);
    }

    public static Tween to(Object target, int tweenType, float duration) {
        Tween tween = pool.get();
        tween.setup(target, tweenType, duration);
        tween.ease(Quad.INOUT);
        tween.path(TweenPaths.catmullRom);
        return tween;
    }

    public static Tween from(Object target, int tweenType, float duration) {
        Tween tween = pool.get();
        tween.setup(target, tweenType, duration);
        tween.ease(Quad.INOUT);
        tween.path(TweenPaths.catmullRom);
        tween.isFrom = true;
        return tween;
    }

    public static Tween set(Object target, int tweenType) {
        Tween tween = pool.get();
        tween.setup(target, tweenType, 0.0F);
        tween.ease(Quad.INOUT);
        return tween;
    }

    public static Tween call(TweenCallback callback) {
        Tween tween = pool.get();
        tween.setup(null, -1, 0.0F);
        tween.setCallback(callback);
        tween.setCallbackTriggers(2);
        return tween;
    }

    public static Tween mark() {
        Tween tween = pool.get();
        tween.setup(null, -1, 0.0F);
        return tween;
    }

    private Tween() {
        this.reset();
    }

    @Override
    protected void reset() {
        super.reset();
        this.target = null;
        this.targetClass = null;
        this.accessor = null;
        this.type = -1;
        this.equation = null;
        this.path = null;
        this.isFrom = this.isRelative = false;
        this.combinedAttrsCnt = this.waypointsCnt = 0;
        if (this.accessorBuffer.length != combinedAttrsLimit) {
            this.accessorBuffer = new float[combinedAttrsLimit];
        }
        if (this.pathBuffer.length != (2 + waypointsLimit) * combinedAttrsLimit) {
            this.pathBuffer = new float[(2 + waypointsLimit) * combinedAttrsLimit];
        }
    }

    private void setup(Object target, int tweenType, float duration) {
        if (duration < 0.0F) {
            throw new RuntimeException("Duration can't be negative");
        } else {
            this.target = target;
            this.targetClass = target != null ? this.findTargetClass() : null;
            this.type = tweenType;
            this.duration = duration;
        }
    }

    private Class<?> findTargetClass() {
        if (registeredAccessors.containsKey(this.target.getClass())) {
            return this.target.getClass();
        } else if (this.target instanceof TweenAccessor) {
            return this.target.getClass();
        } else {
            Class<?> parentClass = this.target.getClass().getSuperclass();
            while (parentClass != null && !registeredAccessors.containsKey(parentClass)) {
                parentClass = parentClass.getSuperclass();
            }
            return parentClass;
        }
    }

    public Tween ease(TweenEquation easeEquation) {
        this.equation = easeEquation;
        return this;
    }

    public Tween cast(Class<?> targetClass) {
        if (this.isStarted()) {
            throw new RuntimeException("You can't cast the target of a tween once it is started");
        } else {
            this.targetClass = targetClass;
            return this;
        }
    }

    public Tween target(float targetValue) {
        this.targetValues[0] = targetValue;
        return this;
    }

    public Tween target(float targetValue1, float targetValue2) {
        this.targetValues[0] = targetValue1;
        this.targetValues[1] = targetValue2;
        return this;
    }

    public Tween target(float targetValue1, float targetValue2, float targetValue3) {
        this.targetValues[0] = targetValue1;
        this.targetValues[1] = targetValue2;
        this.targetValues[2] = targetValue3;
        return this;
    }

    public Tween target(float... targetValues) {
        if (targetValues.length > combinedAttrsLimit) {
            this.throwCombinedAttrsLimitReached();
        }
        System.arraycopy(targetValues, 0, this.targetValues, 0, targetValues.length);
        return this;
    }

    public Tween targetRelative(float targetValue) {
        this.isRelative = true;
        this.targetValues[0] = this.isInitialized() ? targetValue + this.startValues[0] : targetValue;
        return this;
    }

    public Tween targetRelative(float targetValue1, float targetValue2) {
        this.isRelative = true;
        this.targetValues[0] = this.isInitialized() ? targetValue1 + this.startValues[0] : targetValue1;
        this.targetValues[1] = this.isInitialized() ? targetValue2 + this.startValues[1] : targetValue2;
        return this;
    }

    public Tween targetRelative(float targetValue1, float targetValue2, float targetValue3) {
        this.isRelative = true;
        this.targetValues[0] = this.isInitialized() ? targetValue1 + this.startValues[0] : targetValue1;
        this.targetValues[1] = this.isInitialized() ? targetValue2 + this.startValues[1] : targetValue2;
        this.targetValues[2] = this.isInitialized() ? targetValue3 + this.startValues[2] : targetValue3;
        return this;
    }

    public Tween targetRelative(float... targetValues) {
        if (targetValues.length > combinedAttrsLimit) {
            this.throwCombinedAttrsLimitReached();
        }
        for (int i = 0; i < targetValues.length; i++) {
            this.targetValues[i] = this.isInitialized() ? targetValues[i] + this.startValues[i] : targetValues[i];
        }
        this.isRelative = true;
        return this;
    }

    public Tween waypoint(float targetValue) {
        if (this.waypointsCnt == waypointsLimit) {
            this.throwWaypointsLimitReached();
        }
        this.waypoints[this.waypointsCnt] = targetValue;
        this.waypointsCnt++;
        return this;
    }

    public Tween waypoint(float targetValue1, float targetValue2) {
        if (this.waypointsCnt == waypointsLimit) {
            this.throwWaypointsLimitReached();
        }
        this.waypoints[this.waypointsCnt * 2] = targetValue1;
        this.waypoints[this.waypointsCnt * 2 + 1] = targetValue2;
        this.waypointsCnt++;
        return this;
    }

    public Tween waypoint(float targetValue1, float targetValue2, float targetValue3) {
        if (this.waypointsCnt == waypointsLimit) {
            this.throwWaypointsLimitReached();
        }
        this.waypoints[this.waypointsCnt * 3] = targetValue1;
        this.waypoints[this.waypointsCnt * 3 + 1] = targetValue2;
        this.waypoints[this.waypointsCnt * 3 + 2] = targetValue3;
        this.waypointsCnt++;
        return this;
    }

    public Tween waypoint(float... targetValues) {
        if (this.waypointsCnt == waypointsLimit) {
            this.throwWaypointsLimitReached();
        }
        System.arraycopy(targetValues, 0, this.waypoints, this.waypointsCnt * targetValues.length, targetValues.length);
        this.waypointsCnt++;
        return this;
    }

    public Tween path(TweenPath path) {
        this.path = path;
        return this;
    }

    public Object getTarget() {
        return this.target;
    }

    public int getType() {
        return this.type;
    }

    public TweenEquation getEasing() {
        return this.equation;
    }

    public float[] getTargetValues() {
        return this.targetValues;
    }

    public int getCombinedAttributesCount() {
        return this.combinedAttrsCnt;
    }

    public TweenAccessor<?> getAccessor() {
        return this.accessor;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    public Tween build() {
        if (this.target == null) {
            return this;
        } else {
            this.accessor = (TweenAccessor<Object>) registeredAccessors.get(this.targetClass);
            if (this.accessor == null && this.target instanceof TweenAccessor) {
                this.accessor = (TweenAccessor<Object>) this.target;
            }
            if (this.accessor != null) {
                this.combinedAttrsCnt = this.accessor.getValues(this.target, this.type, this.accessorBuffer);
                if (this.combinedAttrsCnt > combinedAttrsLimit) {
                    this.throwCombinedAttrsLimitReached();
                }
                return this;
            } else {
                throw new RuntimeException("No TweenAccessor was found for the target");
            }
        }
    }

    @Override
    public void free() {
        pool.free(this);
    }

    @Override
    protected void initializeOverride() {
        if (this.target != null) {
            this.accessor.getValues(this.target, this.type, this.startValues);
            for (int i = 0; i < this.combinedAttrsCnt; i++) {
                this.targetValues[i] = this.targetValues[i] + (this.isRelative ? this.startValues[i] : 0.0F);
                for (int ii = 0; ii < this.waypointsCnt; ii++) {
                    this.waypoints[ii * this.combinedAttrsCnt + i] = this.waypoints[ii * this.combinedAttrsCnt + i] + (this.isRelative ? this.startValues[i] : 0.0F);
                }
                if (this.isFrom) {
                    float tmp = this.startValues[i];
                    this.startValues[i] = this.targetValues[i];
                    this.targetValues[i] = tmp;
                }
            }
        }
    }

    @Override
    protected void updateOverride(int step, int lastStep, boolean isIterationStep, float delta) {
        if (this.target != null && this.equation != null) {
            if (!isIterationStep && step > lastStep) {
                this.accessor.setValues(this.target, this.type, this.isReverse(lastStep) ? this.startValues : this.targetValues);
            } else if (!isIterationStep && step < lastStep) {
                this.accessor.setValues(this.target, this.type, this.isReverse(lastStep) ? this.targetValues : this.startValues);
            } else {
                assert isIterationStep;
                assert this.getCurrentTime() >= 0.0F;
                assert this.getCurrentTime() <= this.duration;
                if (this.duration < 1.0E-11F && delta > -1.0E-11F) {
                    this.accessor.setValues(this.target, this.type, this.isReverse(step) ? this.targetValues : this.startValues);
                } else if (this.duration < 1.0E-11F && delta < 1.0E-11F) {
                    this.accessor.setValues(this.target, this.type, this.isReverse(step) ? this.startValues : this.targetValues);
                } else {
                    float time = this.isReverse(step) ? this.duration - this.getCurrentTime() : this.getCurrentTime();
                    float t = this.equation.compute(time / this.duration);
                    if (this.waypointsCnt != 0 && this.path != null) {
                        for (int i = 0; i < this.combinedAttrsCnt; i++) {
                            this.pathBuffer[0] = this.startValues[i];
                            this.pathBuffer[1 + this.waypointsCnt] = this.targetValues[i];
                            for (int ii = 0; ii < this.waypointsCnt; ii++) {
                                this.pathBuffer[ii + 1] = this.waypoints[ii * this.combinedAttrsCnt + i];
                            }
                            this.accessorBuffer[i] = this.path.compute(t, this.pathBuffer, this.waypointsCnt + 2);
                        }
                    } else {
                        for (int i = 0; i < this.combinedAttrsCnt; i++) {
                            this.accessorBuffer[i] = this.startValues[i] + t * (this.targetValues[i] - this.startValues[i]);
                        }
                    }
                    this.accessor.setValues(this.target, this.type, this.accessorBuffer);
                }
            }
        }
    }

    @Override
    protected void forceStartValues() {
        if (this.target != null) {
            this.accessor.setValues(this.target, this.type, this.startValues);
        }
    }

    @Override
    protected void forceEndValues() {
        if (this.target != null) {
            this.accessor.setValues(this.target, this.type, this.targetValues);
        }
    }

    @Override
    protected boolean containsTarget(Object target) {
        return this.target == target;
    }

    @Override
    protected boolean containsTarget(Object target, int tweenType) {
        return this.target == target && this.type == tweenType;
    }

    private void throwCombinedAttrsLimitReached() {
        String msg = "You cannot combine more than " + combinedAttrsLimit + " attributes in a tween. You can raise this limit with Tween.setCombinedAttributesLimit(), which should be called once in application initialization code.";
        throw new RuntimeException(msg);
    }

    private void throwWaypointsLimitReached() {
        String msg = "You cannot add more than " + waypointsLimit + " waypoints to a tween. You can raise this limit with Tween.setWaypointsLimit(), which should be called once in application initialization code.";
        throw new RuntimeException(msg);
    }
}