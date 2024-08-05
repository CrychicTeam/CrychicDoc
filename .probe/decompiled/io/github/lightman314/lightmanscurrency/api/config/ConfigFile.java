package io.github.lightman314.lightmanscurrency.api.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

public abstract class ConfigFile {

    private static final List<ConfigFile> loadableFiles = new ArrayList();

    private final String fileName;

    private final List<Runnable> reloadListeners = new ArrayList();

    private ConfigFile.ConfigSection root = null;

    public final ConfigFile.LoadPhase loadPhase;

    private boolean reloading = false;

    private boolean loaded = false;

    public static Iterable<ConfigFile> getAvailableFiles() {
        return ImmutableList.copyOf(loadableFiles);
    }

    private static void registerConfig(@Nonnull ConfigFile file) {
        loadableFiles.add(file);
    }

    public static void loadClientFiles(@Nonnull ConfigFile.LoadPhase phase) {
        loadFiles(true, phase);
    }

    public static void loadServerFiles(@Nonnull ConfigFile.LoadPhase phase) {
        loadFiles(false, phase);
    }

    public static void loadFiles(boolean logicalClient, @Nonnull ConfigFile.LoadPhase phase) {
        for (ConfigFile file : loadableFiles) {
            try {
                if (!file.isLoaded() && file.shouldReload(logicalClient) && file.loadPhase == phase) {
                    file.reload();
                }
            } catch (NullPointerException | IllegalArgumentException var5) {
                LightmansCurrency.LogError("Error reloading config file!", var5);
            }
        }
    }

    public static void reloadClientFiles() {
        reloadFiles(true);
    }

    public static void reloadServerFiles() {
        reloadFiles(false);
    }

    private static void reloadFiles(boolean logicalClient) {
        for (ConfigFile file : loadableFiles) {
            try {
                if (file.shouldReload(logicalClient)) {
                    file.reload();
                }
            } catch (NullPointerException | IllegalArgumentException var4) {
                LightmansCurrency.LogError("Error reloading config file!", var4);
            }
        }
    }

    @Nonnull
    protected String getConfigFolder() {
        return "config";
    }

    @Nonnull
    public String getFileName() {
        return this.fileName;
    }

    public final void addListener(@Nonnull Runnable listener) {
        if (!this.reloadListeners.contains(listener)) {
            this.reloadListeners.add(listener);
        }
    }

    @Nonnull
    protected String getFilePath() {
        return this.getConfigFolder() + "/" + this.fileName + ".lcconfig";
    }

    @Nonnull
    protected final File getFile() {
        return new File(this.getFilePath());
    }

    public final void confirmSetup() {
        if (this.root == null) {
            ConfigFile.ConfigBuilder builder = new ConfigFile.ConfigBuilder();
            this.setup(builder);
            this.root = builder.build(this);
        }
    }

    protected final void forEach(@Nonnull Consumer<ConfigOption<?>> action) {
        this.confirmSetup();
        this.root.forEach(action);
    }

    @Nonnull
    public final Map<String, ConfigOption<?>> getAllOptions() {
        this.confirmSetup();
        Map<String, ConfigOption<?>> results = new HashMap();
        this.collectOptionsFrom(this.root, results);
        return ImmutableMap.copyOf(results);
    }

    private void collectOptionsFrom(@Nonnull ConfigFile.ConfigSection section, @Nonnull Map<String, ConfigOption<?>> resultMap) {
        section.options.forEach((key, option) -> resultMap.put(section.fullNameOfChild(key), option));
        section.sectionsInOrder.forEach(s -> this.collectOptionsFrom(s, resultMap));
    }

    @Nullable
    protected final ConfigFile.ConfigSection findSection(@Nonnull String sectionName) {
        String[] subSections = sectionName.split("\\.");
        ConfigFile.ConfigSection currentSection = this.root;
        for (String ss : subSections) {
            if (!currentSection.sections.containsKey(ss)) {
                return null;
            }
            currentSection = (ConfigFile.ConfigSection) currentSection.sections.get(ss);
        }
        return currentSection;
    }

    protected ConfigFile(@Nonnull String fileName) {
        this(fileName, ConfigFile.LoadPhase.SETUP);
    }

    protected ConfigFile(@Nonnull String fileName, @Nonnull ConfigFile.LoadPhase loadPhase) {
        this.fileName = fileName;
        this.loadPhase = loadPhase;
        registerConfig(this);
    }

    public boolean isClientOnly() {
        return false;
    }

    public boolean isServerOnly() {
        return false;
    }

    protected abstract void setup(@Nonnull ConfigFile.ConfigBuilder var1);

    public final boolean shouldReload(boolean isLogicalClient) {
        if (this.isClientOnly() && !isLogicalClient) {
            return false;
        } else {
            return this.isServerOnly() && isLogicalClient ? false : this.canReload(isLogicalClient);
        }
    }

    protected boolean canReload(boolean isLogicalClient) {
        return true;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public final void reload() {
        if (!this.reloading) {
            this.reloading = true;
            try {
                LightmansCurrency.LogInfo("Reloading " + this.getFilePath());
                this.confirmSetup();
                List<String> lines = this.readLines();
                this.forEach(ConfigOption::clear);
                ConfigFile.ConfigSection currentSection = this.root;
                for (String line : lines) {
                    String cleanLine = cleanStartingWhitespace(line);
                    if (!cleanLine.startsWith("#")) {
                        int equalIndex = cleanLine.indexOf(61);
                        if (equalIndex > 0) {
                            String optionName = cleanLine.substring(0, equalIndex);
                            String optionValue = "";
                            if (equalIndex < cleanLine.length() - 1) {
                                optionValue = cleanLine.substring(equalIndex + 1);
                            }
                            if (currentSection.options.containsKey(optionName)) {
                                ((ConfigOption) currentSection.options.get(optionName)).load(optionValue, ConfigOption.LoadSource.FILE);
                            } else {
                                LightmansCurrency.LogWarning("Option " + currentSection.fullName() + "." + optionName + " found in the file, but is not present in the config setup!");
                            }
                        } else if (cleanLine.startsWith("[")) {
                            String fullyCleaned = ConfigOption.cleanWhitespace(cleanLine);
                            if (fullyCleaned.endsWith("]")) {
                                String section = fullyCleaned.substring(1, fullyCleaned.length() - 1);
                                ConfigFile.ConfigSection query = this.findSection(section);
                                if (query != null) {
                                    currentSection = query;
                                } else {
                                    LightmansCurrency.LogWarning("Line " + (lines.indexOf(line) + 1) + " of " + this.fileName + " contained a section (" + section + ") that is not present in this config!");
                                    currentSection = this.root;
                                }
                            } else {
                                LightmansCurrency.LogWarning("Line " + (lines.indexOf(line) + 1) + " of config '" + this.fileName + "' is missing the ']' for the section label!");
                            }
                        }
                    }
                }
                this.getAllOptions().forEach((id, option) -> {
                    if (!option.isLoaded()) {
                        option.loadDefault();
                        LightmansCurrency.LogWarning("Option " + id + " was missing from the config. Default value will be used instead.");
                    }
                });
                this.loaded = true;
                this.writeToFile();
                this.afterReload();
                for (Runnable l : this.reloadListeners) {
                    l.run();
                }
            } catch (Throwable var10) {
                this.reloading = false;
                throw var10;
            }
            this.reloading = false;
        }
    }

    @Nonnull
    public static String cleanStartingWhitespace(@Nonnull String line) {
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (!Character.isWhitespace(c)) {
                return line.substring(i);
            }
        }
        return line;
    }

    private List<String> readLines() {
        File file = this.getFile();
        if (!file.exists()) {
            return new ArrayList();
        } else {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
                List<String> lines = new ArrayList();
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                br.close();
                return lines;
            } catch (IOException var5) {
                LightmansCurrency.LogError("Error loading config file '" + file.getPath() + "'!", var5);
                return new ArrayList();
            }
        }
    }

    public final void onOptionChanged(@Nonnull ConfigOption<?> option) {
        this.writeToFile();
        this.afterOptionChanged(option);
    }

    public final void writeToFile() {
        File file = this.getFile();
        try {
            if (!file.exists()) {
                File folder = new File(file.getParent());
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                if (!file.createNewFile()) {
                    LightmansCurrency.LogError("Unable to create " + this.fileName + "!");
                    return;
                }
            }
            PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
            try {
                this.confirmSetup();
                this.writeSection(writer, this.root);
            } catch (Throwable var6) {
                try {
                    writer.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
                throw var6;
            }
            writer.close();
        } catch (SecurityException | IOException var7) {
            LightmansCurrency.LogError("Error modifying " + this.fileName + "!", var7);
        }
    }

    private void writeSection(@Nonnull PrintWriter writer, @Nonnull ConfigFile.ConfigSection section) {
        if (section.parent != null) {
            Consumer<String> w = section.parent.lineConsumer(writer);
            writeComments(section.comments, w);
            w.accept("[" + section.fullName() + "]");
        }
        Consumer<String> w = section.lineConsumer(writer);
        section.optionsInOrder.forEach(pair -> ((ConfigOption) pair.getSecond()).write((String) pair.getFirst(), w));
        section.sectionsInOrder.forEach(s -> this.writeSection(writer, s));
    }

    public static Consumer<String> lineConsumer(@Nonnull PrintWriter writer, int depth) {
        return s -> writer.println("\t".repeat(Math.max(0, depth)) + s);
    }

    public static void writeComments(@Nonnull List<String> comments, @Nonnull Consumer<String> writer) {
        for (String c : comments) {
            for (String c2 : c.split("\n")) {
                writer.accept("#" + c2);
            }
        }
    }

    protected void afterReload() {
    }

    protected void afterOptionChanged(@Nonnull ConfigOption<?> option) {
    }

    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    protected static final class ConfigBuilder {

        private final ConfigFile.ConfigSectionBuilder root = new ConfigFile.ConfigSectionBuilder("root", 0, null);

        private final List<String> comments = new ArrayList();

        private ConfigFile.ConfigSectionBuilder currentSection = this.root;

        private ConfigFile.ConfigSection build(ConfigFile file) {
            return this.root.build(null, file);
        }

        private ConfigBuilder() {
        }

        public ConfigFile.ConfigBuilder push(String newSection) {
            if (invalidName(newSection)) {
                throw new IllegalArgumentException("Illegal section name '" + newSection + "'!");
            } else {
                if (this.currentSection.sections.containsKey(newSection)) {
                    this.currentSection = (ConfigFile.ConfigSectionBuilder) this.currentSection.sections.get(newSection);
                } else {
                    this.currentSection = this.currentSection.addChild(newSection);
                }
                this.currentSection.comments.addAll(this.comments);
                this.comments.clear();
                return this;
            }
        }

        public static boolean invalidName(String name) {
            if (name.equals("root")) {
                return false;
            } else {
                for (int i = 0; i < name.length(); i++) {
                    if (!validNameChar(name.charAt(i))) {
                        return true;
                    }
                }
                return false;
            }
        }

        public static boolean validNameChar(char c) {
            return c == '_' || c == '-' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c >= 'A' && c <= 'Z';
        }

        public ConfigFile.ConfigBuilder pop() {
            if (this.currentSection == this.root) {
                throw new IllegalArgumentException("Cannot pop the builder when we're already at root level!");
            } else {
                this.currentSection = this.currentSection.parent;
                return this;
            }
        }

        public ConfigFile.ConfigBuilder comment(String... comment) {
            this.comments.addAll(ImmutableList.copyOf(comment));
            return this;
        }

        public ConfigFile.ConfigBuilder add(@Nonnull String optionName, @Nonnull ConfigOption<?> option) {
            if (invalidName(optionName)) {
                throw new IllegalArgumentException("Illegal option name '" + optionName + "'!");
            } else {
                if (this.currentSection == null) {
                    this.currentSection = this.root;
                }
                if (this.currentSection.options.containsKey(optionName)) {
                    LightmansCurrency.LogError("Duplicate option '" + this.currentSection.fullNameOfChild(optionName) + "'!\nDuplicate option will be ignored!");
                    return this;
                } else {
                    this.currentSection.addOption(optionName, option);
                    option.setComments(this.comments);
                    this.comments.clear();
                    return this;
                }
            }
        }
    }

    protected static final class ConfigSection {

        private final ConfigFile.ConfigSection parent;

        private final int depth;

        private final String name;

        private final List<String> comments;

        private final List<ConfigFile.ConfigSection> sectionsInOrder;

        private final Map<String, ConfigFile.ConfigSection> sections;

        private final List<Pair<String, ConfigOption<?>>> optionsInOrder;

        private final Map<String, ConfigOption<?>> options;

        private String fullName() {
            return this.parent != null ? this.parent.fullNameOfChild(this.name) : this.name;
        }

        private String fullNameOfChild(@Nonnull String childName) {
            return this.parent == null ? childName : this.fullName() + "." + childName;
        }

        void forEach(@Nonnull Consumer<ConfigOption<?>> action) {
            this.optionsInOrder.forEach(p -> action.accept((ConfigOption) p.getSecond()));
            this.sectionsInOrder.forEach(s -> s.forEach(action));
        }

        Consumer<String> lineConsumer(@Nonnull PrintWriter writer) {
            return ConfigFile.lineConsumer(writer, this.depth);
        }

        private ConfigSection(ConfigFile.ConfigSectionBuilder builder, ConfigFile.ConfigSection parent, ConfigFile file) {
            this.name = builder.name;
            this.depth = builder.depth;
            this.parent = parent;
            this.comments = ImmutableList.copyOf(builder.comments);
            this.optionsInOrder = ImmutableList.copyOf(builder.optionsInOrder);
            this.options = ImmutableMap.copyOf(builder.options);
            this.options.forEach((key, option) -> option.init(file, key, this.fullNameOfChild(key)));
            List<ConfigFile.ConfigSection> temp1 = new ArrayList();
            builder.sectionsInOrder.forEach(b -> temp1.add(b.build(this, file)));
            this.sectionsInOrder = ImmutableList.copyOf(temp1);
            Map<String, ConfigFile.ConfigSection> temp2 = new HashMap();
            this.sectionsInOrder.forEach(section -> temp2.put(section.name, section));
            this.sections = ImmutableMap.copyOf(temp2);
        }
    }

    private static final class ConfigSectionBuilder {

        private final ConfigFile.ConfigSectionBuilder parent;

        private final String name;

        private final int depth;

        private final List<String> comments = new ArrayList();

        private final List<ConfigFile.ConfigSectionBuilder> sectionsInOrder = new ArrayList();

        private final Map<String, ConfigFile.ConfigSectionBuilder> sections = new HashMap();

        private final List<Pair<String, ConfigOption<?>>> optionsInOrder = new ArrayList();

        private final Map<String, ConfigOption<?>> options = new HashMap();

        private String fullName() {
            return this.parent != null ? this.parent.fullNameOfChild(this.name) : this.name;
        }

        private String fullNameOfChild(@Nonnull String childName) {
            return this.parent == null ? childName : this.fullName() + "." + childName;
        }

        private ConfigSectionBuilder(@Nonnull String name, int depth, ConfigFile.ConfigSectionBuilder parent) {
            this.name = name;
            this.depth = depth;
            this.parent = parent;
        }

        private void addOption(@Nonnull String name, @Nonnull ConfigOption<?> option) {
            this.optionsInOrder.add(Pair.of(name, option));
            this.options.put(name, option);
        }

        private ConfigFile.ConfigSectionBuilder addChild(@Nonnull String name) {
            ConfigFile.ConfigSectionBuilder builder = new ConfigFile.ConfigSectionBuilder(name, this.depth + 1, this);
            this.sectionsInOrder.add(builder);
            this.sections.put(name, builder);
            return builder;
        }

        private ConfigFile.ConfigSection build(@Nullable ConfigFile.ConfigSection parent, @Nonnull ConfigFile file) {
            return new ConfigFile.ConfigSection(this, parent, file);
        }
    }

    public static enum LoadPhase {

        NULL, SETUP, GAME_START
    }
}