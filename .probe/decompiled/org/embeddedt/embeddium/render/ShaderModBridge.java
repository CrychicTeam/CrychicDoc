package org.embeddedt.embeddium.render;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import net.minecraft.client.gui.screens.Screen;

public class ShaderModBridge {

    private static final MethodHandle SHADERS_ENABLED;

    private static final MethodHandle SHADERS_OPEN_SCREEN;

    private static final MethodHandle NVIDIUM_ENABLED;

    public static boolean isNvidiumEnabled() {
        if (NVIDIUM_ENABLED != null) {
            try {
                return (boolean) NVIDIUM_ENABLED.invokeExact();
            } catch (Throwable var1) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean areShadersEnabled() {
        if (SHADERS_ENABLED != null) {
            try {
                return (boolean) SHADERS_ENABLED.invokeExact();
            } catch (Throwable var1) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isShaderModPresent() {
        return SHADERS_ENABLED != null;
    }

    public static boolean emulateLegacyColorBrightnessFormat() {
        return areShadersEnabled() || isNvidiumEnabled();
    }

    public static Object openShaderScreen(Screen parentScreen) {
        if (SHADERS_OPEN_SCREEN != null) {
            try {
                return (Object) SHADERS_OPEN_SCREEN.invoke(parentScreen);
            } catch (Throwable var2) {
                var2.printStackTrace();
            }
        }
        return null;
    }

    static {
        MethodHandle shadersEnabled = null;
        MethodHandle shaderOpenScreen = null;
        try {
            Class<?> irisApiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            Method instanceGetter = irisApiClass.getDeclaredMethod("getInstance");
            Object irisApiInstance = instanceGetter.invoke(null);
            shadersEnabled = MethodHandles.lookup().unreflect(irisApiClass.getDeclaredMethod("isShaderPackInUse")).bindTo(irisApiInstance);
            shaderOpenScreen = MethodHandles.lookup().unreflect(irisApiClass.getDeclaredMethod("openMainIrisScreenObj", Object.class)).bindTo(irisApiInstance);
        } catch (NoSuchMethodException var6) {
            var6.printStackTrace();
        } catch (Throwable var7) {
        }
        SHADERS_ENABLED = shadersEnabled;
        SHADERS_OPEN_SCREEN = shaderOpenScreen;
        MethodHandle nvidiumEnabled = null;
        try {
            Class<?> nvidiumClass = Class.forName("me.cortex.nvidium.Nvidium");
            nvidiumEnabled = MethodHandles.lookup().findStaticGetter(nvidiumClass, "IS_ENABLED", boolean.class);
        } catch (Throwable var5) {
        }
        NVIDIUM_ENABLED = nvidiumEnabled;
    }
}