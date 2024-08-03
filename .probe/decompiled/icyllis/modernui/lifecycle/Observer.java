package icyllis.modernui.lifecycle;

@FunctionalInterface
public interface Observer<T> {

    void onChanged(T var1);
}