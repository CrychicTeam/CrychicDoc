package forge.me.thosea.badoptimizations.other;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Config {

    public static final Logger LOGGER = LoggerFactory.getLogger("BadOptimizations");

    public static final File FILE = new File(PlatformMethods.getConfigFolder(), "badoptimizations.txt");

    public static final int CONFIG_VER = 3;

    @Nullable
    public static String error;

    public static boolean enable_lightmap_caching = true;

    public static int lightmap_time_change_needed_for_update = 80;

    public static boolean enable_sky_color_caching = true;

    public static int skycolor_time_change_needed_for_update = 3;

    public static boolean enable_debug_renderer_disable_if_not_needed = true;

    public static boolean enable_particle_manager_optimization = true;

    public static boolean enable_toast_optimizations = true;

    public static boolean enable_sky_angle_caching_in_worldrenderer = true;

    public static boolean enable_entity_renderer_caching = true;

    public static boolean enable_block_entity_renderer_caching = true;

    public static boolean enable_entity_flag_caching = true;

    public static boolean enable_remove_redundant_fov_calculations = true;

    public static boolean enable_remove_tutorial_if_not_demo = true;

    public static boolean show_f3_text = true;

    public static boolean ignore_mod_incompatibilities = false;

    public static boolean log_config = true;

    private Config() {
    }

    public static void load() {
        if (FILE.exists()) {
            LOGGER.info("Loading config from {}", FILE);
            try {
                loadConfig();
            } catch (Exception var2) {
                error = "BadOptimizations failed to load the config,\nand some config options have not been read properly.\nRead the game log for details.";
                LOGGER.error("Failed to load config from " + FILE + ". If you need to, you can delete the file to generate a new one.", var2);
            }
        } else {
            LOGGER.info("Creating config file version {} at {}", 3, FILE);
            try {
                writeConfig();
            } catch (Exception var1) {
                error = "BadOptimizations failed to write the default config.\nRead the game log for details.";
                LOGGER.error("Failed to write default config to " + FILE, var1);
            }
        }
    }

    private static void loadConfig() throws Exception {
        Properties prop = new Properties();
        FileInputStream stream = new FileInputStream(FILE);
        try {
            prop.load(stream);
        } catch (Throwable var5) {
            try {
                stream.close();
            } catch (Throwable var4) {
                var5.addSuppressed(var4);
            }
            throw var5;
        }
        stream.close();
        int ver = num(prop, "config_version");
        if (ver > 3) {
            LOGGER.warn("Config version is newer than supported, this may cause issues (supported: {}, found: {})", 3, ver);
        } else if (ver < 3) {
            LOGGER.info("Upgrading config from version {} to supported version {}", ver, 3);
        } else {
            LOGGER.info("Config version: {}", 3);
        }
        lightmap_time_change_needed_for_update = num(prop, "lightmap_time_change_needed_for_update");
        enable_lightmap_caching = bool(prop, "enable_lightmap_caching") && lightmap_time_change_needed_for_update > 1;
        enable_sky_color_caching = bool(prop, "enable_sky_color_caching");
        skycolor_time_change_needed_for_update = num(prop, "skycolor_time_change_needed_for_update");
        enable_debug_renderer_disable_if_not_needed = bool(prop, "enable_debug_renderer_disable_if_not_needed");
        enable_particle_manager_optimization = bool(prop, "enable_particle_manager_optimization");
        enable_toast_optimizations = bool(prop, "enable_toast_optimizations");
        enable_sky_angle_caching_in_worldrenderer = bool(prop, "enable_sky_angle_caching_in_worldrenderer");
        enable_entity_renderer_caching = bool(prop, "enable_entity_renderer_caching");
        enable_block_entity_renderer_caching = bool(prop, "enable_block_entity_renderer_caching");
        enable_entity_flag_caching = bool(prop, "enable_entity_flag_caching");
        enable_remove_redundant_fov_calculations = bool(prop, "enable_remove_redundant_fov_calculations");
        enable_remove_tutorial_if_not_demo = bool(prop, "enable_remove_tutorial_if_not_demo");
        show_f3_text = bool(prop, "show_f3_text");
        if (ver >= 2) {
            ignore_mod_incompatibilities = bool(prop, "ignore_mod_incompatibilities");
            log_config = bool(prop, "log_config");
        }
        if (ver < 3) {
            writeConfig();
            loadConfig();
        } else {
            if (log_config) {
                LOGGER.info("BadOptimizations config dump:");
                prop.forEach((key, value) -> LOGGER.info("{}: {}", key, value));
            }
            if (!ignore_mod_incompatibilities) {
                if (enable_entity_renderer_caching) {
                    if (PlatformMethods.isModLoaded("twilightforest")) {
                        enable_entity_renderer_caching = false;
                        LOGGER.info("Disabled entity_renderer_caching because Twilight Forest is present");
                    } else if (PlatformMethods.isModLoaded("aquaculture")) {
                        enable_entity_renderer_caching = false;
                        LOGGER.info("Disabled entity_renderer_caching because Aquaculture is present");
                    }
                }
                if (enable_sky_color_caching && PlatformMethods.isModLoaded("polytone")) {
                    enable_sky_color_caching = false;
                    LOGGER.info("Disabled sky_color_caching because Polytone is present");
                }
            }
        }
    }

    private static boolean bool(Properties prop, String name) throws Exception {
        String str = prop.getProperty(name);
        if (str == null) {
            throw new Exception("Config option " + name + " not found.");
        } else if (str.equalsIgnoreCase("true")) {
            return true;
        } else if (str.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new Exception("Config option " + name + " is not \"true\" or \"false\" (\"" + str + "\").");
        }
    }

    private static int num(Properties prop, String name) throws Exception {
        String str = prop.getProperty(name);
        if (str == null) {
            throw new Exception("Config option " + name + " not found.");
        } else {
            int result;
            try {
                result = Integer.parseInt(str);
            } catch (Exception var5) {
                throw new Exception("Config option " + name + " is not a valid number (\"" + str + "\").");
            }
            if (result < 0) {
                throw new Exception("Config option " + name + " is negative (" + str + ")");
            } else {
                return result;
            }
        }
    }

    private static void writeConfig() throws Exception {
        File parent = FILE.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new Exception("Failed to create config directory at " + parent);
        } else {
            String data = "# BadOptimizations configuration\n# Here you can configure stuff, mostly enabling/disabling specific optimizations.\n\n# Whether we should cancel updating the lightmap if not needed.\nenable_lightmap_caching: %s\n# How much the in-game time must change in ticks (1/20th of a second)\n# for the lightmap to immediately update.\n# Higher values will result in less frequent updates\n# to block lighting, but better performance.\n# Values below 2 will disable hte optimization.\nlightmap_time_change_needed_for_update: %s\n\n# Whether the sky's color should be cached unless you're on a biome border.\nenable_sky_color_caching: %s\n# How much the in-game time must change in ticks for the sky color to\n# be recalculated with our own calculation. Higher values will result in\n# the sky updating less frequently, but slightly better performance.\n# Values below 2 will all have the same effect.\nskycolor_time_change_needed_for_update: %s\n\n# Whether we should avoid calling debug renderers\n# and their calculations if there are no debug entries to render\nenable_debug_renderer_disable_if_not_needed: %s\n\n#\n# Micro optimizations\n#\n\n# Whether we should avoid calling the particle manager\n# and its calculations if there are no particles.\nenable_particle_manager_optimization: %s\n# Whether we should avoid calling the toast manager\n# if there are no toasts\nenable_toast_optimizations: %s\n# Whether the result of getSkyAngle should be cached\n# for the entire frame during rendering\nenable_sky_angle_caching_in_worldrenderer: %s\n# Whether entity renderers should be stored directly in EntityType\n# instead of a HashMap.\n# Disable to fix compatibility with Twilight Forest\nenable_entity_renderer_caching: %s\n# Whether block entity renderers should be stored in BlockEntityType\n# instead of a HashMap.\nenable_block_entity_renderer_caching: %s\n# Whether entity flags should be cached instead of\n# calling DataTracker.\n# Also removes the unnecessary thread lock in DataTracker\n# however this is also done by Lithium (they don't conflict, however).\nenable_entity_flag_caching: %s\n# Whether we should avoid calling FOV calculations\n# if the FOV effect scale is zero.\nenable_remove_redundant_fov_calculations: %s\n# Don't tick the tutorial if the game is not in demo mode.\nenable_remove_tutorial_if_not_demo: %s\n\n#\n# Other\n#\n\n# Whether BadOptimizations <version> should be added onto\n# the left text of the F3 menu.\nshow_f3_text: %s\n\n# Some config options will be force-disabled if certain mods are present\n# due to incompatibilities (e.g. entity rendering caching\n# is disabled w/ Twilight Forest).\n# However, if you still want to use the optimizations, you can override it\n# by setting this to true. Beware of crashes. And Herobrine.\nignore_mod_incompatibilities: %s\n\n# Whether to log the entire config into console when booting up.\n# If you plan on reporting an issue, please keep this on.\nlog_config: %s\n\n# Do not change this\nconfig_version: %s\n".formatted(enable_lightmap_caching, lightmap_time_change_needed_for_update, enable_sky_color_caching, skycolor_time_change_needed_for_update, enable_debug_renderer_disable_if_not_needed, enable_particle_manager_optimization, enable_toast_optimizations, enable_sky_angle_caching_in_worldrenderer, enable_entity_renderer_caching, enable_block_entity_renderer_caching, enable_entity_flag_caching, enable_remove_redundant_fov_calculations, enable_remove_tutorial_if_not_demo, show_f3_text, ignore_mod_incompatibilities, log_config, 3);
            if (FILE.exists()) {
                FILE.delete();
            }
            Files.writeString(FILE.toPath(), data, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
        }
    }
}