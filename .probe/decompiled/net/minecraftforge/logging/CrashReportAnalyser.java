package net.minecraftforge.logging;

import com.mojang.logging.LogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;

public final class CrashReportAnalyser {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<String, IModInfo> PACKAGE_MOD_CACHE = new HashMap();

    private static final Map<IModInfo, String[]> SUSPECTED_MODS = new HashMap();

    private CrashReportAnalyser() {
    }

    public static String appendSuspectedMods(Throwable throwable, StackTraceElement[] uncategorizedStackTrace) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            cacheModList();
            analyseCrashReport(throwable, uncategorizedStackTrace);
            buildSuspectedModsSection(stringBuilder);
        } catch (Throwable var4) {
            LOGGER.error("Failed to append suspected mod(s) to crash report!", var4);
        }
        return stringBuilder.toString();
    }

    private static void analyseCrashReport(Throwable throwable, StackTraceElement[] uncategorizedStackTrace) {
        scanThrowable(throwable);
        scanStacktrace(uncategorizedStackTrace);
    }

    private static void buildSuspectedModsSection(StringBuilder stringBuilder) {
        stringBuilder.append("Suspected Mod");
        stringBuilder.append(SUSPECTED_MODS.size() == 1 ? ": " : "s: ");
        if (SUSPECTED_MODS.isEmpty()) {
            stringBuilder.append("NONE\n");
        } else {
            SUSPECTED_MODS.forEach((iModInfo, position) -> {
                stringBuilder.append("\n\t").append(iModInfo.getDisplayName()).append(" (").append(iModInfo.getModId()).append("),").append(" Version: ").append(iModInfo.getVersion());
                iModInfo.getOwningFile().getConfig().getConfigElement(new String[] { "issueTrackerURL" }).ifPresent(issuesLink -> stringBuilder.append("\n\t\t").append("Issue tracker URL: ").append(issuesLink));
                stringBuilder.append("\n\t\t");
                for (String s : position) {
                    stringBuilder.append(s);
                }
                stringBuilder.append("\n");
            });
            SUSPECTED_MODS.clear();
        }
    }

    private static void scanThrowable(Throwable throwable) {
        scanStacktrace(throwable.getStackTrace());
        if (throwable.getCause() != null && throwable.getCause() != throwable) {
            scanThrowable(throwable.getCause());
        }
    }

    private static void scanStacktrace(StackTraceElement[] stackTrace) {
        for (StackTraceElement stackTraceElement : stackTrace) {
            identifyByClass(stackTraceElement);
            identifyByMixins(stackTraceElement);
        }
    }

    private static void cacheModList() {
        ModList modList = ModList.get();
        ModuleLayer gameLayer = FMLLoader.getGameLayer();
        if (modList != null) {
            modList.getMods().forEach(iModInfo -> {
                if (!iModInfo.getModId().equals("forge") && !iModInfo.getModId().equals("minecraft")) {
                    Set<String> packages = new HashSet();
                    gameLayer.findModule(iModInfo.getModId()).ifPresent(module -> packages.addAll(module.getPackages()));
                    packages.forEach(s -> PACKAGE_MOD_CACHE.put(s, iModInfo));
                }
            });
        }
    }

    private static void identifyByClass(StackTraceElement stackTraceElement) {
        blameIfPresent(stackTraceElement);
    }

    private static void identifyByMixins(StackTraceElement stackTraceElement) {
        IMixinInfo mixinInfo = getMixinInfo(stackTraceElement);
        if (mixinInfo != null) {
            String elementAsString = stackTraceElement.toString();
            String mixinClassName = mixinInfo.getClassName();
            List<String> targetClasses = mixinInfo.getTargetClasses();
            blameIfPresent(mixinClassName, "Mixin class: ", mixinClassName, "\n\t\tTarget", (targetClasses.size() == 1 ? ": " + (String) targetClasses.get(0) : "s: " + targetClasses).replaceAll("/", "."), "\n\t\tat ", elementAsString);
        }
    }

    private static void blameIfPresent(StackTraceElement stackTraceElement) {
        blameIfPresent(stackTraceElement.getClassName(), "at ", stackTraceElement.toString());
    }

    private static void blameIfPresent(String className, String... position) {
        String commonPackage = findMatchingPackage(className);
        if (commonPackage != null) {
            SUSPECTED_MODS.putIfAbsent((IModInfo) PACKAGE_MOD_CACHE.get(commonPackage), position);
        }
    }

    @Nullable
    private static String findMatchingPackage(String className) {
        for (String s : PACKAGE_MOD_CACHE.keySet()) {
            if (className.startsWith(s)) {
                return s;
            }
        }
        return null;
    }

    @Nullable
    private static MixinMerged findMixinMerged(StackTraceElement element) {
        try {
            Class<?> clazz = Class.forName(element.getClassName());
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(element.getMethodName())) {
                    MixinMerged mixinMerged = (MixinMerged) method.getAnnotation(MixinMerged.class);
                    if (mixinMerged != null) {
                        return mixinMerged;
                    }
                }
            }
        } catch (NoClassDefFoundError | ClassNotFoundException var8) {
        }
        return null;
    }

    @Nullable
    private static IMixinInfo getMixinInfo(StackTraceElement element) {
        MixinMerged mixinMerged = findMixinMerged(element);
        if (mixinMerged != null) {
            ClassInfo classInfo = ClassInfo.forName(mixinMerged.mixin().replace('.', '/'));
            if (classInfo != null) {
                try {
                    Field mixinField = ClassInfo.class.getDeclaredField("mixin");
                    mixinField.setAccessible(true);
                    return (IMixinInfo) mixinField.get(classInfo);
                } catch (IllegalAccessException | NoSuchFieldException var4) {
                    throw new RuntimeException(var4);
                }
            }
        }
        return null;
    }
}