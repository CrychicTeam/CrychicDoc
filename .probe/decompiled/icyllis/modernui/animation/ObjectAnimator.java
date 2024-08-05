package icyllis.modernui.animation;

import icyllis.modernui.util.FloatProperty;
import icyllis.modernui.util.IntProperty;
import icyllis.modernui.util.Property;
import java.lang.ref.WeakReference;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ObjectAnimator extends ValueAnimator {

    @Nullable
    private WeakReference<Object> mTarget;

    private boolean mAutoCancel = false;

    @Nonnull
    public static <T> ObjectAnimator ofInt(@Nullable T target, @Nonnull IntProperty<T> property, @Nonnull int... values) {
        return ofPropertyValuesHolder(target, PropertyValuesHolder.ofInt(property, values));
    }

    @Nonnull
    public static <T> ObjectAnimator ofArgb(@Nullable T target, @Nonnull IntProperty<T> property, @Nonnull int... values) {
        PropertyValuesHolder pvh = PropertyValuesHolder.ofInt(property, values);
        pvh.setEvaluator(ColorEvaluator.getInstance());
        return ofPropertyValuesHolder(target, pvh);
    }

    @Nonnull
    public static <T> ObjectAnimator ofFloat(@Nullable T target, @Nonnull FloatProperty<T> property, @Nonnull float... values) {
        return ofPropertyValuesHolder(target, PropertyValuesHolder.ofFloat(property, values));
    }

    @Nonnull
    @SafeVarargs
    public static <T, V> ObjectAnimator ofObject(@Nullable T target, @Nonnull Property<T, V> property, @Nonnull TypeEvaluator<V> evaluator, @Nonnull V... values) {
        return ofPropertyValuesHolder(target, PropertyValuesHolder.ofObject(property, evaluator, values));
    }

    @Nonnull
    @SafeVarargs
    public static <T, V, P> ObjectAnimator ofObject(@Nullable T target, @Nonnull Property<T, P> property, @Nonnull TypeConverter<V, P> converter, @Nonnull TypeEvaluator<V> evaluator, @Nonnull V... values) {
        return ofPropertyValuesHolder(target, PropertyValuesHolder.ofObject(property, converter, evaluator, values));
    }

    @Nonnull
    public static ObjectAnimator ofPropertyValuesHolder(@Nullable Object target, @Nonnull PropertyValuesHolder... values) {
        ObjectAnimator anim = new ObjectAnimator();
        anim.setTarget(target);
        anim.setValues(values);
        return anim;
    }

    public void setAutoCancel(boolean cancel) {
        this.mAutoCancel = cancel;
    }

    @Override
    public void start() {
        AnimationHandler.getInstance().autoCancelBasedOn(this);
        super.start();
    }

    public ObjectAnimator clone() {
        return (ObjectAnimator) super.clone();
    }

    @Override
    public void setupStartValues() {
        this.initAnimation();
        Object target = this.getTarget();
        if (target != null) {
            for (PropertyValuesHolder value : this.mValues) {
                value.setupStartValue(target);
            }
        }
    }

    @Override
    public void setupEndValues() {
        this.initAnimation();
        Object target = this.getTarget();
        if (target != null) {
            for (PropertyValuesHolder value : this.mValues) {
                value.setupEndValue(target);
            }
        }
    }

    @Override
    void initAnimation() {
        if (!this.mInitialized) {
            Object target = this.getTarget();
            for (PropertyValuesHolder value : this.mValues) {
                if (target != null) {
                    value.setupSetterAndGetter(target);
                }
                value.init();
            }
            this.mInitialized = true;
        }
    }

    public ObjectAnimator setDuration(long duration) {
        super.setDuration(duration);
        return this;
    }

    @Nullable
    public Object getTarget() {
        return this.mTarget == null ? null : this.mTarget.get();
    }

    @Override
    public void setTarget(@Nullable Object target) {
        Object oldTarget = this.getTarget();
        if (oldTarget != target) {
            if (this.isStarted()) {
                this.cancel();
            }
            this.mTarget = target == null ? null : new WeakReference(target);
            this.mInitialized = false;
        }
    }

    @Override
    void animateValue(float fraction) {
        Object target = this.getTarget();
        if (this.mTarget != null && target == null) {
            this.cancel();
        } else {
            super.animateValue(fraction);
            if (target != null) {
                for (PropertyValuesHolder value : this.mValues) {
                    value.setAnimatedValue(target);
                }
            }
        }
    }

    boolean shouldAutoCancel(AnimationHandler.FrameCallback anim) {
        if (anim == this && this.mAutoCancel) {
            return true;
        } else {
            if (anim instanceof ObjectAnimator it && it.mAutoCancel) {
                PropertyValuesHolder[] itsValues = it.getValues();
                if (it.getTarget() == this.getTarget() && this.mValues.length == itsValues.length) {
                    for (int i = 0; i < this.mValues.length; i++) {
                        if (!Objects.equals(this.mValues[i], itsValues[i])) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }
}