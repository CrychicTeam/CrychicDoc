package me.jellysquid.mods.sodium.client.gl.shader.uniform;

import java.nio.FloatBuffer;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL30C;
import org.lwjgl.system.MemoryStack;

public class GlUniformMatrix4f extends GlUniform<Matrix4fc> {

    public GlUniformMatrix4f(int index) {
        super(index);
    }

    public void set(Matrix4fc value) {
        MemoryStack stack = MemoryStack.stackPush();
        try {
            FloatBuffer buf = stack.callocFloat(16);
            value.get(buf);
            GL30C.glUniformMatrix4fv(this.index, false, buf);
        } catch (Throwable var6) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }
            throw var6;
        }
        if (stack != null) {
            stack.close();
        }
    }
}