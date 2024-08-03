package icyllis.modernui.util;

public abstract class FloatProperty<T> extends Property<T, Float> {

    public FloatProperty(String name) {
        super(Float.class, name);
    }

    public abstract void setValue(T var1, float var2);

    @Deprecated
    public final void set(T object, Float value) {
        this.setValue(object, value);
    }
}