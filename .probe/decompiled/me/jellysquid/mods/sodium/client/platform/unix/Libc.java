package me.jellysquid.mods.sodium.client.platform.unix;

import java.nio.ByteBuffer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.JNI;
import org.lwjgl.system.Library;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.SharedLibrary;

public class Libc {

    private static final SharedLibrary LIBRARY = Library.loadNative("me.jellyquid.mods.sodium", "libc.so.6");

    private static final long PFN_setenv = APIUtil.apiGetFunctionAddress(LIBRARY, "setenv");

    public static void setEnvironmentVariable(String name, @Nullable String value) {
        MemoryStack stack = MemoryStack.stackPush();
        try {
            ByteBuffer nameBuf = stack.UTF8(name);
            ByteBuffer valueBuf = value != null ? stack.UTF8(value) : null;
            JNI.callPPI(MemoryUtil.memAddress(nameBuf), MemoryUtil.memAddressSafe(valueBuf), 1, PFN_setenv);
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