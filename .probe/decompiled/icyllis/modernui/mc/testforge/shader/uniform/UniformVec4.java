package icyllis.modernui.mc.testforge.shader.uniform;

import icyllis.modernui.mc.testforge.shader.ShaderUniform;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

@Deprecated
public class UniformVec4 extends ShaderUniform<Vector4f> {

    public UniformVec4(int location) {
        super(location);
    }

    public void load(Vector4f data) {
        if (this.location != -1) {
            GL20.glUniform4f(this.location, data.x(), data.y(), data.z(), data.w());
        }
    }
}