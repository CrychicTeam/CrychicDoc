package icyllis.modernui.mc.testforge.shader;

@Deprecated
public abstract class ShaderUniform<T> {

    protected final int location;

    public ShaderUniform(int location) {
        this.location = location;
    }

    public abstract void load(T var1);
}