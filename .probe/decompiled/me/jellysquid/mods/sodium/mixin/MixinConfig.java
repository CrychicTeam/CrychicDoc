package me.jellysquid.mods.sodium.mixin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MixinConfig {

    private static final Logger LOGGER = LogManager.getLogger("EmbeddiumConfig");

    private static final String JSON_KEY_SODIUM_OPTIONS = "sodium:options";

    private final Map<String, MixinOption> options = new HashMap();

    private MixinConfig() {
        this.addMixinRule("core", true);
        this.addMixinRule("features", true);
        this.addMixinRule("features.gui", true);
        this.addMixinRule("features.gui.hooks", true);
        this.addMixinRule("features.gui.hooks.console", true);
        this.addMixinRule("features.gui.hooks.debug", true);
        this.addMixinRule("features.gui.hooks.settings", true);
        this.addMixinRule("features.gui.screen", true);
        this.addMixinRule("features.model", true);
        this.addMixinRule("features.options", true);
        this.addMixinRule("features.options.overlays", true);
        this.addMixinRule("features.options.render_layers", true);
        this.addMixinRule("features.options.weather", true);
        this.addMixinRule("features.render", true);
        this.addMixinRule("features.render.entity", true);
        this.addMixinRule("features.render.entity.cull", true);
        this.addMixinRule("features.render.entity.fast_render", true);
        this.addMixinRule("features.render.entity.shadow", true);
        this.addMixinRule("features.render.gui", true);
        this.addMixinRule("features.render.gui.debug", true);
        this.addMixinRule("features.render.gui.font", true);
        this.addMixinRule("features.render.gui.outlines", true);
        this.addMixinRule("features.render.immediate", true);
        this.addMixinRule("features.render.immediate.buffer_builder", true);
        this.addMixinRule("features.render.immediate.buffer_builder.fast_delegate", true);
        this.addMixinRule("features.render.immediate.matrix_stack", true);
        this.addMixinRule("features.render.model", true);
        this.addMixinRule("features.render.model.block", true);
        this.addMixinRule("features.render.model.item", true);
        this.addMixinRule("features.render.particle", true);
        this.addMixinRule("features.render.world", true);
        this.addMixinRule("features.render.world.clouds", true);
        this.addMixinRule("features.render.world.sky", true);
        this.addMixinRule("features.shader", true);
        this.addMixinRule("features.shader.uniform", true);
        this.addMixinRule("features.textures", true);
        this.addMixinRule("features.textures.animations", true);
        this.addMixinRule("features.textures.mipmaps", true);
        this.addMixinRule("features.world", true);
        this.addMixinRule("features.world.biome", true);
        this.addMixinRule("features.world.storage", true);
        this.addMixinRule("workarounds", true);
        this.addMixinRule("workarounds.context_creation", true);
        this.addMixinRule("workarounds.event_loop", true);
        Pattern replacePattern = Pattern.compile("[^\\w]");
        if (FMLLoader.getLoadingModList().getErrors().isEmpty()) {
            for (ModInfo modInfo : FMLLoader.getLoadingModList().getMods()) {
                String sanitizedModId = replacePattern.matcher(modInfo.getModId()).replaceAll("_");
                this.addMixinRule("modcompat." + sanitizedModId, true);
            }
        }
        this.applyBuiltInCompatOverrides();
    }

    private static boolean isModLoaded(String modId) {
        return LoadingModList.get().getModFileById(modId) != null;
    }

    private void applyBuiltInCompatOverrides() {
    }

    private void disableIfModPresent(String modId, String option) {
        if (isModLoaded(modId)) {
            this.applyModOverride(modId, "mixin." + option, false);
        }
    }

    private void addMixinRule(String mixin, boolean enabled) {
        String name = getMixinRuleName(mixin);
        if (this.options.putIfAbsent(name, new MixinOption(name, enabled, false)) != null) {
            throw new IllegalStateException("Mixin rule already defined: " + mixin);
        }
    }

    private void readProperties(Properties props) {
        for (Entry<Object, Object> entry : props.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            MixinOption option = (MixinOption) this.options.get(key);
            if (option == null) {
                LOGGER.warn("No configuration key exists with name '{}', ignoring", key);
            } else {
                boolean enabled;
                if (value.equalsIgnoreCase("true")) {
                    enabled = true;
                } else {
                    if (!value.equalsIgnoreCase("false")) {
                        LOGGER.warn("Invalid value '{}' encountered for configuration key '{}', ignoring", value, key);
                        continue;
                    }
                    enabled = false;
                }
                option.setEnabled(enabled, true);
            }
        }
    }

    private void applyModOverrides() {
        for (ModInfo meta : LoadingModList.get().getMods()) {
            meta.getConfigElement(new String[] { "sodium:options" }).ifPresent(overridesObj -> {
                if (overridesObj instanceof Map overrides && overrides.keySet().stream().allMatch(key -> key instanceof String)) {
                    overrides.forEach((key, value) -> this.applyModOverride(meta.getModId(), (String) key, value));
                    return;
                }
                LOGGER.warn("Mod '{}' contains invalid Embeddium option overrides, ignoring", meta.getModId());
            });
        }
    }

    private void applyModOverride(String modid, String name, Object value) {
        MixinOption option = (MixinOption) this.options.get(name);
        if (option == null) {
            LOGGER.warn("Mod '{}' attempted to override option '{}', which doesn't exist, ignoring", modid, name);
        } else if (value instanceof Boolean enabled) {
            if (!enabled && option.isEnabled()) {
                option.clearModsDefiningValue();
            }
            if (!enabled || option.isEnabled() || option.getDefiningMods().isEmpty()) {
                option.addModOverride(enabled, modid);
            }
        } else {
            LOGGER.warn("Mod '{}' attempted to override option '{}' with an invalid value, ignoring", modid, name);
        }
    }

    public MixinOption getEffectiveOptionForMixin(String mixinClassName) {
        int lastSplit = 0;
        MixinOption rule = null;
        int nextSplit;
        while ((nextSplit = mixinClassName.indexOf(46, lastSplit)) != -1) {
            String key = getMixinRuleName(mixinClassName.substring(0, nextSplit));
            MixinOption candidate = (MixinOption) this.options.get(key);
            if (candidate != null) {
                rule = candidate;
                if (!candidate.isEnabled()) {
                    return candidate;
                }
            }
            lastSplit = nextSplit + 1;
        }
        return rule;
    }

    public static MixinConfig load(File file) {
        if (!file.exists()) {
            try {
                writeDefaultConfig(file);
            } catch (IOException var6) {
                LOGGER.warn("Could not write default configuration file", var6);
            }
            MixinConfig config = new MixinConfig();
            config.applyModOverrides();
            return config;
        } else {
            Properties props = new Properties();
            try {
                FileInputStream fin = new FileInputStream(file);
                try {
                    props.load(fin);
                } catch (Throwable var7) {
                    try {
                        fin.close();
                    } catch (Throwable var5) {
                        var7.addSuppressed(var5);
                    }
                    throw var7;
                }
                fin.close();
            } catch (IOException var8) {
                throw new RuntimeException("Could not load config file", var8);
            }
            MixinConfig config = new MixinConfig();
            config.readProperties(props);
            config.applyModOverrides();
            return config;
        }
    }

    private static void writeDefaultConfig(File file) throws IOException {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Could not create parent directories");
            }
        } else if (!dir.isDirectory()) {
            throw new IOException("The parent file is not a directory");
        }
        Writer writer = new FileWriter(file);
        try {
            writer.write("# This is the configuration file for Embeddium.\n");
            writer.write("#\n");
            writer.write("# You can find information on editing this file and all the available options here:\n");
            writer.write("# https://github.com/jellysquid3/sodium-fabric/wiki/Configuration-File\n");
            writer.write("#\n");
            writer.write("# By default, this file will be empty except for this notice.\n");
        } catch (Throwable var6) {
            try {
                writer.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }
            throw var6;
        }
        writer.close();
    }

    private static String getMixinRuleName(String name) {
        return "mixin." + name;
    }

    public int getOptionCount() {
        return this.options.size();
    }

    public int getOptionOverrideCount() {
        return (int) this.options.values().stream().filter(MixinOption::isOverridden).count();
    }
}