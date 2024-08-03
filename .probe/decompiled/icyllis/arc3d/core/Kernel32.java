package icyllis.arc3d.core;

import java.nio.IntBuffer;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.Checks;
import org.lwjgl.system.JNI;
import org.lwjgl.system.Library;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.SharedLibrary;

public class Kernel32 {

    private static final SharedLibrary KERNEL32 = Library.loadNative(Kernel32.class, "org.lwjgl", "kernel32");

    public static final int HANDLE_FLAG_INHERIT = 1;

    public static final int HANDLE_FLAG_PROTECT_FROM_CLOSE = 2;

    public static SharedLibrary getLibrary() {
        return KERNEL32;
    }

    @NativeType("BOOL")
    public static boolean CloseHandle(@NativeType("HANDLE") long hObject) {
        long __functionAddress = Kernel32.Functions.CloseHandle;
        return JNI.callPI(hObject, __functionAddress) != 0;
    }

    @NativeType("BOOL")
    public static boolean GetHandleInformation(@NativeType("HANDLE") long hObject, @NativeType("LPDWORD") IntBuffer lpdwFlags) {
        long __functionAddress = Kernel32.Functions.GetHandleInformation;
        if (Checks.CHECKS) {
            Checks.checkSafe(lpdwFlags, 1);
        }
        return JNI.callPPI(hObject, MemoryUtil.memAddressSafe(lpdwFlags), __functionAddress) != 0;
    }

    @NativeType("DWORD")
    public static int GetLastError() {
        long __functionAddress = Kernel32.Functions.GetLastError;
        return JNI.callI(__functionAddress);
    }

    @NativeType("DLL_DIRECTORY_COOKIE")
    public static long AddDllDirectory(@NativeType("PCWSTR") String NewDirectory) {
        long __functionAddress = Kernel32.Functions.AddDllDirectory;
        MemoryStack stack = MemoryStack.stackPush();
        long var4;
        try {
            stack.nUTF16Safe(NewDirectory, true);
            var4 = JNI.callPP(NewDirectory == null ? 0L : stack.getPointerAddress(), __functionAddress);
        } catch (Throwable var7) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }
            throw var7;
        }
        if (stack != null) {
            stack.close();
        }
        return var4;
    }

    public static final class Functions {

        public static final long CloseHandle = APIUtil.apiGetFunctionAddress(Kernel32.KERNEL32, "CloseHandle");

        public static final long GetHandleInformation = APIUtil.apiGetFunctionAddress(Kernel32.KERNEL32, "GetHandleInformation");

        public static final long GetLastError = APIUtil.apiGetFunctionAddress(Kernel32.KERNEL32, "GetLastError");

        public static final long AddDllDirectory = APIUtil.apiGetFunctionAddress(Kernel32.KERNEL32, "AddDllDirectory");

        private Functions() {
        }
    }
}