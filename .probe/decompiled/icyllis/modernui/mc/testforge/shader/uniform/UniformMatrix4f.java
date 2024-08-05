package icyllis.modernui.mc.testforge.shader.uniform;

import icyllis.arc3d.core.Matrix4;
import icyllis.modernui.mc.testforge.shader.ShaderUniform;
import javax.annotation.Nonnull;

@Deprecated
public class UniformMatrix4f extends ShaderUniform<Matrix4> {

    public UniformMatrix4f(int location) {
        super(location);
    }

    public void load(@Nonnull Matrix4 data) {
        if (this.location != -1) {
        }
    }
}