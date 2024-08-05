package dev.latvian.mods.kubejs.client;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import java.io.BufferedWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.OptionalInt;
import java.util.Properties;
import net.minecraft.util.Mth;

public class ClientProperties {

    private static ClientProperties instance;

    private final Properties properties;

    private boolean writeProperties;

    private final boolean tempIconCancel = true;

    public String title;

    private boolean showTagNames;

    private boolean disableRecipeBook;

    private boolean exportAtlases;

    private boolean overrideColors;

    private int backgroundColor;

    private int barColor;

    private int barBorderColor;

    private float[] backgroundColor3f;

    private float[] fmlMemoryColor3f;

    private float[] fmlLogColor3f;

    private int menuBackgroundBrightness;

    private int menuInnerBackgroundBrightness;

    private float menuBackgroundScale;

    public boolean blurScaledPackIcon;

    public static ClientProperties get() {
        if (instance == null) {
            instance = new ClientProperties();
        }
        return instance;
    }

    public static void reload() {
        instance = new ClientProperties();
    }

    private ClientProperties() {
        this.properties = new Properties();
        try {
            this.writeProperties = false;
            if (Files.exists(KubeJSPaths.CLIENT_PROPERTIES, new LinkOption[0])) {
                Reader reader = Files.newBufferedReader(KubeJSPaths.CLIENT_PROPERTIES);
                try {
                    this.properties.load(reader);
                } catch (Throwable var5) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }
                    throw var5;
                }
                if (reader != null) {
                    reader.close();
                }
            } else {
                this.writeProperties = true;
            }
            this.title = this.get("title", "");
            this.showTagNames = this.get("showTagNames", false);
            this.disableRecipeBook = this.get("disableRecipeBook", false);
            this.exportAtlases = this.get("exportAtlases", false);
            this.overrideColors = this.get("overrideColors", false);
            this.backgroundColor = this.getColor("backgroundColor", 3028032);
            this.barColor = this.getColor("barColor", 15527924);
            this.barBorderColor = this.getColor("barBorderColor", 15527924);
            this.backgroundColor3f = this.getColor3f(this.backgroundColor);
            this.fmlMemoryColor3f = this.getColor3f(this.getColor("fmlMemoryColor", 15527924));
            this.fmlLogColor3f = this.getColor3f(this.getColor("fmlLogColor", 15527924));
            this.menuBackgroundBrightness = Mth.clamp(this.get("menuBackgroundBrightness", 64), 0, 255);
            this.menuInnerBackgroundBrightness = Mth.clamp(this.get("menuInnerBackgroundBrightness", 32), 0, 255);
            this.menuBackgroundScale = (float) Mth.clamp(this.get("menuBackgroundScale", 32.0), 0.0625, 1024.0);
            this.blurScaledPackIcon = this.get("blurScaledPackIcon", true);
            KubeJSPlugins.forEachPlugin(this, KubeJSPlugin::loadClientProperties);
            if (this.writeProperties) {
                this.save();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        KubeJS.LOGGER.info("Loaded client.properties");
    }

    public String get(String key, String def) {
        String s = this.properties.getProperty(key);
        if (s == null) {
            this.properties.setProperty(key, def);
            this.writeProperties = true;
            return def;
        } else {
            return s;
        }
    }

    public boolean get(String key, boolean def) {
        return this.get(key, def ? "true" : "false").equals("true");
    }

    public int get(String key, int def) {
        return Integer.parseInt(this.get(key, Integer.toString(def)));
    }

    public double get(String key, double def) {
        return Double.parseDouble(this.get(key, Double.toString(def)));
    }

    public int getColor(String key, int def) {
        String s = this.get(key, String.format("%06X", def & 16777215));
        if (!s.isEmpty() && !s.equals("default")) {
            try {
                return 16777215 & Integer.decode(s.startsWith("#") ? s : "#" + s);
            } catch (Exception var5) {
                var5.printStackTrace();
                return def;
            }
        } else {
            return def;
        }
    }

    public float[] getColor3f(int color) {
        return new float[] { (float) (color >> 16 & 0xFF) / 255.0F, (float) (color >> 8 & 0xFF) / 255.0F, (float) (color >> 0 & 0xFF) / 255.0F };
    }

    public boolean getShowTagNames() {
        return this.showTagNames;
    }

    public boolean getDisableRecipeBook() {
        return this.disableRecipeBook;
    }

    public boolean getExportAtlases() {
        return this.exportAtlases;
    }

    public float[] getMemoryColor(float[] color) {
        return this.overrideColors ? this.fmlMemoryColor3f : color;
    }

    public float[] getLogColor(float[] color) {
        return this.overrideColors ? this.fmlLogColor3f : color;
    }

    public OptionalInt getBackgroundColor() {
        return this.overrideColors ? OptionalInt.of(0xFF000000 | this.backgroundColor) : OptionalInt.empty();
    }

    public int getBarColor(int color) {
        return this.overrideColors ? color & 0xFF000000 | this.barColor : color;
    }

    public int getBarBorderColor(int color) {
        return this.overrideColors ? color & 0xFF000000 | this.barBorderColor : color;
    }

    public int getMenuBackgroundBrightness() {
        return this.menuBackgroundBrightness;
    }

    public int getMenuInnerBackgroundBrightness() {
        return this.menuInnerBackgroundBrightness;
    }

    public float getMenuBackgroundScale() {
        return this.menuBackgroundScale;
    }

    public void save() {
        try {
            BufferedWriter writer = Files.newBufferedWriter(KubeJSPaths.CLIENT_PROPERTIES);
            try {
                this.properties.store(writer, "KubeJS Client Properties");
            } catch (Throwable var5) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }
                throw var5;
            }
            if (writer != null) {
                writer.close();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }
}