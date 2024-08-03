package me.jellysquid.mods.sodium.client.platform.windows.api.version;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import me.jellysquid.mods.sodium.client.platform.windows.api.Kernel32;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.JNI;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.SharedLibrary;

public class Version {

    private static final SharedLibrary LIBRARY = APIUtil.apiCreateLibrary("version");

    private static final long PFN_GetFileVersionInfoSizeW = APIUtil.apiGetFunctionAddress(LIBRARY, "GetFileVersionInfoSizeW");

    private static final long PFN_GetFileVersionInfoW = APIUtil.apiGetFunctionAddress(LIBRARY, "GetFileVersionInfoW");

    private static final long PFN_VerQueryValueW = APIUtil.apiGetFunctionAddress(LIBRARY, "VerQueryValueW");

    @Nullable
    static QueryResult query(ByteBuffer pBlock, String subBlock) {
        MemoryStack stack = MemoryStack.stackPush();
        QueryResult var10;
        label43: {
            try {
                ByteBuffer pSubBlock = stack.malloc(16, MemoryUtil.memLengthUTF16(subBlock, true));
                MemoryUtil.memUTF16(subBlock, true, pSubBlock);
                PointerBuffer pBuffer = stack.callocPointer(1);
                IntBuffer pLen = stack.callocInt(1);
                int result = JNI.callPPPPI(MemoryUtil.memAddress(pBlock), MemoryUtil.memAddress(pSubBlock), MemoryUtil.memAddress(pBuffer), MemoryUtil.memAddress(pLen), PFN_VerQueryValueW);
                if (result == 0) {
                    var10 = null;
                    break label43;
                }
                var10 = new QueryResult(pBuffer.get(), pLen.get());
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
            return var10;
        }
        if (stack != null) {
            stack.close();
        }
        return var10;
    }

    @Nullable
    public static VersionInfo getModuleFileVersion(String filename) {
        ByteBuffer lptstrFilename = MemoryUtil.memAlignedAlloc(16, MemoryUtil.memLengthUTF16(filename, true));
        try {
            MemoryStack stack = MemoryStack.stackPush();
            Object var16;
            label114: {
                VersionInfo var7;
                try {
                    MemoryUtil.memUTF16(filename, true, lptstrFilename);
                    IntBuffer lpdwHandle = stack.callocInt(1);
                    int versionInfoLength = JNI.callPPI(MemoryUtil.memAddress(lptstrFilename), MemoryUtil.memAddress(lpdwHandle), PFN_GetFileVersionInfoSizeW);
                    if (versionInfoLength == 0) {
                        int error = Kernel32.getLastError();
                        switch(error) {
                            case 1812:
                            case 1813:
                                var16 = null;
                                break label114;
                            default:
                                throw new RuntimeException("GetFileVersionInfoSizeW failed, error=" + error);
                        }
                    }
                    VersionInfo versionInfo = VersionInfo.allocate(versionInfoLength);
                    int result = JNI.callPPI(MemoryUtil.memAddress(lptstrFilename), lpdwHandle.get(), versionInfoLength, versionInfo.address(), PFN_GetFileVersionInfoW);
                    if (result == 0) {
                        versionInfo.close();
                        throw new RuntimeException("GetFileVersionInfoW failed, error=" + Kernel32.getLastError());
                    }
                    var7 = versionInfo;
                } catch (Throwable var13) {
                    if (stack != null) {
                        try {
                            stack.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                    }
                    throw var13;
                }
                if (stack != null) {
                    stack.close();
                }
                return var7;
            }
            if (stack != null) {
                stack.close();
            }
            return (VersionInfo) var16;
        } finally {
            MemoryUtil.memAlignedFree(lptstrFilename);
        }
    }
}