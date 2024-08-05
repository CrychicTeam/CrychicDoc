package org.embeddedt.modernfix.forge.mixin.bugfix.cofh_core_crash;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import org.embeddedt.modernfix.annotation.RequiresMod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = { "cofh/lib/util/flags/FlagManager" }, remap = false)
@RequiresMod("cofh_core")
public class FlagManagerMixin {

    @Shadow
    @Final
    private static Object2ObjectOpenHashMap<String, ?> FLAGS;

    @Unique
    private static final MethodHandle mfix$getOrCreateFlag;

    @Redirect(method = { "*" }, at = @At(value = "INVOKE", target = "getOrCreateFlag"), require = 0)
    @Coerce
    private Object getFlag(@Coerce Object flagHandler, String flag) {
        if (flagHandler != this) {
            throw new AssertionError("Redirect targeted bad getOrCreateFlag invocation");
        } else {
            synchronized (FLAGS) {
                Object var10000;
                try {
                    var10000 = (Object) mfix$getOrCreateFlag.invoke(this, flag);
                } catch (Throwable var6) {
                    if (var6 instanceof RuntimeException) {
                        throw (RuntimeException) var6;
                    }
                    throw new RuntimeException(var6);
                }
                return var10000;
            }
        }
    }

    static {
        try {
            Method m = MethodHandles.lookup().lookupClass().getDeclaredMethod("getOrCreateFlag", String.class);
            m.setAccessible(true);
            mfix$getOrCreateFlag = MethodHandles.lookup().unreflect(m);
        } catch (ReflectiveOperationException var1) {
            throw new AssertionError(var1);
        }
    }
}