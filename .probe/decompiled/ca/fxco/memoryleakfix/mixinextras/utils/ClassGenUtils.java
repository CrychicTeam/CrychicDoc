package ca.fxco.memoryleakfix.mixinextras.utils;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import sun.misc.Unsafe;

public class ClassGenUtils {

    private static final ClassGenUtils.Definer DEFINER;

    private static final Map<String, byte[]> DEFINITIONS = new HashMap();

    public static void defineClass(ClassNode node, Lookup scope) {
        ClassWriter writer = new ClassWriter(2);
        node.accept(writer);
        byte[] bytes = writer.toByteArray();
        String name = node.name.replace('/', '.');
        try {
            DEFINER.define(name, bytes, scope);
        } catch (Throwable var6) {
            throw new RuntimeException(String.format("Failed to define class %s from %s! Please report to LlamaLad7!", node.name, scope), var6);
        }
        DEFINITIONS.put(name, bytes);
        MixinInternals.registerClassInfo(node);
        MixinInternals.getExtensions().export(MixinEnvironment.getCurrentEnvironment(), node.name, false, node);
    }

    public static Map<String, byte[]> getDefinitions() {
        return Collections.unmodifiableMap(DEFINITIONS);
    }

    static {
        ClassGenUtils.Definer theDefiner;
        try {
            Method defineClass = Unsafe.class.getMethod("defineClass", String.class, byte[].class, int.class, int.class, ClassLoader.class, ProtectionDomain.class);
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe unsafe = (Unsafe) theUnsafe.get(null);
            theDefiner = (name, bytes, scope) -> defineClass.invoke(unsafe, name, bytes, 0, bytes.length, scope.lookupClass().getClassLoader(), scope.lookupClass().getProtectionDomain());
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException var5) {
            try {
                Method defineClassx = Lookup.class.getMethod("defineClass", byte[].class);
                theDefiner = (name, bytes, scope) -> defineClass.invoke(scope, bytes);
            } catch (NoSuchMethodException var4) {
                RuntimeException e = new RuntimeException("Could not resolve class definer! Please report to LlamaLad7.");
                e.addSuppressed(var5);
                e.addSuppressed(var4);
                throw e;
            }
        }
        DEFINER = theDefiner;
    }

    @FunctionalInterface
    private interface Definer {

        void define(String var1, byte[] var2, Lookup var3) throws Throwable;
    }
}