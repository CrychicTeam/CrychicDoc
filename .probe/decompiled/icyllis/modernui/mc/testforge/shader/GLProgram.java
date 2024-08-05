package icyllis.modernui.mc.testforge.shader;

import icyllis.arc3d.opengl.GLCore;

@Deprecated
public class GLProgram {

    int mProgram;

    public void use() {
        GLCore.glUseProgram(this.mProgram);
    }

    public final int get() {
        return this.mProgram;
    }
}