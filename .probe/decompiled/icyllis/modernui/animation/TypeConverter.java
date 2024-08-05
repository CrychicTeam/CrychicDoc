package icyllis.modernui.animation;

@FunctionalInterface
public interface TypeConverter<T, V> {

    V convert(T var1);
}