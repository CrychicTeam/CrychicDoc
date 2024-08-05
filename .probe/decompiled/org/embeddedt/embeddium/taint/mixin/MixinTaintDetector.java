package org.embeddedt.embeddium.taint.mixin;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.IModInfo.ModVersion;
import org.apache.maven.artifact.versioning.Restriction;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

public class MixinTaintDetector implements IExtension {

    private static final List<String> TARGET_PREFIXES = List.of("me.jellysquid.mods.sodium", "org.embeddedt.embeddium");

    private static final MethodHandle GET_MIXINS_ON_CLASS_INFO;

    private static final Logger LOGGER = LoggerFactory.getLogger("Embeddium-MixinTaintDetector");

    private static final MixinTaintDetector.EnforceLevel DEFAULT_ENFORCE_LEVEL = MixinTaintDetector.EnforceLevel.WARN;

    public static final MixinTaintDetector.EnforceLevel ENFORCE_LEVEL;

    private static final Collection<String> MOD_ID_WHITELIST = Set.of("embeddium", "flywheel", "oculus", "iris");

    private static final Map<String, String> packagePrefixToModCache;

    public static void initialize() {
        if (MixinEnvironment.getDefaultEnvironment().getActiveTransformer() instanceof IMixinTransformer transformer && transformer.getExtensions() instanceof Extensions internalExtensions) {
            MixinTaintDetector instance = new MixinTaintDetector();
            try {
                Field extensionsField = internalExtensions.getClass().getDeclaredField("extensions");
                extensionsField.setAccessible(true);
                ((List) extensionsField.get(internalExtensions)).add(instance);
                Field activeExtensionsField = internalExtensions.getClass().getDeclaredField("activeExtensions");
                activeExtensionsField.setAccessible(true);
                List<IExtension> newActiveExtensions = new ArrayList((List) activeExtensionsField.get(internalExtensions));
                newActiveExtensions.add(instance);
                activeExtensionsField.set(internalExtensions, Collections.unmodifiableList(newActiveExtensions));
            } catch (RuntimeException | ReflectiveOperationException var6) {
                var6.printStackTrace();
            }
        }
    }

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    private static boolean isEmbeddiumClass(String className) {
        for (String prefix : TARGET_PREFIXES) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static Set<IMixinInfo> getPotentialMixinsForClass(ClassInfo info) {
        if (GET_MIXINS_ON_CLASS_INFO != null) {
            try {
                return (Set) GET_MIXINS_ON_CLASS_INFO.invokeExact(info);
            } catch (Throwable var2) {
                LOGGER.error("Error encountered getting mixins for class info", var2);
            }
        }
        return Collections.emptySet();
    }

    private static String getModIdByPackage(String pkg) {
        String id = (String) packagePrefixToModCache.get(pkg);
        if (id != null) {
            return id;
        } else {
            id = "[unknown]";
            String[] components = pkg.split("\\.");
            for (ModFileInfo mfi : LoadingModList.get().getModFiles()) {
                if (!mfi.getMods().isEmpty()) {
                    Path path = mfi.getFile().findResource(components);
                    if (path != null && Files.exists(path, new LinkOption[0])) {
                        id = ((IModInfo) mfi.getMods().get(0)).getModId();
                        break;
                    }
                }
            }
            packagePrefixToModCache.put(pkg, id);
            return id;
        }
    }

    private static boolean isDepSingleVersion(ModVersion version) {
        List<Restriction> restrictions = version.getVersionRange().getRestrictions();
        if (restrictions.isEmpty()) {
            return false;
        } else {
            for (Restriction restriction : restrictions) {
                if (restriction.getLowerBound() == null || restriction.getUpperBound() == null || !restriction.getLowerBound().equals(restriction.getUpperBound())) {
                    return false;
                }
            }
            return true;
        }
    }

    private static Map<String, List<IMixinInfo>> filterInvalidMixins(Collection<IMixinInfo> mixins) {
        Map<String, List<IMixinInfo>> map = new HashMap();
        for (IMixinInfo mixin : mixins) {
            String pkg = mixin.getConfig().getMixinPackage();
            String modId = getModIdByPackage(pkg);
            if (!MOD_ID_WHITELIST.contains(modId)) {
                ModFileInfo file = LoadingModList.get().getModFileById(modId);
                if (file != null) {
                    List<? extends ModVersion> deps = ((IModInfo) file.getMods().get(0)).getDependencies();
                    List<? extends ModVersion> embeddiumDeps = deps.stream().filter(dep -> dep.getModId().equals("embeddium")).toList();
                    if (embeddiumDeps.isEmpty() || embeddiumDeps.stream().anyMatch(d -> !isDepSingleVersion(d))) {
                        ((List) map.computeIfAbsent(modId, k -> new ArrayList())).add(mixin);
                    }
                }
            }
        }
        return map;
    }

    public void preApply(ITargetClassContext context) {
        if (ENFORCE_LEVEL != MixinTaintDetector.EnforceLevel.IGNORE) {
            ClassInfo classInfo = context.getClassInfo();
            String name = classInfo.getClassName();
            if (isEmbeddiumClass(name)) {
                Set<IMixinInfo> mixins = getPotentialMixinsForClass(classInfo);
                if (!mixins.isEmpty()) {
                    Map<String, List<IMixinInfo>> illegalMixinMap = filterInvalidMixins(mixins);
                    if (!illegalMixinMap.isEmpty()) {
                        String mixinList = "[" + String.join(", ", illegalMixinMap.keySet()) + "]";
                        LOGGER.warn("Mod(s) {} are modifying Embeddium class {}, which may cause instability. Limited support is provided for such mods, and the ability to do this will be mostly removed in 1.21. It is highly recommended that mods migrate to APIs provided by Embeddium or the modloader (and/or request/contribute their own).", mixinList, name);
                        if (ENFORCE_LEVEL == MixinTaintDetector.EnforceLevel.CRASH) {
                            throw new IllegalStateException("Mods " + mixinList + " are mixing into internal Embeddium class " + name + ", which is no longer permitted");
                        }
                    }
                }
            }
        }
    }

    public void postApply(ITargetClassContext context) {
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }

    static {
        MethodHandle mh = null;
        try {
            Field m = ClassInfo.class.getDeclaredField("mixins");
            m.setAccessible(true);
            mh = MethodHandles.publicLookup().unreflectGetter(m).asType(MethodType.methodType(Set.class, ClassInfo.class));
        } catch (RuntimeException | ReflectiveOperationException var2) {
            var2.printStackTrace();
        }
        GET_MIXINS_ON_CLASS_INFO = mh;
        MixinTaintDetector.EnforceLevel propertyLevel = MixinTaintDetector.EnforceLevel.valueOf(System.getProperty("embeddium.mixinTaintEnforceLevel", DEFAULT_ENFORCE_LEVEL.name()));
        if (propertyLevel.ordinal() < DEFAULT_ENFORCE_LEVEL.ordinal()) {
            propertyLevel = DEFAULT_ENFORCE_LEVEL;
        }
        ENFORCE_LEVEL = propertyLevel;
        packagePrefixToModCache = new ConcurrentHashMap();
    }

    public static enum EnforceLevel {

        IGNORE, WARN, CRASH
    }
}