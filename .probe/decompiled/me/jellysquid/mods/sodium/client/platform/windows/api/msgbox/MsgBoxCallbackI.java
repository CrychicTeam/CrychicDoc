package me.jellysquid.mods.sodium.client.platform.windows.api.msgbox;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.CallbackI;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.libffi.FFICIF;
import org.lwjgl.system.libffi.FFIType;
import org.lwjgl.system.libffi.LibFFI;

@FunctionalInterface
@NativeType("MSGBOXCALLBACK")
public interface MsgBoxCallbackI extends CallbackI {

    FFICIF CIF = APIUtil.apiCreateCIF(LibFFI.FFI_DEFAULT_ABI, LibFFI.ffi_type_void, new FFIType[] { LibFFI.ffi_type_pointer });

    @NotNull
    default FFICIF getCallInterface() {
        return CIF;
    }

    default void callback(long ret, long args) {
        this.invoke(MemoryUtil.memGetAddress(MemoryUtil.memGetAddress(args)));
    }

    void invoke(@NativeType("LPHELPINFO *") long var1);
}