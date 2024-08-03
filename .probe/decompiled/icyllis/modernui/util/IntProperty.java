package icyllis.modernui.util;

public abstract class IntProperty<T> extends Property<T, Integer> {

    public IntProperty(String name) {
        super(Integer.class, name);
    }

    public abstract void setValue(T var1, int var2);

    @Deprecated
    public final void set(T object, Integer value) {
        this.setValue(object, value);
    }
}