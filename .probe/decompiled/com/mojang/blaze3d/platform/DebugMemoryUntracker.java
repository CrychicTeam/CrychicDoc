package com.mojang.blaze3d.platform;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import org.lwjgl.system.Pointer;

public class DebugMemoryUntracker {

    @Nullable
    private static final MethodHandle UNTRACK = GLX.make(() -> {
        try {
            Lookup $$0 = MethodHandles.lookup();
            Class<?> $$1 = Class.forName("org.lwjgl.system.MemoryManage$DebugAllocator");
            Method $$2 = $$1.getDeclaredMethod("untrack", long.class);
            $$2.setAccessible(true);
            Field $$3 = Class.forName("org.lwjgl.system.MemoryUtil$LazyInit").getDeclaredField("ALLOCATOR");
            $$3.setAccessible(true);
            Object $$4 = $$3.get(null);
            return $$1.isInstance($$4) ? $$0.unreflect($$2) : null;
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | ClassNotFoundException var5) {
            throw new RuntimeException(var5);
        }
    });

    public static void untrack(long long0) {
        if (UNTRACK != null) {
            try {
                UNTRACK.invoke(long0);
            } catch (Throwable var3) {
                throw new RuntimeException(var3);
            }
        }
    }

    public static void untrack(Pointer pointer0) {
        untrack(pointer0.address());
    }
}