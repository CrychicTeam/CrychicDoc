package me.jellysquid.mods.sodium.client.gl.shader;

import java.nio.ByteBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

class ShaderWorkarounds {

    static void safeShaderSource(int glId, CharSequence source) {
        MemoryStack stack = MemoryStack.stackPush();
        try {
            ByteBuffer sourceBuffer = MemoryUtil.memUTF8(source, true);
            PointerBuffer pointers = stack.mallocPointer(1);
            pointers.put(sourceBuffer);
            GL20C.nglShaderSource(glId, 1, pointers.address0(), 0L);
            APIUtil.apiArrayFree(pointers.address0(), 1);
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