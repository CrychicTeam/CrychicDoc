package me.jellysquid.mods.sodium.client.compat.modernui;

import java.lang.reflect.Method;
import java.util.stream.Stream;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import net.minecraft.client.Minecraft;

public class MuiGuiScaleHook {

    private static final Method calcGuiScalesMethod = (Method) Stream.of("icyllis.modernui.forge.MForgeCompat", "icyllis.modernui.forge.MuiForgeApi").flatMap(clzName -> {
        try {
            return Stream.of(Class.forName(clzName));
        } catch (Throwable var2) {
            return Stream.of();
        }
    }).flatMap(clz -> {
        try {
            Method m = clz.getDeclaredMethod("calcGuiScales");
            m.setAccessible(true);
            return Stream.of(m);
        } catch (Throwable var2) {
            return Stream.of();
        }
    }).findFirst().orElse(null);

    public static int getMaxGuiScale() {
        if (calcGuiScalesMethod != null) {
            try {
                return (Integer) calcGuiScalesMethod.invoke(null) & 15;
            } catch (Throwable var1) {
                var1.printStackTrace();
            }
        }
        return Minecraft.getInstance().getWindow().calculateScale(0, Minecraft.getInstance().isEnforceUnicode());
    }

    static {
        if (calcGuiScalesMethod != null) {
            SodiumClientMod.logger().info("Found ModernUI GUI scale hook");
        }
    }
}