package org.embeddedt.modernfix.forge.classloading;

import cpw.mods.jarhandling.SecureJar;
import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.NamedPath;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService.ITransformerLoader;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService.Phase;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModValidator;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;
import org.embeddedt.modernfix.util.CommonModUtil;
import org.objectweb.asm.Type;

public class ATInjector {

    public static void injectModATs() {
        CommonModUtil.runWithoutCrash(() -> {
            ModValidator validator = (ModValidator) ObfuscationReflectionHelper.getPrivateValue(FMLLoader.class, null, "modValidator");
            List<ModFile> modFiles = (List<ModFile>) ObfuscationReflectionHelper.getPrivateValue(ModValidator.class, validator, "candidateMods");
            List<Pair<ModFile, Path>> list = (List<Pair<ModFile, Path>>) modFiles.stream().filter(file -> file.getAccessTransformer().isPresent()).map(file -> Pair.of(file, (Path) file.getAccessTransformer().get())).collect(Collectors.toList());
            if (list.size() > 0) {
                ModernFixMixinPlugin.instance.logger.warn("Applying ATs from {} mods despite being in errored state, this might cause a crash!", list.size());
                for (Pair<ModFile, Path> pair : list) {
                    try {
                        FMLLoader.addAccessTransformer((Path) pair.getRight(), (ModFile) pair.getLeft());
                    } catch (RuntimeException var10) {
                        ModernFixMixinPlugin.instance.logger.error("Exception occured applying AT from {}", ((ModFile) pair.getLeft()).getFileName(), var10);
                    }
                }
            }
            try {
                Launcher launcher = Launcher.INSTANCE;
                Field launchPlugins = Launcher.class.getDeclaredField("launchPlugins");
                launchPlugins.setAccessible(true);
                LaunchPluginHandler handler = (LaunchPluginHandler) launchPlugins.get(launcher);
                Field plugins = LaunchPluginHandler.class.getDeclaredField("plugins");
                plugins.setAccessible(true);
                Map<String, ILaunchPluginService> map = (Map<String, ILaunchPluginService>) plugins.get(handler);
                Map<String, ILaunchPluginService> newMap = new HashMap(map);
                ATInjector.NonTransformingLaunchPluginService.class.getName();
                newMap.replaceAll((name, plugin) -> {
                    if (plugin.getClass().getName().startsWith("org.spongepowered.asm.launch.MixinLaunchPlugin")) {
                        ModernFixMixinPlugin.instance.logger.warn("Disabling plugin '{}': {}", name, plugin.getClass().getName());
                        return new ATInjector.NonTransformingLaunchPluginService(plugin);
                    } else {
                        return plugin;
                    }
                });
                plugins.set(handler, newMap);
            } catch (ReflectiveOperationException var9) {
                var9.printStackTrace();
            }
        }, "applying mod ATs in errored state");
    }

    static class NonTransformingLaunchPluginService implements ILaunchPluginService {

        private final ILaunchPluginService delegate;

        private static final EnumSet<Phase> NEVER = EnumSet.noneOf(Phase.class);

        NonTransformingLaunchPluginService(ILaunchPluginService delegate) {
            this.delegate = delegate;
        }

        public String name() {
            return this.delegate.name();
        }

        public EnumSet<Phase> handlesClass(Type classType, boolean isEmpty) {
            return NEVER;
        }

        public void offerResource(Path resource, String name) {
            this.delegate.offerResource(resource, name);
        }

        public void addResources(List<SecureJar> resources) {
            this.delegate.addResources(resources);
        }

        public void initializeLaunch(ITransformerLoader transformerLoader, NamedPath[] specialPaths) {
            this.delegate.initializeLaunch(transformerLoader, specialPaths);
        }

        public <T> T getExtension() {
            return (T) this.delegate.getExtension();
        }

        public void customAuditConsumer(String className, Consumer<String[]> auditDataAcceptor) {
            this.delegate.customAuditConsumer(className, auditDataAcceptor);
        }
    }
}