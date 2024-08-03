package com.mojang.blaze3d.platform;

import com.google.common.base.Charsets;
import java.nio.ByteBuffer;
import net.minecraft.util.StringDecomposer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.system.MemoryUtil;

public class ClipboardManager {

    public static final int FORMAT_UNAVAILABLE = 65545;

    private final ByteBuffer clipboardScratchBuffer = BufferUtils.createByteBuffer(8192);

    public String getClipboard(long long0, GLFWErrorCallbackI gLFWErrorCallbackI1) {
        GLFWErrorCallback $$2 = GLFW.glfwSetErrorCallback(gLFWErrorCallbackI1);
        String $$3 = GLFW.glfwGetClipboardString(long0);
        $$3 = $$3 != null ? StringDecomposer.filterBrokenSurrogates($$3) : "";
        GLFWErrorCallback $$4 = GLFW.glfwSetErrorCallback($$2);
        if ($$4 != null) {
            $$4.free();
        }
        return $$3;
    }

    private static void pushClipboard(long long0, ByteBuffer byteBuffer1, byte[] byte2) {
        byteBuffer1.clear();
        byteBuffer1.put(byte2);
        byteBuffer1.put((byte) 0);
        byteBuffer1.flip();
        GLFW.glfwSetClipboardString(long0, byteBuffer1);
    }

    public void setClipboard(long long0, String string1) {
        byte[] $$2 = string1.getBytes(Charsets.UTF_8);
        int $$3 = $$2.length + 1;
        if ($$3 < this.clipboardScratchBuffer.capacity()) {
            pushClipboard(long0, this.clipboardScratchBuffer, $$2);
        } else {
            ByteBuffer $$4 = MemoryUtil.memAlloc($$3);
            try {
                pushClipboard(long0, $$4, $$2);
            } finally {
                MemoryUtil.memFree($$4);
            }
        }
    }
}