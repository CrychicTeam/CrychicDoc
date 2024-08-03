package com.velocitypowered.natives.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.function.BooleanSupplier;

public class NativeConstraints {

    private static final boolean NATIVES_ENABLED = !Boolean.getBoolean("velocity.natives-disabled");

    private static final boolean IS_AMD64;

    private static final boolean IS_AARCH64;

    private static final boolean CAN_GET_MEMORYADDRESS;

    static final BooleanSupplier NATIVE_BASE;

    static final BooleanSupplier LINUX_X86_64;

    static final BooleanSupplier LINUX_AARCH64;

    static final BooleanSupplier MACOS_AARCH64;

    static {
        ByteBuf test = Unpooled.directBuffer();
        try {
            CAN_GET_MEMORYADDRESS = test.hasMemoryAddress();
        } finally {
            test.release();
        }
        String osArch = System.getProperty("os.arch", "");
        IS_AMD64 = osArch.equals("amd64") || osArch.equals("x86_64");
        IS_AARCH64 = osArch.equals("aarch64") || osArch.equals("arm64");
        NATIVE_BASE = () -> NATIVES_ENABLED && CAN_GET_MEMORYADDRESS;
        LINUX_X86_64 = () -> NATIVE_BASE.getAsBoolean() && System.getProperty("os.name", "").equalsIgnoreCase("Linux") && IS_AMD64;
        LINUX_AARCH64 = () -> NATIVE_BASE.getAsBoolean() && System.getProperty("os.name", "").equalsIgnoreCase("Linux") && IS_AARCH64;
        MACOS_AARCH64 = () -> NATIVE_BASE.getAsBoolean() && System.getProperty("os.name", "").equalsIgnoreCase("Mac OS X") && IS_AARCH64;
    }
}