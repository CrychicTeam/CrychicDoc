package icyllis.modernui.animation;

import icyllis.modernui.util.FloatProperty;
import icyllis.modernui.util.IntProperty;
import icyllis.modernui.util.Property;
import javax.annotation.Nonnull;

public sealed class PropertyValuesHolder implements Cloneable permits PropertyValuesHolder.IntPropertyValuesHolder, PropertyValuesHolder.FloatPropertyValuesHolder {

    Property mProperty;

    Keyframes mKeyframes;

    private Object mAnimatedValue;

    private TypeEvaluator mEvaluator;

    private TypeConverter mConverter;

    private PropertyValuesHolder() {
    }

    private PropertyValuesHolder(Property<?, ?> property) {
        this.mProperty = property;
    }

    @Nonnull
    public static PropertyValuesHolder ofInt(@Nonnull int... values) {
        return new PropertyValuesHolder.IntPropertyValuesHolder(values);
    }

    @Nonnull
    public static PropertyValuesHolder ofInt(IntProperty<?> property, @Nonnull int... values) {
        return new PropertyValuesHolder.IntPropertyValuesHolder(property, values);
    }

    @Nonnull
    public static PropertyValuesHolder ofFloat(@Nonnull float... values) {
        return new PropertyValuesHolder.FloatPropertyValuesHolder(values);
    }

    @Nonnull
    public static PropertyValuesHolder ofFloat(FloatProperty<?> property, @Nonnull float... values) {
        return new PropertyValuesHolder.FloatPropertyValuesHolder(property, values);
    }

    @Nonnull
    @SafeVarargs
    public static <V> PropertyValuesHolder ofObject(TypeEvaluator<V> evaluator, @Nonnull V... values) {
        PropertyValuesHolder pvh = new PropertyValuesHolder();
        pvh.setObjectValues(values);
        pvh.setEvaluator(evaluator);
        return pvh;
    }

    @Nonnull
    @SafeVarargs
    public static <V> PropertyValuesHolder ofObject(Property<?, V> property, TypeEvaluator<V> evaluator, @Nonnull V... values) {
        PropertyValuesHolder pvh = new PropertyValuesHolder(property);
        pvh.setObjectValues(values);
        pvh.setEvaluator(evaluator);
        return pvh;
    }

    @Nonnull
    @SafeVarargs
    public static <V, P> PropertyValuesHolder ofObject(Property<?, P> property, TypeConverter<V, P> converter, TypeEvaluator<V> evaluator, @Nonnull V... values) {
        PropertyValuesHolder pvh = new PropertyValuesHolder(property);
        pvh.setConverter(converter);
        pvh.setObjectValues(values);
        pvh.setEvaluator(evaluator);
        return pvh;
    }

    @Nonnull
    public static PropertyValuesHolder ofKeyframe(@Nonnull Keyframe... values) {
        Keyframes keyframes = KeyframeSet.ofKeyframe(values);
        if (keyframes instanceof Keyframes.IntKeyframes) {
            return new PropertyValuesHolder.IntPropertyValuesHolder((Keyframes.IntKeyframes) keyframes);
        } else if (keyframes instanceof Keyframes.FloatKeyframes) {
            return new PropertyValuesHolder.FloatPropertyValuesHolder((Keyframes.FloatKeyframes) keyframes);
        } else {
            PropertyValuesHolder pvh = new PropertyValuesHolder();
            pvh.mKeyframes = keyframes;
            return pvh;
        }
    }

    @Nonnull
    public static PropertyValuesHolder ofKeyframe(Property<?, ?> property, @Nonnull Keyframe... values) {
        PropertyValuesHolder pvh = new PropertyValuesHolder(property);
        pvh.setKeyframes(values);
        return pvh;
    }

    @Nonnull
    public static <P> PropertyValuesHolder ofKeyframe(Property<?, P> property, TypeConverter<?, P> converter, @Nonnull Keyframe... values) {
        PropertyValuesHolder pvh = new PropertyValuesHolder(property);
        pvh.setConverter(converter);
        pvh.setKeyframes(values);
        return pvh;
    }

    @Nonnull
    public static PropertyValuesHolder ofKeyframe(IntProperty<?> property, @Nonnull Keyframe... values) {
        Keyframes keyframes = KeyframeSet.ofKeyframe(values);
        if (keyframes instanceof Keyframes.IntKeyframes) {
            return new PropertyValuesHolder.IntPropertyValuesHolder(property, (Keyframes.IntKeyframes) keyframes);
        } else {
            throw new IllegalArgumentException("Some keyframes are not int keyframes");
        }
    }

    @Nonnull
    public static PropertyValuesHolder ofKeyframe(FloatProperty<?> property, @Nonnull Keyframe... values) {
        Keyframes keyframes = KeyframeSet.ofKeyframe(values);
        if (keyframes instanceof Keyframes.FloatKeyframes) {
            return new PropertyValuesHolder.FloatPropertyValuesHolder(property, (Keyframes.FloatKeyframes) keyframes);
        } else {
            throw new IllegalArgumentException("Some keyframes are not float keyframes");
        }
    }

    public void setIntValues(@Nonnull int... values) {
        throw new UnsupportedOperationException();
    }

    public void setFloatValues(@Nonnull float... values) {
        throw new UnsupportedOperationException();
    }

    public void setKeyframes(@Nonnull Keyframe... values) {
        this.mKeyframes = KeyframeSet.ofKeyframe(values);
    }

    public void setObjectValues(@Nonnull Object... values) {
        this.mKeyframes = KeyframeSet.ofObject(values);
        if (this.mEvaluator != null) {
            this.mKeyframes.setEvaluator(this.mEvaluator);
        }
    }

    public void setConverter(TypeConverter<?, ?> converter) {
        this.mConverter = converter;
    }

    void setupSetterAndGetter(@Nonnull Object target) {
        if (this.mProperty != null) {
            Object testValue = null;
            Keyframe[] keyframes = this.mKeyframes.getKeyframes();
            int count = keyframes == null ? 0 : keyframes.length;
            for (int i = 0; i < count; i++) {
                Keyframe kf = keyframes[i];
                if (!kf.hasValue() || kf.mValueWasSetOnStart) {
                    if (testValue == null) {
                        testValue = this.convertBack(this.mProperty.get(target));
                    }
                    kf.setValue(testValue);
                    kf.mValueWasSetOnStart = true;
                }
            }
        }
    }

    private Object convertBack(Object value) {
        if (this.mConverter != null) {
            if (!(this.mConverter instanceof BidirectionalTypeConverter)) {
                throw new IllegalArgumentException("Converter " + this.mConverter.getClass().getName() + " must be a BidirectionalTypeConverter");
            } else {
                return ((BidirectionalTypeConverter) this.mConverter).convertBack(value);
            }
        } else {
            return value;
        }
    }

    void setupStartValue(@Nonnull Object target) {
        if (this.mProperty != null) {
            Keyframe[] keyframes = this.mKeyframes.getKeyframes();
            if (keyframes.length > 0) {
                Object value = this.convertBack(this.mProperty.get(target));
                keyframes[0].setValue(value);
            }
        }
    }

    void setupEndValue(@Nonnull Object target) {
        if (this.mProperty != null) {
            Keyframe[] keyframes = this.mKeyframes.getKeyframes();
            if (keyframes.length > 0) {
                Object value = this.convertBack(this.mProperty.get(target));
                keyframes[keyframes.length - 1].setValue(value);
            }
        }
    }

    public PropertyValuesHolder clone() {
        try {
            PropertyValuesHolder newPVH = (PropertyValuesHolder) super.clone();
            newPVH.mProperty = this.mProperty;
            newPVH.mKeyframes = this.mKeyframes.copy();
            newPVH.mEvaluator = this.mEvaluator;
            return newPVH;
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2);
        }
    }

    void setAnimatedValue(@Nonnull Object target) {
        if (this.mProperty != null) {
            this.mProperty.set(target, this.getAnimatedValue());
        }
    }

    void init() {
        if (this.mEvaluator != null) {
            this.mKeyframes.setEvaluator(this.mEvaluator);
        }
    }

    public void setEvaluator(TypeEvaluator<?> evaluator) {
        this.mEvaluator = evaluator;
        this.mKeyframes.setEvaluator(this.mEvaluator);
    }

    public void invert() {
        Keyframe[] keyframes = this.mKeyframes.getKeyframes();
        if (keyframes != null) {
            for (Keyframe keyframe : keyframes) {
                keyframe.setFraction(1.0F - keyframe.getFraction());
            }
        }
    }

    void calculateValue(float fraction) {
        Object value = this.mKeyframes.getValue(fraction);
        this.mAnimatedValue = this.mConverter == null ? value : this.mConverter.convert(value);
    }

    public void setProperty(Property<?, ?> property) {
        this.mProperty = property;
    }

    Object getAnimatedValue() {
        return this.mAnimatedValue;
    }

    static final class FloatPropertyValuesHolder extends PropertyValuesHolder {

        private float mFloatAnimatedValue;

        private FloatPropertyValuesHolder(@Nonnull Keyframes.FloatKeyframes keyframes) {
            this.mKeyframes = keyframes;
        }

        private FloatPropertyValuesHolder(FloatProperty<?> property, @Nonnull Keyframes.FloatKeyframes keyframes) {
            super(property);
            this.mKeyframes = keyframes;
        }

        private FloatPropertyValuesHolder(@Nonnull float... values) {
            this.setFloatValues(values);
        }

        private FloatPropertyValuesHolder(FloatProperty<?> property, @Nonnull float... values) {
            super(property);
            this.setFloatValues(values);
        }

        @Override
        public void setProperty(Property<?, ?> property) {
            if (property != null && !(property instanceof FloatProperty)) {
                throw new IllegalArgumentException();
            } else {
                super.setProperty(property);
            }
        }

        @Override
        public void setFloatValues(@Nonnull float... values) {
            this.mKeyframes = KeyframeSet.ofFloat(values);
        }

        @Override
        public void setKeyframes(@Nonnull Keyframe... values) {
            super.setKeyframes(values);
            if (!(this.mKeyframes instanceof Keyframes.FloatKeyframes)) {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public void setObjectValues(@Nonnull Object... values) {
            throw new UnsupportedOperationException();
        }

        @Override
        void setAnimatedValue(@Nonnull Object target) {
            if (this.mProperty != null) {
                ((FloatProperty) this.mProperty).setValue(target, this.mFloatAnimatedValue);
            }
        }

        @Override
        void calculateValue(float fraction) {
            this.mFloatAnimatedValue = ((Keyframes.FloatKeyframes) this.mKeyframes).getFloatValue(fraction);
        }

        @Override
        public void setConverter(TypeConverter<?, ?> converter) {
            throw new UnsupportedOperationException();
        }

        Float getAnimatedValue() {
            return this.mFloatAnimatedValue;
        }

        public PropertyValuesHolder.FloatPropertyValuesHolder clone() {
            return (PropertyValuesHolder.FloatPropertyValuesHolder) super.clone();
        }
    }

    static final class IntPropertyValuesHolder extends PropertyValuesHolder {

        private int mIntAnimatedValue;

        private IntPropertyValuesHolder(@Nonnull Keyframes.IntKeyframes keyframes) {
            this.mKeyframes = keyframes;
        }

        private IntPropertyValuesHolder(IntProperty<?> property, @Nonnull Keyframes.IntKeyframes keyframes) {
            super(property);
            this.mKeyframes = keyframes;
        }

        private IntPropertyValuesHolder(@Nonnull int... values) {
            this.setIntValues(values);
        }

        private IntPropertyValuesHolder(IntProperty<?> property, @Nonnull int... values) {
            super(property);
            this.setIntValues(values);
        }

        @Override
        public void setProperty(Property<?, ?> property) {
            if (property != null && !(property instanceof IntProperty)) {
                throw new IllegalArgumentException();
            } else {
                super.setProperty(property);
            }
        }

        @Override
        public void setIntValues(@Nonnull int... values) {
            this.mKeyframes = KeyframeSet.ofInt(values);
        }

        @Override
        public void setKeyframes(@Nonnull Keyframe... values) {
            super.setKeyframes(values);
            if (!(this.mKeyframes instanceof Keyframes.IntKeyframes)) {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public void setObjectValues(@Nonnull Object... values) {
            throw new UnsupportedOperationException();
        }

        @Override
        void setAnimatedValue(@Nonnull Object target) {
            if (this.mProperty != null) {
                ((IntProperty) this.mProperty).setValue(target, this.mIntAnimatedValue);
            }
        }

        @Override
        void calculateValue(float fraction) {
            this.mIntAnimatedValue = ((Keyframes.IntKeyframes) this.mKeyframes).getIntValue(fraction);
        }

        @Override
        public void setConverter(TypeConverter<?, ?> converter) {
            throw new UnsupportedOperationException();
        }

        Integer getAnimatedValue() {
            return this.mIntAnimatedValue;
        }

        public PropertyValuesHolder.IntPropertyValuesHolder clone() {
            return (PropertyValuesHolder.IntPropertyValuesHolder) super.clone();
        }
    }
}