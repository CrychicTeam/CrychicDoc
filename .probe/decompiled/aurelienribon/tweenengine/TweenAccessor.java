package aurelienribon.tweenengine;

public interface TweenAccessor<T> {

    int getValues(T var1, int var2, float[] var3);

    void setValues(T var1, int var2, float[] var3);
}