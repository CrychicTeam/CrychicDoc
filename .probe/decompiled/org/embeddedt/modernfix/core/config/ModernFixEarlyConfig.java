package org.embeddedt.modernfix.core.config;

import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.annotation.IgnoreOutsideDev;
import org.embeddedt.modernfix.annotation.RequiresMod;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixin;

public class ModernFixEarlyConfig {

    private static final Logger LOGGER = LogManager.getLogger("ModernFixConfig");

    private final Map<String, Option> options = new HashMap();

    private final Multimap<String, Option> optionsByCategory = HashMultimap.create();

    private static final boolean ALLOW_OVERRIDE_OVERRIDES = Boolean.getBoolean("modernfix.unsupported.allowOverriding");

    public static final boolean OPTIFINE_PRESENT;

    private File configFile;

    private static final String MIXIN_DESC;

    private static final String MIXIN_CLIENT_ONLY_DESC;

    private static final String MIXIN_REQUIRES_MOD_DESC;

    private static final String MIXIN_DEV_ONLY_DESC;

    private static final Pattern PLATFORM_PREFIX;

    private final Set<String> mixinOptions = new ObjectOpenHashSet();

    private final Map<String, String> mixinsMissingMods = new Object2ObjectOpenHashMap();

    public static boolean isFabric;

    private static final boolean isDevEnv;

    private static final ImmutableMap<String, Boolean> DEFAULT_SETTING_OVERRIDES;

    private static boolean modPresent(String modId) {
        return modId.equals("optifine") ? OPTIFINE_PRESENT : ModernFixPlatformHooks.INSTANCE.modPresent(modId);
    }

    public static String sanitize(String mixinClassName) {
        return PLATFORM_PREFIX.matcher(mixinClassName).replaceFirst("");
    }

    public Map<String, String> getPermanentlyDisabledMixins() {
        return this.mixinsMissingMods;
    }

    private void scanForAndBuildMixinOptions() {
        List<String> configFiles = ImmutableList.of("modernfix-common.mixins.json", "modernfix-fabric.mixins.json", "modernfix-forge.mixins.json");
        List<String> mixinPaths = new ArrayList();
        for (String configFile : configFiles) {
            InputStream stream = ModernFixEarlyConfig.class.getClassLoader().getResourceAsStream(configFile);
            if (stream != null) {
                try {
                    Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                    try {
                        JsonObject configObject = (JsonObject) new JsonParser().parse(reader);
                        List<JsonElement> mixinList = (List<JsonElement>) Stream.of("mixins", "client").map(key -> Optional.ofNullable(configObject.getAsJsonArray(key))).flatMap(arr -> (Stream) arr.map(jsonElements -> StreamSupport.stream(jsonElements.spliterator(), false)).orElseGet(() -> Stream.of())).collect(Collectors.toList());
                        String packageName = configObject.get("package").getAsString().replace('.', '/');
                        for (JsonElement mixin : mixinList) {
                            mixinPaths.add(packageName + "/" + mixin.getAsString().replace('.', '/') + ".class");
                        }
                    } catch (Throwable var22) {
                        try {
                            reader.close();
                        } catch (Throwable var19) {
                            var22.addSuppressed(var19);
                        }
                        throw var22;
                    }
                    reader.close();
                } catch (JsonParseException | IOException var23) {
                    LOGGER.error("Error loading config " + configFile, var23);
                }
            }
        }
        Splitter dotSplitter = Splitter.on('.');
        for (String mixinPath : mixinPaths) {
            try {
                InputStream stream = ModernFixEarlyConfig.class.getClassLoader().getResourceAsStream(mixinPath);
                label132: {
                    try {
                        ClassReader reader = new ClassReader(stream);
                        ClassNode node = new ClassNode();
                        reader.accept(node, 7);
                        if (node.invisibleAnnotations == null) {
                            break label132;
                        }
                        boolean isMixin = false;
                        boolean isClientOnly = false;
                        boolean requiredModPresent = true;
                        boolean isDevOnly = false;
                        String requiredModId = "";
                        for (AnnotationNode annotation : node.invisibleAnnotations) {
                            if (Objects.equals(annotation.desc, MIXIN_DESC)) {
                                isMixin = true;
                            } else if (Objects.equals(annotation.desc, MIXIN_CLIENT_ONLY_DESC)) {
                                isClientOnly = true;
                            } else if (Objects.equals(annotation.desc, MIXIN_REQUIRES_MOD_DESC)) {
                                for (int i = 0; i < annotation.values.size(); i += 2) {
                                    if (annotation.values.get(i).equals("value")) {
                                        String modId = (String) annotation.values.get(i + 1);
                                        if (modId != null) {
                                            requiredModPresent = modId.startsWith("!") ? !modPresent(modId.substring(1)) : modPresent(modId);
                                            requiredModId = modId;
                                        }
                                        break;
                                    }
                                }
                            } else if (Objects.equals(annotation.desc, MIXIN_DEV_ONLY_DESC)) {
                                isDevOnly = true;
                            }
                        }
                        if (isMixin && (!isDevOnly || ModernFixPlatformHooks.INSTANCE.isDevEnv())) {
                            String mixinClassName = sanitize(node.name.replace('/', '.')).replace("org.embeddedt.modernfix.mixin.", "");
                            if (!requiredModPresent) {
                                this.mixinsMissingMods.put(mixinClassName, requiredModId);
                            } else if (isClientOnly && !ModernFixPlatformHooks.INSTANCE.isClient()) {
                                this.mixinsMissingMods.put(mixinClassName, "[not client]");
                            }
                            String mixinCategoryName = "mixin." + mixinClassName.substring(0, mixinClassName.lastIndexOf(46));
                            this.mixinOptions.add(mixinCategoryName);
                        }
                    } catch (Throwable var20) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable var18) {
                                var20.addSuppressed(var18);
                            }
                        }
                        throw var20;
                    }
                    if (stream != null) {
                        stream.close();
                    }
                    continue;
                }
                if (stream != null) {
                    stream.close();
                }
                return;
            } catch (IOException var21) {
                LOGGER.error("Error scanning file " + mixinPath, var21);
            }
        }
    }

    private ModernFixEarlyConfig(File file) {
        this.configFile = file;
        OptionCategories.load();
        this.scanForAndBuildMixinOptions();
        this.mixinOptions.addAll(DEFAULT_SETTING_OVERRIDES.keySet());
        for (String optionName : this.mixinOptions) {
            boolean defaultEnabled = (Boolean) DEFAULT_SETTING_OVERRIDES.getOrDefault(optionName, true);
            Option option = new Option(optionName, defaultEnabled, false);
            this.options.putIfAbsent(optionName, option);
            this.optionsByCategory.put(OptionCategories.getCategoryForOption(optionName), option);
        }
        for (Entry<String, Option> entry : this.options.entrySet()) {
            int idx = ((String) entry.getKey()).lastIndexOf(46);
            if (idx > 0) {
                String potentialParentKey = ((String) entry.getKey()).substring(0, idx);
                Option potentialParent = (Option) this.options.get(potentialParentKey);
                if (potentialParent != null) {
                    ((Option) entry.getValue()).setParent(potentialParent);
                }
            }
        }
        this.addMixinRule("launch.class_search_cache", true);
        this.disableIfModPresent("mixin.perf.thread_priorities", "smoothboot", "threadtweak");
        this.disableIfModPresent("mixin.perf.boost_worker_count", "smoothboot", "threadtweak");
        this.disableIfModPresent("mixin.perf.compress_biome_container", "chocolate", "betterendforge", "skyblockbuilder", "modern_beta", "worldedit");
        this.disableIfModPresent("mixin.bugfix.mc218112", "performant");
        this.disableIfModPresent("mixin.bugfix.remove_block_chunkloading", "performant");
        this.disableIfModPresent("mixin.bugfix.paper_chunk_patches", "c2me");
        this.disableIfModPresent("mixin.bugfix.preserve_early_window_pos", "better_loading_screen");
        this.disableIfModPresent("mixin.perf.dynamic_dfu", "litematica");
        this.disableIfModPresent("mixin.perf.cache_strongholds", "littletiles", "c2me");
        this.disableIfModPresent("mixin.perf.deduplicate_wall_shapes", "dashloader");
        this.disableIfModPresent("mixin.perf.nbt_memory_usage", "c2me");
        this.disableIfModPresent("mixin.bugfix.item_cache_flag", "lithium", "canary", "radium");
        this.disableIfModPresent("mixin.bugfix.chunk_deadlock", "c2me", "dimthread");
        this.disableIfModPresent("mixin.perf.reuse_datapacks", "tac");
        this.disableIfModPresent("mixin.launch.class_search_cache", "optifine");
        this.disableIfModPresent("mixin.perf.faster_texture_stitching", "optifine");
        this.disableIfModPresent("mixin.bugfix.entity_pose_stack", "optifine");
        this.disableIfModPresent("mixin.perf.datapack_reload_exceptions", "cyanide");
        this.disableIfModPresent("mixin.bugfix.buffer_builder_leak", "isometric-renders", "witherstormmod");
        this.disableIfModPresent("mixin.feature.remove_chat_signing", "nochatreports");
        this.disableIfModPresent("mixin.perf.faster_texture_loading", "stitch", "optifine", "changed");
        if (isFabric) {
            this.disableIfModPresent("mixin.bugfix.packet_leak", "memoryleakfix");
        }
        this.checkBlockstateCacheRebuilds();
        this.checkModelDataManager();
    }

    private void checkBlockstateCacheRebuilds() {
        if (ModernFixPlatformHooks.INSTANCE.isDevEnv()) {
            try {
                URL deobfClass = isFabric ? ModernFixEarlyConfig.class.getResource("/net/minecraft/world/level/Level.class") : ModernFixEarlyConfig.class.getClassLoader().getResource("/net/minecraft/world/level/Level.class");
                if (deobfClass == null) {
                    LOGGER.warn("We are in a non-Mojmap dev environment. Disabling blockstate cache patch");
                    ((Option) this.options.get("mixin.perf.reduce_blockstate_cache_rebuilds")).addModOverride(false, "[not mojmap]");
                }
            } catch (Throwable var2) {
                var2.printStackTrace();
            }
        }
    }

    private void checkModelDataManager() {
        if (!isFabric && modPresent("rubidium") && !modPresent("embeddium")) {
            Option option = (Option) this.options.get("mixin.bugfix.model_data_manager_cme");
            if (option != null) {
                LOGGER.warn("ModelDataManager bugfixes have been disabled to prevent broken rendering with Rubidium installed. Please migrate to Embeddium.");
                option.addModOverride(false, "rubidium");
            }
        }
    }

    private void disableIfModPresent(String configName, String... ids) {
        for (String id : ids) {
            if (!ModernFixPlatformHooks.INSTANCE.isEarlyLoadingNormally() || modPresent(id)) {
                Option option = (Option) this.options.get(configName);
                if (option != null) {
                    option.addModOverride(false, id);
                }
            }
        }
    }

    private void addMixinRule(String mixin, boolean enabled) {
        String name = getMixinRuleName(mixin);
        if (this.options.putIfAbsent(name, new Option(name, enabled, false)) != null) {
            throw new IllegalStateException("Mixin rule already defined: " + mixin);
        }
    }

    private void readJVMProperties() {
        for (String optionKey : this.options.keySet()) {
            String value = System.getProperty("modernfix.config." + optionKey);
            if (value != null && value.length() != 0) {
                boolean isEnabled = Boolean.valueOf(value);
                ModernFixMixinPlugin.instance.logger.info("Configured {} to '{}' via JVM property.", optionKey, isEnabled);
                ((Option) this.options.get(optionKey)).setEnabled(isEnabled, true);
            }
        }
    }

    private void readProperties(Properties props) {
        if (ALLOW_OVERRIDE_OVERRIDES) {
            LOGGER.fatal("JVM argument given to override mod overrides. Issues opened with this option present will be ignored unless they can be reproduced without.");
        }
        for (Entry<Object, Object> entry : props.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            Option option = (Option) this.options.get(key);
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
                if (!ALLOW_OVERRIDE_OVERRIDES && option.isModDefined()) {
                    LOGGER.warn("Option '{}' already disabled by a mod. Ignoring user configuration", key);
                } else {
                    option.setEnabled(enabled, true);
                }
            }
        }
    }

    public Option getEffectiveOptionForMixin(String mixinClassName) {
        int lastSplit = 0;
        Option rule = null;
        int nextSplit;
        while ((nextSplit = mixinClassName.indexOf(46, lastSplit)) != -1) {
            String key = getMixinRuleName(mixinClassName.substring(0, nextSplit));
            Option candidate = (Option) this.options.get(key);
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

    public static ModernFixEarlyConfig load(File file) {
        ModernFixEarlyConfig config = new ModernFixEarlyConfig(file);
        Properties props = new Properties();
        if (!Boolean.getBoolean("modernfix.ignoreConfigForTesting")) {
            if (file.exists()) {
                try {
                    FileInputStream fin = new FileInputStream(file);
                    try {
                        props.load(fin);
                    } catch (Throwable var8) {
                        try {
                            fin.close();
                        } catch (Throwable var6) {
                            var8.addSuppressed(var6);
                        }
                        throw var8;
                    }
                    fin.close();
                } catch (IOException var9) {
                    throw new RuntimeException("Could not load config file", var9);
                }
                config.readProperties(props);
            }
            try {
                config.save();
            } catch (IOException var7) {
                LOGGER.warn("Could not write configuration file", var7);
            }
            config.readJVMProperties();
        }
        return config;
    }

    public void save() throws IOException {
        File dir = this.configFile.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Could not create parent directories");
            }
        } else if (!dir.isDirectory()) {
            throw new IOException("The parent file is not a directory");
        }
        Writer writer = new FileWriter(this.configFile);
        try {
            writer.write("# This is the configuration file for ModernFix.\n");
            writer.write("# In general, prefer using the config screen to editing this file. It can be accessed\n");
            writer.write("# via the standard mod menu on your respective mod loader. Changes will, however,\n");
            writer.write("# require restarting the game to take effect.\n");
            writer.write("#\n");
            writer.write("# The following options can be enabled or disabled if there is a compatibility issue.\n");
            writer.write("# Add a line with your option name and =true or =false at the bottom of the file to enable\n");
            writer.write("# or disable a rule. For example:\n");
            writer.write("#   mixin.perf.dynamic_resources=true\n");
            writer.write("# Do not include the #. You may reset to defaults by deleting this file.\n");
            writer.write("#\n");
            writer.write("# Available options:\n");
            List<String> keys = (List<String>) this.options.keySet().stream().filter(keyx -> !keyx.equals("mixin.core")).sorted().collect(Collectors.toList());
            for (String line : keys) {
                if (!line.equals("mixin.core")) {
                    Option option = (Option) this.options.get(line);
                    String extraContext = "";
                    if (option != null) {
                        if (!option.isUserDefined()) {
                            extraContext = "=" + option.isEnabled() + " # " + (option.isModDefined() ? "(overridden for mod compat)" : "(default)");
                        } else {
                            boolean defaultEnabled = (Boolean) DEFAULT_SETTING_OVERRIDES.getOrDefault(line, true);
                            extraContext = "=" + defaultEnabled + " # (default)";
                        }
                    }
                    writer.write("#  " + line + extraContext + "\n");
                }
            }
            writer.write("#\n");
            writer.write("# User overrides go here.\n");
            for (String key : keys) {
                Option option = (Option) this.options.get(key);
                if (option.isUserDefined()) {
                    writer.write(key + "=" + option.isEnabled() + "\n");
                }
            }
        } catch (Throwable var10) {
            try {
                writer.close();
            } catch (Throwable var9) {
                var10.addSuppressed(var9);
            }
            throw var10;
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
        return (int) this.options.values().stream().filter(Option::isOverridden).count();
    }

    public Map<String, Option> getOptionMap() {
        return Collections.unmodifiableMap(this.options);
    }

    public Multimap<String, Option> getOptionCategoryMap() {
        return Multimaps.unmodifiableMultimap(this.optionsByCategory);
    }

    static {
        boolean hasOfClass = false;
        try {
            Class.forName("optifine.OptiFineTransformationService");
            hasOfClass = true;
        } catch (Throwable var2) {
        }
        OPTIFINE_PRESENT = hasOfClass;
        MIXIN_DESC = Type.getDescriptor(Mixin.class);
        MIXIN_CLIENT_ONLY_DESC = Type.getDescriptor(ClientOnlyMixin.class);
        MIXIN_REQUIRES_MOD_DESC = Type.getDescriptor(RequiresMod.class);
        MIXIN_DEV_ONLY_DESC = Type.getDescriptor(IgnoreOutsideDev.class);
        PLATFORM_PREFIX = Pattern.compile("(forge|fabric|common)\\.");
        isFabric = ModernFixEarlyConfig.class.getClassLoader().getResourceAsStream("modernfix-fabric.mixins.json") != null;
        isDevEnv = ModernFixPlatformHooks.INSTANCE.isDevEnv();
        DEFAULT_SETTING_OVERRIDES = new ModernFixEarlyConfig.DefaultSettingMapBuilder().put("mixin.perf.dynamic_resources", false).put("mixin.feature.direct_stack_trace", false).put("mixin.feature.stalled_chunk_load_detection", false).put("mixin.perf.blast_search_trees.force", false).put("mixin.bugfix.restore_old_dragon_movement", false).put("mixin.perf.worldgen_allocation", false).put("mixin.feature.cause_lag_by_disabling_threads", false).put("mixin.perf.clear_mixin_classinfo", false).put("mixin.perf.deduplicate_climate_parameters", false).put("mixin.bugfix.packet_leak", false).put("mixin.perf.deduplicate_location", false).put("mixin.perf.dynamic_entity_renderers", false).put("mixin.feature.integrated_server_watchdog", true).put("mixin.perf.faster_item_rendering", false).put("mixin.feature.spam_thread_dump", false).put("mixin.feature.disable_unihex_font", false).put("mixin.feature.remove_chat_signing", false).put("mixin.feature.snapshot_easter_egg", true).put("mixin.feature.warn_missing_perf_mods", true).put("mixin.feature.spark_profile_launch", false).put("mixin.devenv", isDevEnv).put("mixin.perf.remove_spawn_chunks", isDevEnv).putConditionally(() -> !isFabric, "mixin.bugfix.fix_config_crashes", true).putConditionally(() -> !isFabric, "mixin.bugfix.forge_at_inject_error", true).putConditionally(() -> !isFabric, "mixin.feature.registry_event_progress", false).putConditionally(() -> isFabric, "mixin.perf.clear_fabric_mapping_tables", false).build();
    }

    private static class DefaultSettingMapBuilder extends Builder<String, Boolean> {

        public ModernFixEarlyConfig.DefaultSettingMapBuilder putConditionally(BooleanSupplier condition, String k, Boolean v) {
            if (condition.getAsBoolean()) {
                this.put(k, v);
            }
            return this;
        }

        public ModernFixEarlyConfig.DefaultSettingMapBuilder put(String key, Boolean value) {
            super.put(key, value);
            return this;
        }
    }
}