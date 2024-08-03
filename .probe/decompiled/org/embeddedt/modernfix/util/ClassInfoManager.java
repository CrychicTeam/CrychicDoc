package org.embeddedt.modernfix.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.logging.LoggerAdapterDefault;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.service.MixinServiceAbstract;

public class ClassInfoManager {

    private static boolean hasRun = false;

    private static final List<Runnable> loggersToRestore = new ArrayList();

    public static void clear() {
        if (ModernFixMixinPlugin.instance.isOptionEnabled("perf.clear_mixin_classinfo.ClassInfoManager") && !hasRun) {
            hasRun = true;
            ModernFix.resourceReloadExecutor().execute(ClassInfoManager::doClear);
        }
    }

    private static Field accessible(Field f) {
        f.setAccessible(true);
        return f;
    }

    private static void changeLoggerAndRestoreLater(Map<String, ILogger> map, ILogger newLogger) {
        ILogger oldLogger = (ILogger) map.put("mixin.audit", newLogger);
        loggersToRestore.add((Runnable) () -> map.put("mixin.audit", oldLogger));
    }

    private static void disableLoggers() throws ReflectiveOperationException {
        Field loggersField = accessible(MixinServiceAbstract.class.getDeclaredField("loggers"));
        changeLoggerAndRestoreLater((Map<String, ILogger>) loggersField.get(null), new LoggerAdapterDefault("mixin.audit"));
        Class<?> fabricLogger = null;
        try {
            fabricLogger = Class.forName("net.fabricmc.loader.impl.knot.MixinLogger");
        } catch (Throwable var3) {
            return;
        }
        loggersField = accessible(fabricLogger.getDeclaredField("LOGGER_MAP"));
        changeLoggerAndRestoreLater((Map<String, ILogger>) loggersField.get(null), new LoggerAdapterDefault("mixin.audit"));
    }

    private static void doClear() {
        Map<String, ClassInfo> classInfoCache;
        Field mixinField;
        Field stateField;
        Field classNodeField;
        try {
            disableLoggers();
            Field field = accessible(ClassInfo.class.getDeclaredField("cache"));
            classInfoCache = (Map<String, ClassInfo>) field.get(null);
            mixinField = accessible(ClassInfo.class.getDeclaredField("mixin"));
            Class<?> stateClz = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo$State");
            stateField = accessible(Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo").getDeclaredField("state"));
            classNodeField = accessible(stateClz.getDeclaredField("classNode"));
        } catch (RuntimeException | ReflectiveOperationException var8) {
            var8.printStackTrace();
            return;
        }
        MixinEnvironment.getDefaultEnvironment().audit();
        try {
            ClassNode emptyNode = new ClassNode();
            List<Entry<String, ClassInfo>> entries = new ArrayList(classInfoCache.entrySet());
            entries.stream().filter(entry -> {
                if (((String) entry.getKey()).equals("java/lang/Object")) {
                    return false;
                } else {
                    ClassInfo mixinClz = (ClassInfo) entry.getValue();
                    if (mixinClz == null) {
                        return true;
                    } else {
                        try {
                            if (mixinClz.isMixin()) {
                                IMixinInfo theInfo = (IMixinInfo) mixinField.get(mixinClz);
                                Object state = stateField.get(theInfo);
                                if (state != null) {
                                    classNodeField.set(state, emptyNode);
                                }
                            }
                        } catch (RuntimeException | ReflectiveOperationException var8x) {
                            var8x.printStackTrace();
                        }
                        return true;
                    }
                }
            }).forEach(entry -> classInfoCache.remove(entry.getKey()));
        } catch (RuntimeException var7) {
            var7.printStackTrace();
        }
        loggersToRestore.forEach(Runnable::run);
        loggersToRestore.clear();
        ModernFix.LOGGER.warn("Cleared mixin data structures");
    }
}