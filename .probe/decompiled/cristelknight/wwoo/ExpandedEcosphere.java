package cristelknight.wwoo;

import cristelknight.wwoo.config.configs.EEConfig;
import cristelknight.wwoo.config.configs.ReplaceBiomesConfig;
import cristelknight.wwoo.utils.BiomeReplace;
import cristelknight.wwoo.utils.Updater;
import cristelknight.wwoo.utils.Util;
import net.cristellib.ModLoadingUtil;
import net.cristellib.builtinpacks.BuiltInDataPacks;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExpandedEcosphere {

    public static final String MODID = "expanded_ecosphere";

    public static final Logger LOGGER = LogManager.getLogger("Expanded Ecosphere");

    public static final String WWOOVersion = EEExpectPlatform.getVersionForMod("expanded_ecosphere");

    private static final Updater updater = new Updater(WWOOVersion);

    public static ExpandedEcosphere.Mode currentMode = Util.getMode(EEConfig.DEFAULT.getConfig().mode());

    public static final String LINK_DC = "https://discord.com/invite/yJng7sC44x";

    public static final String LINK_MODRINTH = "https://modrinth.com/mod/expanded-ecosphere";

    public static final String LINK_CF = "https://www.curseforge.com/minecraft/mc-mods/expanded-ecosphere";

    public static final String backupVersionNumber = "1.20.2";

    public static final String minTerraBlenderVersion = "3.0.0.169";

    public static void init() {
        LOGGER.info("Loading Expanded Ecosphere");
        updater.checkForUpdates();
        ReplaceBiomesConfig config2 = ReplaceBiomesConfig.DEFAULT.getConfig();
        if (config2.enableBiomes() && currentMode.equals(ExpandedEcosphere.Mode.DEFAULT)) {
            BiomeReplace.replace();
        }
        BuiltInDataPacks.registerPack(new EERL("resources/ee_default"), "expanded_ecosphere", Component.literal("Expanded Ecosphere Default World Gen"), () -> currentMode.equals(ExpandedEcosphere.Mode.DEFAULT));
        BuiltInDataPacks.registerPack(new EERL("resources/ee_remove_blobs"), "expanded_ecosphere", Component.literal("Disables granit, etc."), () -> EEConfig.DEFAULT.getConfig().removeOreBlobs());
        BuiltInDataPacks.registerPack(new EERL("resources/ee_force_large_biomes"), "expanded_ecosphere", Component.literal("Forcing LARGE biomes"), () -> EEConfig.DEFAULT.getConfig().forceLargeBiomes());
    }

    public static Updater getUpdater() {
        return updater;
    }

    public static boolean isTerraBlenderLoaded() {
        return ModLoadingUtil.isModLoadedWithVersion("terrablender", "3.0.0.169");
    }

    public static enum Mode {

        COMPATIBLE, DEFAULT
    }
}