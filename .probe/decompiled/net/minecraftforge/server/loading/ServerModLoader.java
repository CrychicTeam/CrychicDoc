package net.minecraftforge.server.loading;

import java.io.File;
import java.util.List;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LoadingFailedException;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.ModWorkManager;
import net.minecraftforge.logging.CrashReportExtender;
import net.minecraftforge.server.LanguageHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerModLoader {

    private static final Logger LOGGER = LogManager.getLogger();

    private static boolean hasErrors = false;

    public static void load() {
        LogicalSidedProvider.setServer(() -> {
            throw new IllegalStateException("Unable to access server yet");
        });
        LanguageHook.loadForgeAndMCLangs();
        try {
            ModLoader.get().gatherAndInitializeMods(ModWorkManager.syncExecutor(), ModWorkManager.parallelExecutor(), () -> {
            });
            ModLoader.get().loadMods(ModWorkManager.syncExecutor(), ModWorkManager.parallelExecutor(), () -> {
            });
            ModLoader.get().finishMods(ModWorkManager.syncExecutor(), ModWorkManager.parallelExecutor(), () -> {
            });
        } catch (LoadingFailedException var1) {
            hasErrors = true;
            LanguageHook.loadForgeAndMCLangs();
            CrashReportExtender.dumpModLoadingCrashReport(LOGGER, var1, new File("."));
            throw var1;
        }
        List<ModLoadingWarning> warnings = ModLoader.get().getWarnings();
        if (!warnings.isEmpty()) {
            LOGGER.warn(Logging.LOADING, "Mods loaded with {} warnings", warnings.size());
            warnings.forEach(warning -> LOGGER.warn(Logging.LOADING, warning.formatToString()));
        }
        MinecraftForge.EVENT_BUS.start();
    }

    public static boolean hasErrors() {
        return hasErrors;
    }
}