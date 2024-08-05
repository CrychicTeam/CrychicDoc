package me.jellysquid.mods.sodium.mixin;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import me.jellysquid.mods.sodium.client.SodiumPreLaunch;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.embeddedt.embeddium.asm.AnnotationProcessingEngine;
import org.embeddedt.embeddium.config.ConfigMigrator;
import org.embeddedt.embeddium.taint.mixin.MixinTaintDetector;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class SodiumMixinPlugin implements IMixinConfigPlugin {

    private static final String MIXIN_PACKAGE_ROOT = "me.jellysquid.mods.sodium.mixin.";

    private final Logger logger = LogManager.getLogger("Embeddium");

    private MixinConfig config;

    public void onLoad(String mixinPackage) {
        try {
            this.config = MixinConfig.load(ConfigMigrator.handleConfigMigration("embeddium-mixins.properties").toFile());
        } catch (Exception var3) {
            throw new RuntimeException("Could not load configuration file for Embeddium", var3);
        }
        this.logger.info("Loaded configuration file for Embeddium: {} options available, {} override(s) found", this.config.getOptionCount(), this.config.getOptionOverrideCount());
        SodiumPreLaunch.onPreLaunch();
        MixinTaintDetector.initialize();
    }

    public String getRefMapperConfig() {
        return null;
    }

    public boolean shouldApplyMixin(String s, String s1) {
        return true;
    }

    private boolean isMixinEnabled(String mixin) {
        MixinOption option = this.config.getEffectiveOptionForMixin(mixin);
        if (option == null) {
            if (!mixin.startsWith("modcompat.")) {
                this.logger.error("No rules matched mixin '{}', treating as foreign and disabling!", mixin);
            }
            return false;
        } else {
            if (option.isOverridden()) {
                String source = "[unknown]";
                if (option.isUserDefined()) {
                    source = "user configuration";
                } else if (option.isModDefined()) {
                    source = "mods [" + String.join(", ", option.getDefiningMods()) + "]";
                }
                if (option.isEnabled()) {
                    this.logger.warn("Force-enabling mixin '{}' as rule '{}' (added by {}) enables it", mixin, option.getName(), source);
                } else {
                    this.logger.warn("Force-disabling mixin '{}' as rule '{}' (added by {}) disables it and children", mixin, option.getName(), source);
                }
            }
            return option.isEnabled();
        }
    }

    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    private static String mixinClassify(Path baseFolder, Path path) {
        try {
            String className = baseFolder.relativize(path).toString().replace('/', '.');
            return className.substring(0, className.length() - 6);
        } catch (RuntimeException var3) {
            throw new IllegalStateException("Error relativizing " + path + " to " + baseFolder, var3);
        }
    }

    public List<String> getMixins() {
        if (FMLLoader.getDist() != Dist.CLIENT) {
            return null;
        } else {
            ModFileInfo modFileInfo = FMLLoader.getLoadingModList().getModFileById("embeddium");
            if (modFileInfo == null) {
                this.logger.error("Could not find embeddium mod, there is likely a dependency error. Skipping mixin application.");
                return null;
            } else {
                ModFile modFile = modFileInfo.getFile();
                Set<Path> rootPaths = new HashSet();
                for (String basePackage : new String[] { "core", "modcompat" }) {
                    Path mixinPackagePath = modFile.findResource(new String[] { "me", "jellysquid", "mods", "sodium", "mixin", basePackage });
                    if (Files.exists(mixinPackagePath, new LinkOption[0])) {
                        rootPaths.add(mixinPackagePath.getParent().toAbsolutePath());
                    }
                }
                Set<String> possibleMixinClasses = new HashSet();
                for (Path rootPath : rootPaths) {
                    try {
                        Stream<Path> mixinStream = Files.find(rootPath, Integer.MAX_VALUE, (path, attrs) -> attrs.isRegularFile() && path.getFileName().toString().endsWith(".class"), new FileVisitOption[0]);
                        try {
                            mixinStream.map(Path::toAbsolutePath).filter(MixinClassValidator::isMixinClass).map(path -> mixinClassify(rootPath, path)).filter(this::isMixinEnabled).forEach(possibleMixinClasses::add);
                        } catch (Throwable var11) {
                            if (mixinStream != null) {
                                try {
                                    mixinStream.close();
                                } catch (Throwable var10) {
                                    var11.addSuppressed(var10);
                                }
                            }
                            throw var11;
                        }
                        if (mixinStream != null) {
                            mixinStream.close();
                        }
                    } catch (IOException var12) {
                        var12.printStackTrace();
                    }
                }
                return new ArrayList(possibleMixinClasses);
            }
        }
    }

    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (targetClassName.startsWith("org.embeddedt.embeddium.") || targetClassName.startsWith("me.jellysquid.mods.sodium.")) {
            AnnotationProcessingEngine.processClass(targetClass);
        }
    }
}