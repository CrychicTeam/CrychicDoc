package icyllis.modernui.mc.testforge.shader.uniform;

import icyllis.modernui.mc.testforge.shader.ShaderUniform;
import org.lwjgl.opengl.GL20;

@Deprecated
public class UniformFloat extends ShaderUniform<Float> {

    public UniformFloat(int location) {
        super(location);
    }

    public void load(Float data) {
        if (this.location != -1) {
            GL20.glUniform1f(this.location, data);
        }
    }
}