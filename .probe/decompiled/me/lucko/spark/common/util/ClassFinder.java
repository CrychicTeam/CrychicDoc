package me.lucko.spark.common.util;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;
import me.lucko.spark.lib.bytebuddy.agent.ByteBuddyAgent;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ClassFinder {

    private final Map<String, Class<?>> classes = new HashMap();

    public ClassFinder() {
        Instrumentation instrumentation;
        try {
            instrumentation = ByteBuddyAgent.install();
        } catch (Exception var6) {
            return;
        }
        if (instrumentation != null) {
            for (Class<?> loadedClass : instrumentation.getAllLoadedClasses()) {
                this.classes.put(loadedClass.getName(), loadedClass);
            }
        }
    }

    @Nullable
    public Class<?> findClass(String className) {
        Class<?> clazz = (Class<?>) this.classes.get(className);
        if (clazz != null) {
            return clazz;
        } else {
            try {
                return Class.forName(className);
            } catch (Throwable var4) {
                return null;
            }
        }
    }
}