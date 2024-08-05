package icyllis.modernui.animation;

@FunctionalInterface
public interface TypeEvaluator<T> {

    T evaluate(float var1, T var2, T var3);
}