package de.keksuccino.konkrete.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ReflectionHelper {

    public static Field getDeclaredField(Class<?> c, String fieldname) {
        try {
            Field f = c.getDeclaredField(fieldname);
            f.setAccessible(true);
            return f;
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static boolean setField(Field f, Object instance, Object value) {
        try {
            f.set(instance, value);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public static Object setStaticFinalField(Field f, Class<?> c, Object value) {
        Object o = null;
        try {
            f.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(f, f.getModifiers() & -17);
            o = f.get(c);
            f.set(null, value);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return o;
    }

    public static <T> Field findField(Class<? super T> c, String srgName) {
        return ObfuscationReflectionHelper.findField(c, srgName);
    }

    public static <T> Method findMethod(Class<? super T> c, String srgName, Class<?>... parameterTypes) {
        return ObfuscationReflectionHelper.findMethod(c, srgName, parameterTypes);
    }
}