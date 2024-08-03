package icyllis.modernui.util;

public abstract class Property<T, V> {

    private final String mName;

    private final Class<V> mType;

    public Property(Class<V> type, String name) {
        this.mName = name;
        this.mType = type;
    }

    public abstract void set(T var1, V var2);

    public abstract V get(T var1);

    public String getName() {
        return this.mName;
    }

    public Class<V> getType() {
        return this.mType;
    }
}