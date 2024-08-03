package org.embeddedt.modernfix.forge.config;

import com.electronwill.nightconfig.core.file.FileWatcher;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;
import org.embeddedt.modernfix.util.CommonModUtil;

public class NightConfigFixer {

    public static final LinkedHashSet<Runnable> configsToReload = new LinkedHashSet();

    private static boolean couldShowMessage = true;

    public static void monitorFileWatcher() {
        if (ModernFixMixinPlugin.instance.isOptionEnabled("bugfix.fix_config_crashes.NightConfigFixerMixin")) {
            CommonModUtil.runWithoutCrash(() -> {
                FileWatcher watcher = FileWatcher.defaultInstance();
                Field field = FileWatcher.class.getDeclaredField("watchedFiles");
                field.setAccessible(true);
                ConcurrentHashMap<Path, ?> theMap = (ConcurrentHashMap<Path, ?>) field.get(watcher);
                field.set(watcher, new NightConfigFixer.MonitoringMap(theMap));
                ModernFixMixinPlugin.instance.logger.info("Applied Forge config corruption patch");
            }, "replacing Night Config watchedFiles map");
        }
    }

    public static void runReloads() {
        List<Runnable> runnablesToRun;
        synchronized (configsToReload) {
            runnablesToRun = new ArrayList(configsToReload);
            configsToReload.clear();
        }
        for (Runnable r : runnablesToRun) {
            try {
                r.run();
            } catch (RuntimeException var4) {
                var4.printStackTrace();
            }
        }
        ModernFix.LOGGER.info("Processed {} config reloads", runnablesToRun.size());
        couldShowMessage = true;
    }

    private static void triggerConfigMessage() {
    }

    static class MonitoringConfigTracker implements Runnable {

        private final Runnable configTracker;

        MonitoringConfigTracker(Runnable r) {
            this.configTracker = r;
        }

        public void run() {
            synchronized (NightConfigFixer.configsToReload) {
                if (FMLLoader.getDist().isClient()) {
                    NightConfigFixer.triggerConfigMessage();
                }
                if (NightConfigFixer.configsToReload.size() == 0) {
                    ModernFixMixinPlugin.instance.logger.info("Please use /{} to reload any changed mod config files", FMLLoader.getDist().isDedicatedServer() ? "mfsrc" : "mfrc");
                }
                NightConfigFixer.configsToReload.add(this.configTracker);
            }
        }
    }

    static class MonitoringMap extends ConcurrentHashMap<Path, Object> {

        private static final Class<?> WATCHED_FILE = (Class<?>) LamdbaExceptionUtils.uncheck(() -> Class.forName("com.electronwill.nightconfig.core.file.FileWatcher$WatchedFile"));

        private static final Field CHANGE_HANDLER = ObfuscationReflectionHelper.findField(WATCHED_FILE, "changeHandler");

        public MonitoringMap(ConcurrentHashMap<Path, ?> oldMap) {
            super(oldMap);
        }

        public Object computeIfAbsent(Path key, Function<? super Path, ?> mappingFunction) {
            return super.computeIfAbsent(key, path -> {
                Object watchedFile = mappingFunction.apply(path);
                try {
                    Runnable changeHandler = (Runnable) CHANGE_HANDLER.get(watchedFile);
                    CHANGE_HANDLER.set(watchedFile, new NightConfigFixer.MonitoringConfigTracker(changeHandler));
                } catch (ReflectiveOperationException var4) {
                    var4.printStackTrace();
                }
                return watchedFile;
            });
        }
    }
}