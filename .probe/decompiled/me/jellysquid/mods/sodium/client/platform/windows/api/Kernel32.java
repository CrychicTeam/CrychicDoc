package me.jellysquid.mods.sodium.client.platform.windows.api;

import java.nio.ByteBuffer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.JNI;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.SharedLibrary;

public class Kernel32 {

    private static final SharedLibrary LIBRARY = APIUtil.apiCreateLibrary("kernel32");

    private static final int MAX_PATH = 32767;

    private static final int GET_MODULE_HANDLE_EX_FLAG_UNCHANGED_REFCOUNT = 1;

    private static final int GET_MODULE_HANDLE_EX_FLAG_FROM_ADDRESS = 4;

    private static final long PFN_GetCommandLineW = APIUtil.apiGetFunctionAddress(LIBRARY, "GetCommandLineW");

    private static final long PFN_SetEnvironmentVariableW = APIUtil.apiGetFunctionAddress(LIBRARY, "SetEnvironmentVariableW");

    private static final long PFN_GetModuleHandleExW = APIUtil.apiGetFunctionAddress(LIBRARY, "GetModuleHandleExW");

    private static final long PFN_GetLastError = APIUtil.apiGetFunctionAddress(LIBRARY, "GetLastError");

    private static final long PFN_GetModuleFileNameW = APIUtil.apiGetFunctionAddress(LIBRARY, "GetModuleFileNameW");

    public static void setEnvironmentVariable(String name, @Nullable String value) {
        MemoryStack stack = MemoryStack.stackPush();
        try {
            ByteBuffer lpNameBuf = stack.malloc(16, MemoryUtil.memLengthUTF16(name, true));
            MemoryUtil.memUTF16(name, true, lpNameBuf);
            ByteBuffer lpValueBuf = null;
            if (value != null) {
                lpValueBuf = stack.malloc(16, MemoryUtil.memLengthUTF16(value, true));
                MemoryUtil.memUTF16(value, true, lpValueBuf);
            }
            JNI.callPPI(MemoryUtil.memAddress0(lpNameBuf), MemoryUtil.memAddressSafe(lpValueBuf), PFN_SetEnvironmentVariableW);
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

    public static long getCommandLine() {
        return JNI.callP(PFN_GetCommandLineW);
    }

    public static long getModuleHandleByNames(String[] names) {
        for (String name : names) {
            long handle = getModuleHandleByName(name);
            if (handle != 0L) {
                return handle;
            }
        }
        throw new RuntimeException("Could not obtain handle of module");
    }

    public static long getModuleHandleByName(String name) {
        MemoryStack stack = MemoryStack.stackPush();
        long var6;
        label47: {
            long error;
            try {
                ByteBuffer lpFunctionNameBuf = stack.malloc(16, MemoryUtil.memLengthUTF16(name, true));
                MemoryUtil.memUTF16(name, true, lpFunctionNameBuf);
                PointerBuffer phModule = stack.callocPointer(1);
                int result = JNI.callPPI(1, MemoryUtil.memAddress(lpFunctionNameBuf), MemoryUtil.memAddress(phModule), PFN_GetModuleHandleExW);
                if (result == 0) {
                    int errorx = getLastError();
                    switch(errorx) {
                        case 126:
                            var6 = 0L;
                            break label47;
                        default:
                            throw new RuntimeException("GetModuleHandleEx failed, error=" + errorx);
                    }
                }
                error = phModule.get(0);
            } catch (Throwable var9) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }
                throw var9;
            }
            if (stack != null) {
                stack.close();
            }
            return error;
        }
        if (stack != null) {
            stack.close();
        }
        return var6;
    }

    public static String getModuleFileName(long phModule) {
        ByteBuffer lpFileName = MemoryUtil.memAlignedAlloc(16, 32767);
        String var4;
        try {
            int length = JNI.callPPI(phModule, MemoryUtil.memAddress(lpFileName), lpFileName.capacity(), PFN_GetModuleFileNameW);
            if (length == 0) {
                throw new RuntimeException("GetModuleFileNameW failed, error=" + getLastError());
            }
            var4 = MemoryUtil.memUTF16(lpFileName, length);
        } finally {
            MemoryUtil.memAlignedFree(lpFileName);
        }
        return var4;
    }

    public static int getLastError() {
        return JNI.callI(PFN_GetLastError);
    }
}