package journeymap.client.io;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.log.JMLogger;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.theme.Theme;
import journeymap.client.ui.theme.ThemePresets;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.config.StringField;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ThemeLoader {

    public static final String THEME_FILE_SUFFIX = ".theme2.json";

    public static final String DEFAULT_THEME_FILE = "default.theme.config";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setVersion(2.0).create();

    private static transient Theme currentTheme = null;

    public static void initialize(boolean preLoadCurrentTheme) {
        Journeymap.getLogger().trace("Initializing themes ...");
        for (String dirName : (Set) ThemePresets.getPresetDirs().stream().collect(Collectors.toSet())) {
            FileHandler.copyResources(getThemeIconDir(), new ResourceLocation("journeymap", "theme/" + dirName), dirName, true);
        }
        ThemePresets.getPresets().forEach(ThemeLoader::save);
        ensureDefaultThemeFile();
        if (preLoadCurrentTheme) {
            preloadCurrentTheme();
        }
    }

    public static File getThemeIconDir() {
        File dir = new File(FileHandler.getMinecraftDirectory(), Constants.THEME_ICON_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File[] getThemeDirectories() {
        File parentDir = getThemeIconDir();
        return parentDir.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
    }

    public static List<Theme> getThemes() {
        File[] themeDirs = getThemeDirectories();
        if (themeDirs == null || themeDirs.length == 0) {
            initialize(false);
            themeDirs = getThemeDirectories();
            if (themeDirs == null || themeDirs.length == 0) {
                Journeymap.getLogger().error("Couldn't find theme directories.");
                return Collections.emptyList();
            }
        }
        ArrayList<Theme> themes = new ArrayList();
        for (File themeDir : themeDirs) {
            File[] themeFiles = themeDir.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.endsWith(".theme2.json");
                }
            });
            if (themeFiles != null && themeFiles.length > 0) {
                for (File themeFile : themeFiles) {
                    Theme theme = loadThemeFromFile(themeFile, false);
                    if (theme != null) {
                        themes.add(theme);
                    }
                }
            }
        }
        if (themes.isEmpty()) {
            themes.addAll(ThemePresets.getPresets());
        }
        Collections.sort(themes, Comparator.comparing(themex -> themex.name));
        return themes;
    }

    public static List<String> getThemeNames() {
        List<Theme> themes = null;
        try {
            themes = getThemes();
        } catch (Exception var2) {
            themes = ThemePresets.getPresets();
        }
        return (List<String>) themes.stream().map(theme -> theme.name).collect(Collectors.toList());
    }

    public static Theme getCurrentTheme() {
        return getCurrentTheme(false);
    }

    public static synchronized void setCurrentTheme(Theme theme) {
        if (currentTheme != theme) {
            JourneymapClient.getInstance().getCoreProperties().themeName.set(theme.name);
            getCurrentTheme(true);
            UIManager.INSTANCE.getMiniMap().reset();
        }
    }

    public static synchronized Theme getCurrentTheme(boolean forceReload) {
        if (forceReload) {
            TextureCache.purgeThemeImages(TextureCache.themeImages);
        }
        String themeName = JourneymapClient.getInstance().getCoreProperties().themeName.get();
        if (forceReload || currentTheme == null || !themeName.equals(currentTheme.name)) {
            currentTheme = getThemeByName(themeName);
            JourneymapClient.getInstance().getCoreProperties().themeName.set(currentTheme.name);
        }
        return currentTheme;
    }

    public static Theme getThemeByName(String themeName) {
        for (Theme theme : getThemes()) {
            if (theme.name.equals(themeName)) {
                return theme;
            }
        }
        Journeymap.getLogger().warn(String.format("Theme '%s' not found, reverting to default", themeName));
        return ThemePresets.getDefault();
    }

    public static Theme loadThemeFromFile(File themeFile, boolean createIfMissing) {
        try {
            if (themeFile != null && themeFile.exists()) {
                Charset UTF8 = Charset.forName("UTF-8");
                String json = Files.toString(themeFile, UTF8);
                Theme theme = (Theme) GSON.fromJson(json, Theme.class);
                if ((double) theme.schema < 2.0) {
                    Journeymap.getLogger().error("Theme file schema is obsolete, cannot be used: " + themeFile);
                    return null;
                }
                return theme;
            }
            if (createIfMissing) {
                Journeymap.getLogger().info("Generating Theme json file: " + themeFile);
                Theme theme = new Theme();
                theme.name = themeFile.getName();
                save(theme);
                return theme;
            }
        } catch (Throwable var5) {
            Journeymap.getLogger().error("Could not load Theme json file: " + LogFormatter.toString(var5));
        }
        return null;
    }

    private static File getThemeFile(String themeDirName, String themeFileName) {
        File themeDir = new File(getThemeIconDir(), themeDirName);
        String fileName = String.format("%s%s", themeFileName.replaceAll("[\\\\/:\"*?<>|]", "_"), ".theme2.json");
        return new File(themeDir, fileName);
    }

    public static void save(Theme theme) {
        try {
            File themeFile = getThemeFile(theme.directory, theme.name);
            Files.createParentDirs(themeFile);
            Charset UTF8 = Charset.forName("UTF-8");
            Files.write(GSON.toJson(theme), themeFile, UTF8);
        } catch (Throwable var3) {
            Journeymap.getLogger().error("Could not save Theme json file: " + var3);
        }
    }

    private static void ensureDefaultThemeFile() {
        File defaultThemeFile = new File(getThemeIconDir(), "default.theme.config");
        if (!defaultThemeFile.exists()) {
            try {
                Theme.DefaultPointer defaultPointer = new Theme.DefaultPointer(ThemePresets.getDefault());
                Charset UTF8 = Charset.forName("UTF-8");
                Files.write(GSON.toJson(defaultPointer), defaultThemeFile, UTF8);
            } catch (Throwable var3) {
                Journeymap.getLogger().error("Could not save DefaultTheme json file: " + var3);
            }
        }
    }

    public static Theme getDefaultTheme() {
        if (Minecraft.getInstance() == null) {
            return ThemePresets.getDefault();
        } else {
            Theme theme = null;
            File themeFile = null;
            Theme.DefaultPointer pointer = null;
            try {
                pointer = loadDefaultPointer();
                pointer.filename = pointer.filename.replace(".theme2.json", "");
                themeFile = getThemeFile(pointer.directory, pointer.filename);
                theme = loadThemeFromFile(themeFile, false);
            } catch (Exception var4) {
                JMLogger.throwLogOnce("Default theme not found in files", var4);
            }
            if (theme == null) {
                theme = ThemePresets.getDefault();
            }
            return theme;
        }
    }

    public static synchronized void loadNextTheme() {
        List<String> themeNames = getThemeNames();
        int index = themeNames.indexOf(getCurrentTheme().name);
        if (index >= 0 && index < themeNames.size() - 1) {
            index++;
        } else {
            index = 0;
        }
        setCurrentTheme((Theme) getThemes().get(index));
    }

    private static Theme.DefaultPointer loadDefaultPointer() {
        try {
            ensureDefaultThemeFile();
            File defaultThemeFile = new File(getThemeIconDir(), "default.theme.config");
            if (defaultThemeFile.exists()) {
                Charset UTF8 = Charset.forName("UTF-8");
                String json = Files.toString(defaultThemeFile, UTF8);
                return (Theme.DefaultPointer) GSON.fromJson(json, Theme.DefaultPointer.class);
            } else {
                return new Theme.DefaultPointer(ThemePresets.getDefault());
            }
        } catch (Throwable var3) {
            Journeymap.getLogger().error("Could not load Theme.DefaultTheme json file: " + LogFormatter.toString(var3));
            return null;
        }
    }

    public static void preloadCurrentTheme() {
        int count = 0;
        try {
            Theme theme = getCurrentTheme();
            File themeDir = new File(getThemeIconDir(), theme.directory).getCanonicalFile();
            Path themePath = themeDir.toPath();
            for (File file : Files.fileTraverser().breadthFirst(themeDir)) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                    String relativePath = themePath.relativize(file.toPath()).toString().replaceAll("\\\\", "/");
                    TextureCache.getThemeTexture(theme, relativePath);
                    count++;
                }
            }
        } catch (Throwable var7) {
            Journeymap.getLogger().error("Error preloading theme textures: " + LogFormatter.toString(var7));
        }
        Journeymap.getLogger().info("Preloaded theme textures: " + count);
    }

    public static class ThemeValuesProvider implements StringField.ValuesProvider {

        @Override
        public List<String> getStrings() {
            return ThemeLoader.getThemeNames();
        }

        @Override
        public String getDefaultString() {
            return ThemeLoader.getDefaultTheme().name;
        }
    }
}